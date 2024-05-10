package com.s_giken.training.batch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
			Date date = format.parse(args[0]);

			System.out.println(year + "年" + month + "月分の請求情報を確認しています。");

			if (date.equals(this.jdbcTemplate.queryForObject(
					"SELECT billing_ym FROM T_BILLING_STATUS",
					Date.class))

					&& (this.jdbcTemplate.queryForObject(
							"SELECT is_commited FROM T_BILLING_STATUS",
							boolean.class)) == true) {
				return;
			} else {
				this.jdbcTemplate.update(
						"DELETE FROM T_BILLING_STATUS WHERE billing_ym = date");

				this.jdbcTemplate.update(
						"INSERT INTO T_BILLING_STATUS(billing_ym, is_commit) VALUES(date, false)");
			}
		} catch (ParseException e) {
			System.out.println("年月をyyyyMMの文字列で入力してください。");
		}

		logger.info("-".repeat(40));
	}
}
