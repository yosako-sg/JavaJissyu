package com.s_giken.training.batch;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
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
	public void run(String... args) throws RuntimeException {
		try {
			logger.info("-".repeat(40));

			LocalDate yearMonth = this.parseLocalDate(args);

			this.createBillingData(yearMonth);

		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("エラー：請求対象年月が入力されていません。", e);
		} catch (DateTimeException e) {
			logger.error("エラー：請求対象年月は6文字の半角の数字、YYYYMMで入力してください。引数は1つのみです。", e);
		} catch (NullPointerException e) {
			logger.error("エラー：入力された年月の加入者情報は存在していません", e);
		} catch (RuntimeException e) {
			logger.error("エラーが発生しました。", e);
		} finally {
			logger.info("-".repeat(40));
		}
	}

	// コマンドライン引数(String型)をLocalDate型に型変換
	public LocalDate parseLocalDate(String[] args) {
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				.appendPattern("yyyyMM")
				.parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
				.toFormatter();

		return LocalDate.parse(args[0], formatter);
	}

	// 請求データ作成
	@Transactional
	public void createBillingData(LocalDate yearMonth) {
		String yearMonthstr = yearMonth.format(DateTimeFormatter.ofPattern("yyyy年MM月"));

		logger.info(String.format("%s分の請求情報を確認しています。", yearMonthstr));

		Integer count = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM T_BILLING_STATUS WHERE billing_ym = ? AND is_commit = true",
				Integer.class, yearMonth);

		if (count > 0) {
			logger.info(String.format("%s分の請求明細はすでに確定しています。処理を中断します。", yearMonthstr));
			return;
		}

		jdbcTemplate.update(
				"DELETE FROM T_BILLING_STATUS WHERE billing_ym = ?", yearMonth);

		jdbcTemplate.update(
				"DELETE FROM T_BILLING_DETAIL_DATA WHERE billing_ym = ?", yearMonth);

		jdbcTemplate.update(
				"DELETE FROM T_BILLING_DATA WHERE billing_ym = ?", yearMonth);

		logger.info(String.format("データベースから%s分の未確定請求情報を削除しました。", yearMonthstr));

		logger.info(String.format("%s分の請求ステータス情報を追加しています。", yearMonthstr));

		Integer countBillingStatus = jdbcTemplate.update(
				"INSERT INTO T_BILLING_STATUS(billing_ym, is_commit) VALUES(?, false)", yearMonth);

		logger.info(countBillingStatus + "件追加しました。");

		logger.info(String.format("分の請求データ情報を追加しています。", yearMonthstr));

		// 月初日と月末日
		LocalDate firstDate = yearMonth;
		LocalDate endDate = yearMonth.with(TemporalAdjusters.lastDayOfMonth());

		// 条件に一致する料金情報をすべて取得
		List<Map<String, Object>> chargeList =
				jdbcTemplate.queryForList(
						"SELECT * FROM T_CHARGE WHERE (start_date <= ?) AND (end_date IS NULL OR end_date >= ?)",
						endDate, firstDate);

		// 条件に一致する料金情報の月額料金を合計
		int sum = jdbcTemplate.queryForObject(
				"SELECT SUM(amount) FROM T_CHARGE WHERE (start_date <= ?) AND (end_date IS NULL OR end_date >= ?)",
				Integer.class, endDate, firstDate);

		// 条件に一致する加入者情報をすべて取得
		List<Map<String, Object>> memberList =
				jdbcTemplate.queryForList(
						"SELECT * FROM T_MEMBER WHERE (start_date <= ?) AND (end_date IS NULL OR end_date >= ?)",
						endDate, firstDate);

		int countMember = 0;
		int countCharge = 0;

		// 条件に一致した加入者情報を基にそれぞれのデータを作成、テーブルを追加する
		for (Map<String, Object> memberMap : memberList) {
			// 対象年月
			Date billing_ym = java.sql.Date.valueOf(yearMonth);
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

			jdbcTemplate.update(
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

				jdbcTemplate.update(
						"INSERT INTO T_BILLING_DETAIL_DATA (billing_ym, member_id, charge_id, name, amount, start_date, end_date) VALUES(?, ?, ?, ?, ?, ?, ?)",
						billing_ym, member_id, charge_id, chargeName, chargeAmount,
						charge_start_date, charge_end_date);

				countCharge++;
			}

			logger.info(countMember + "件追加しました。");

			logger.info(String.format("%s分の請求明細データ情報を追加しています。", yearMonthstr));

			logger.info(countCharge + "件追加しました。");

		}
	}
}
