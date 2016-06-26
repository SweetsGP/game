package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class DataBase {
	// データベース名
	private static final String DB_NAME = "test.db";
	// タイムアウト時間
	private static final int DB_TIMEOUT_SEC = 30;
	
	// コネクション
	private static Connection conn = null;

	public DataBase() {}
	
	/**
	 * データベースとの接続
	 */
	public static void openDB() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + FilePath.dbDirPath + DB_NAME);
		} catch (SQLException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "データベースへの接続に失敗しました。",
					Main.GAME_TITLE, JOptionPane.ERROR_MESSAGE);
			Main.exitFlag = Main.EXIT_ERROR;
		}
	}
	
	/**
	 * データベースとの切断
	 */
	public static void closeDB() {
		try {
			if(conn != null)
				conn.close();
		} catch(SQLException sqle) {
			System.err.println(sqle);
		}
	}
	
	/**
	 * executeQuery()のラッパーメソッド
	 * @param st SQL文(SELECT文 など)
	 * @return ArrayList<HashMap<String, String>> 実行結果
	 *   内容) resultList[0] => {
	 *         "属性名1" => 値1,
	 *         "属性名2" => 値2,
	 *         ...
	 *       }
	 *       resultList[1] => {
	 *         ... 以下同じ
	 * @throws SQLException
	 */
	public static ArrayList<HashMap<String, String>> wExecuteQuery(String st)
			throws SQLException {
		Statement statement = null;
		ResultSet rs = null;
		ResultSetMetaData rsMetaData;
		int rsColumnCount;
		HashMap<String, String> resultOneData;
		String fieldName;
		String getData;
		ArrayList<HashMap<String, String>> resultList = null;
		
		try {
			statement = conn.createStatement();
			statement.setQueryTimeout(DB_TIMEOUT_SEC);
			
			// ステートメント実行
			rs = statement.executeQuery(st);
			// フィールド名取得
			rsMetaData = rs.getMetaData();
			// 列数取得
			rsColumnCount = rsMetaData.getColumnCount();
			
			// データ格納
			resultList = new ArrayList<HashMap<String, String>>();

			while(rs.next()){
				// 1件分のデータ(連想配列)
				resultOneData = new HashMap<String, String>();
				
				for (int i = 1; i <= rsColumnCount; i++) {
					// フィールド名
					fieldName = rsMetaData.getColumnName(i);
					// フィールド名に対するデータ
					getData = rs.getString(fieldName);
					if (getData == null) {
						getData = "";
					}
					// データ格納(フィールド名, データ)
					resultOneData.put(fieldName, getData);
				}
				// 1件分のデータを格納
				resultList.add(resultOneData);
			}
			
		} finally {
			if (rs != null) {
				rs.close();
			}
			
			if (statement != null) {
				statement.close();
			}
		}
		
		return resultList;
	}
	
	/**
	 * executeUpdate()のラッパーメソッド
	 * @param st SQL文(UPDATE文 など)
	 * @throws SQLException
	 */
	public static void wExecuteUpdate(String st) throws SQLException {
		Statement statement = null;
		
		try {
			statement = conn.createStatement();
			statement.setQueryTimeout(DB_TIMEOUT_SEC);
			
			// ステートメント実行
			statement.executeUpdate(st);
			
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

}
