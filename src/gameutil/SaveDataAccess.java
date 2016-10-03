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
		
		SaveDataIO.writeSaveData(Main.saveData);
		
		CharaStatus readtest = SaveDataIO.readSaveData();
		System.out.println("BGM Volume: " + readtest.getVolumeBgm());
		System.out.println("SE Volume: " + readtest.getVolumeSe());
		
		JOptionPane.showMessageDialog(null, "セーブデータを書き込みました",
				Main.GAME_TITLE, JOptionPane.INFORMATION_MESSAGE);
	}

}
