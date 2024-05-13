package com.s_giken.training.batch;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

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
			String year = null;
			String month = null;

			year = args[0].substring(0, 4);
			month = args[0].substring(4);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
			LocalDate date = LocalDate.parse(args[0], formatter);

			System.out.println(year + "年" + month + "月分の請求情報を確認しています。");

			if (date.equals(this.jdbcTemplate.queryForObject(
					"SELECT billing_ym FROM T_BILLING_STATUS WHERE is_commited = true",
					LocalDate.class))) {
				return;
			} else {
				this.jdbcTemplate.execute(
						"ALTER TABLE T_BILLING_DETAIL_DATA ADD FOREIGN KEY(billing_ym) REFERENCES T_BILLING_STATUS(billing_ym) ON DELETE CASCADE");

				this.jdbcTemplate.execute(
						"ALTER TABLE T_BILLING__DATA ADD FOREIGN KEY(billing_ym) REFERENCES T_BILLING_STATUS(billing_ym) ON DELETE CASCADE");

				this.jdbcTemplate.update(
						"DELETE FROM T_BILLING_STATUS WHERE billing_ym = date");

				System.out.println("データベースから" + year + "年" + month + "月分の未確定請求情報を削除しました。");

				System.out.println(year + "年" + month + "請求ステータス情報を追加しています。");

				this.jdbcTemplate.update(
						"INSERT INTO T_BILLING_STATUS(billing_ym, is_commit) VALUES(date, false)");
			}

			this.jdbcTemplate.execute(
					"ALTER TABLE T_BILLING_DATA ADD FOREIGN KEY(member_id) REFERENCES T_MEMBER(member_id)");

			this.jdbcTemplate.execute(
					"ALTER TABLE T_BILLING_DETAIL_DATA ADD FOREIGN KEY(member_id) REFERENCES T_MEMBER(member_id)");

			this.jdbcTemplate.execute(
					"ALTER TABLE T_BILLING_DETAIL_DATA ADD FOREIGN KEY(charge_id) REFERENCES T_CHARGE(charge_id)");

			this.jdbcTemplate.queryForObject(
					"SELECT * FROM T_MEMBER WHERE (start_date <= EOMONTH(date)) AND (end_date IS NULL OR end_date >= DATEADD(dd, 1, EOMONTH(date, -1)))",
					LocalDate.class);

		} catch (IllegalArgumentException e) {
			System.out.println("年月をyyyyMMの文字列で入力してください。");
		}

		logger.info("-".repeat(40));
	}
}
