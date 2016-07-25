package main;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import gamesystem.CharaStatus;

/**
 * セーブデータ入出力クラス
 *   (セーブデータが2つ以上のファイルになる場合は改変必要)
 *   CharaStatusクラスに対して変更を加えます
 * @author kitayamahideya
 *
 */
public class SaveDataIO {
	// セーブデータのパス
	private static final String SAVE_DATA_FILE_PATH = FilePath.baseDirPath + "test.dat";
	// セーブデータのファイルオブジェクト
	private static File saveData = new File(SAVE_DATA_FILE_PATH);
	
	public SaveDataIO () {}
	
	/**
	 * セーブデータが存在するかチェックする
	 * @return 存在すればtrue、存在しなければfalse
	 */
	public static boolean checkExistsSaveData() {
		boolean e = saveData.exists();
		return e;
	}
	
	/**
	 * セーブデータを読み込み、与えられたCharaStatusクラスの各メンバにセットする
	 * @return セーブデータから読み込んだ内容を格納したCharaStatusオブジェクト
	 */
	public static CharaStatus readSaveData() {
		CharaStatus readStatus = null;
		
		CryptIO input = new CryptIO();
		input.openDecryptFileStream(SAVE_DATA_FILE_PATH);
		
		try {
			ObjectInputStream objInStream = new ObjectInputStream(input.getDecryptFileStream());
			
			readStatus = (CharaStatus ) objInStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		input.closeDecryptFileStream();
		
		return readStatus;
	}
	
	/**
	 * CharaStatusクラスの情報を、セーブデータに書き出す
	 * @param セーブデータとして書き出したい状態を保持したCharaStatusオブジェクト
	 */
	public static void writeSaveData(CharaStatus saveStatus) {
		
		CryptIO output = new CryptIO();
		output.openEncryptFileStream(SAVE_DATA_FILE_PATH);
		
		try {
			ObjectOutputStream objOutStream = new ObjectOutputStream(output.getEncryptFileStream());
			
			objOutStream.writeObject(saveStatus);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		output.closeEncryptFileStream();
	}
	
	/**
	 * セーブデータを削除する
	 */
	public static void deleteSaveData() {
		if (saveData.delete()) {
			JOptionPane.showMessageDialog(null, "セーブデータを消去しました",
					Main.GAME_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
}
