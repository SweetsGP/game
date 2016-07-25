package main;

import java.sql.SQLException;

public class Init {
	
	public Init() {}
	
	/**
	 * セーブデータを消去します
	 */
	public static void deleteSaveData() {
		SaveDataIO.deleteSaveData();
	}
	
	/**
	 * ゲームの進行状況に応じて書き換えが生じるすべてのテーブルの
	 * 該当フィールドの値を初期値に戻す
	 */
	public static void resetDataBases() {
		resetHasMonstersTable();
		resetItemsTable();
	}
	
	/**
	 * てもちモンスター情報テーブルの初期化
	 */
	public static void resetHasMonstersTable() {
		try {
			DataBase.wExecuteUpdate("delete from testhasmonsters");
			
			// test code
			DataBase.wExecuteUpdate("insert into testhasmonsters values ("
					+ "1, 2, 1, 'gorityuu', 'gorityu.png', 55, 15000, 10, 150, 'まひ', 100, 100, 100,"
					+ "1, 15,"
					+ "2, 20,"
					+ "null, null,"
					+ "null, null)");
			DataBase.wExecuteUpdate("insert into testhasmonsters values ("
					+ "2, 1, 1, 'ウホチュウ', 'gorityu.png', 33, 9800, 90, 90, null, 80, 65, 50,"
					+ "1, 30,"
					+ "2, 11,"
					+ "null, null,"
					+ "null, null)");
			DataBase.wExecuteUpdate("insert into testhasmonsters values ("
					+ "3, null, 1, 'ピカチュウ', 'gorityu.png', 33, 9800, 90, 90, null, 80, 65, 50,"
					+ "1, 30,"
					+ "2, 11,"
					+ "null, null,"
					+ "null, null)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * アイテム情報テーブルの初期化
	 */
	public static void resetItemsTable() {
		try {
			DataBase.wExecuteUpdate("update testitems set count = 0 where count != 0");
			
			// test code
			DataBase.wExecuteUpdate("update testitems set count = 10 where itemId = 1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
