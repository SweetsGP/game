package gameutil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import main.DataBase;

public class DBAccess {
	// テーブル名 (test)
	public static final String TABLE_HAS_MONSTERS = "testhasmonsters";
	public static final String TABLE_MONSTERS = "testmonsters";
	public static final String TABLE_EXP = "testexp";
	public static final String TABLE_ITEMS = "testitems";
	
	public DBAccess() {}
	
	// ==================================================
	//  モンスター関連
	// ==================================================
	
	/**
	 * てもちモンスターの一覧情報をデータベースから取得する
	 * @return てもちモンスターの情報
	 */
	public ArrayList<HashMap<String, String>> getHasMonstersInfo() {
		ArrayList<HashMap<String, String>> hasMonstersInfo = null;
		
		try {
			// 順番どおり(orderNumの昇順)に並び替え [orderNumがnullなら手持ちではない -> 取得しない]
			hasMonstersInfo = DataBase.wExecuteQuery("select * from " + TABLE_HAS_MONSTERS + " where orderNum is not null order by orderNum asc");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return hasMonstersInfo;
	}
	
	/**
	 * モンスターの詳細情報を取得する
	 * @param 詳細情報を取得したいモンスターのID
	 * @return 引数で与えたIDのモンスターに関する情報(monstersテーブルの情報 + expテーブルの情報)
	 */
	public ArrayList<HashMap<String, String>> getMonsterDetailInfo(int monsterId) {
		ArrayList<HashMap<String, String>> monsterDetailInfo = null;
		ArrayList<HashMap<String, String>> nextExpInfo = null;

		try {
			monsterDetailInfo = DataBase.wExecuteQuery("select * from " + TABLE_MONSTERS + " where monsterId = "
					+ Integer.toString(monsterId));
			nextExpInfo = DataBase.wExecuteQuery("select * from " + TABLE_EXP + " where level = "
					+ Integer.toString(Integer.parseInt(monsterDetailInfo.get(0).get("level"))+1));
			monsterDetailInfo.add(nextExpInfo.get(0));
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return monsterDetailInfo;
	}
	
	// ==================================================
	//  アイテム関連
	// ==================================================
	/**
	 * 所持しているアイテムの一覧情報をデータベースから取得する
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getHasItemsInfo() {
		ArrayList<HashMap<String, String>> hasItemsInfo = null;

		try {
			// 所持数が1以上のアイテムのみ取得
			hasItemsInfo = DataBase.wExecuteQuery("select * from " + TABLE_ITEMS + " where count > 0 order by itemId asc");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return hasItemsInfo;
	}

}
