package gameutil;

import javax.swing.JOptionPane;

import gamesystem.CharaStatus;
import main.Main;
import main.SaveDataIO;

public class SaveDataAccess {
	
	public SaveDataAccess() {
		
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
