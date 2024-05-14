package com.s_giken.training.batch;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class BatchApplication implements CommandLineRunner {
	private final Logger logger = LoggerFactory.getLogger(BatchApplication.class);
	private final JdbcTemplate jdbcTemplate;

	/**
	 * SpringBoot エントリポイント
	 * 
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param jdbcTemplate SpringBootから注入される JdbcTemplate オブジェクト
	 */
	public BatchApplication(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * コマンドラインプログラムのエントリ―ポイント
	 * 
	 * @param args コマンドライン引数
	 */
	@Override
	@Transactional
	public void run(String... args) throws RuntimeException {
		logger.info("-".repeat(40));

		// TODO: ここにバッチ処理のコードを記述する
		// * データベースからデータを取得する
		// * データを加工する
		// * 加工したデータをデータベースに登録する

		try {
			String year = args[0].substring(0, 4);
			String month = args[0].substring(4, 6);

			System.out.println(year + "年" + month + "月分の請求情報を確認しています。");

			LocalDate localDate =
					LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);

			int count = this.jdbcTemplate.queryForObject(
					"SELECT COUNT(*) FROM T_BILLING_STATUS WHERE billing_ym = ? AND is_commit = true",
					Integer.class, localDate);

			if (count > 0) {
				System.out.println("対象年月が含まれているレコードが存在したため、処理を中断します。");
				return;
			} else {

				this.jdbcTemplate.update(
						"DELETE FROM T_BILLING_STATUS WHERE billing_ym = ?", localDate);

				this.jdbcTemplate.update(
						"DELETE FROM T_BILLING_DETAIL_DATA WHERE billing_ym = ?", localDate);

				this.jdbcTemplate.update(
						"DELETE FROM T_BILLING_DATA WHERE billing_ym = ?", localDate);

				System.out.println("データベースから" + year + "年" + month + "月分の未確定請求情報を削除しました。");

				System.out.println(year + "年" + month + "月分の請求ステータス情報を追加しています。");

				this.jdbcTemplate.update(
						"INSERT INTO T_BILLING_STATUS(billing_ym, is_commit) VALUES(?, false)",
						localDate);

				System.out.println("1件追加しました。");


				System.out.println(year + "年" + month + "月分の請求データ情報を追加しています。");

				// 1
				List<Map<String, Object>> chargeList =
						jdbcTemplate.queryForList(
								"SELECT * FROM T_CHARGE WHERE (start_date <= ?) AND (end_date IS NULL OR end_date >= ?)",
								localDate.with(TemporalAdjusters.lastDayOfMonth()), localDate);
				// 2
				int sum = this.jdbcTemplate.queryForObject(
						"SELECT SUM(amount) FROM T_CHARGE WHERE (start_date <= ?) AND (end_date IS NULL OR end_date >= ?)",
						Integer.class, localDate.with(TemporalAdjusters.lastDayOfMonth()),
						localDate);

				List<Map<String, Object>> memberList =
						jdbcTemplate.queryForList(
								"SELECT * FROM T_MEMBER WHERE (start_date <= ?) AND (end_date IS NULL OR end_date >= ?)",
								localDate.with(TemporalAdjusters.lastDayOfMonth()), localDate);

				int countMember = 0;
				int countCharge = 0;

				//3
				for (Map<String, Object> memberMap : memberList) {
					// 対象年月
					Date billing_ym = java.sql.Date.valueOf(localDate);
					// 加入者情報の加入ID
					Integer member_id = (Integer) memberMap.get("member_id");
					// 請求データ作成
					String mail = (String) memberMap.get("mail");
					String memberName = (String) memberMap.get("name");
					String address = (String) memberMap.get("address");
					Date member_start_date = (Date) memberMap.get("start_date");
					Date member_end_date = (Date) memberMap.get("end_date");
					Integer payment_method = (Integer) memberMap.get("payment_method");
					int amount = sum;
					double tax_ratio = 0.1;
					int total = (int) (amount * (1 + tax_ratio));

					this.jdbcTemplate.update(
							"INSERT INTO T_BILLING_DATA (billing_ym, member_id, mail, name, address, start_date, end_date, payment_method, amount, tax_ratio, total) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
							billing_ym, member_id, mail, memberName, address, member_start_date,
							member_end_date, payment_method, amount, tax_ratio, total);

					countMember++;

					// 請求明細データ作成
					for (Map<String, Object> chargeMap : chargeList) {
						Integer charge_id = (Integer) chargeMap.get("charge_id");
						String chargeName = (String) chargeMap.get("name");
						int chargeAmount = (int) chargeMap.get("amount");
						Date charge_start_date = (Date) chargeMap.get("start_date");
						Date charge_end_date = (Date) chargeMap.get("end_date");

						this.jdbcTemplate.update(
								"INSERT INTO T_BILLING_DETAIL_DATA (billing_ym, member_id, charge_id, name, amount, start_date, end_date) VALUES(?, ?, ?, ?, ?, ?, ?)",
								billing_ym, member_id, charge_id, chargeName, chargeAmount,
								charge_start_date, charge_end_date);

						countCharge++;
					}

				}

				System.out.println(countMember + "件追加しました。");

				System.out.println(year + "年" + month + "月分の請求明細データ情報を追加しています。");

				System.out.println(countCharge + "件追加しました。");

			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("エラー：請求対象年月が入力されていません。");
		} catch (DateTimeException e) {
			System.out.println("エラー：請求対象年月は6文字の数字、YYYYMMで入力してください。");
		} catch (NumberFormatException e) {
			System.out.println("エラー：請求対象年月はYYYYMMの数字で、1つのみ入力してください。");
		}

		logger.info("-".repeat(40));
	}
}
