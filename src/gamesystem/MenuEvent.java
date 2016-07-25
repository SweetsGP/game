package gamesystem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import main.DataBase;
import main.Main;
import main.PanelController;
import main.SaveDataIO;

public class MenuEvent {
	
	public MenuEvent() {}
	
	/**
	 * てもちモンスターの一覧情報をデータベースから取得する
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getHasMonstersInfo() {
		ArrayList<HashMap<String, String>> hasMonstersInfo = null;
		
		try {
			// 順番どおり(orderNumの昇順)に並び替え [orderNumがnullなら手持ちではない -> 取得しない]
			hasMonstersInfo = DataBase.wExecuteQuery("select * from testhasmonsters where orderNum is not null order by orderNum asc");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return hasMonstersInfo;
	}
	
	/**
	 * 所持しているアイテムの一覧情報をデータベースから取得する
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getHasItemsInfo() {
		ArrayList<HashMap<String, String>> hasItemsInfo = null;

		try {
			// 所持数が1以上のアイテムのみ取得
			hasItemsInfo = DataBase.wExecuteQuery("select * from testitems where count > 0 order by itemId asc");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return hasItemsInfo;
	}
	
	/**
	 * 現在のステータスをセーブデータに書き出す
	 */
	public void writeReport() {
		
		// test code
		CharaStatus writetest = new CharaStatus();
		writetest.setCharaName("Hiroshi");
		writetest.setCharaSex(1);
		
		SaveDataIO.writeSaveData(writetest);
		
		CharaStatus readtest = SaveDataIO.readSaveData();
		System.out.println(readtest.getCharaName());
		System.out.println(readtest.getCharaSex());
		/////
		
		JOptionPane.showMessageDialog(null, "セーブデータを書き込みました",
				Main.GAME_TITLE, JOptionPane.INFORMATION_MESSAGE);
	}

}
