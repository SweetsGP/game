package main;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.Hashtable;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;

import javax.swing.JOptionPane;

import gamesystem.MainFrame;

public class Main {
	// ゲームタイトル名
	public static final String GAME_TITLE = "RPG";
	// ゲームループ終了フラグ定数
	public static final int RUNNING = 0;
	public static final int EXIT_OK = 1;
	public static final int EXIT_ERROR = 2;
	
	// ゲームループ終了フラグ -> EXIT_OK もしくは EXIT_ERROR で各ループを強制的に離脱
	public static int exitFlag = RUNNING;

	public Main() {}

	/**
	 * Javaバージョンチェック
	 * JRE1.7以上でない場合はエラーメッセージを出して終了
	 */
	private void checkJavaVer() {
		final boolean isNgJava = 
				(Double.parseDouble(System.getProperty("java.specification.version")) < 1.7);
		if (isNgJava) {
			JOptionPane.showMessageDialog(null, "Your Java version (JRE"
					+ System.getProperty("java.specification.version") + ") is not supported.",
					GAME_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 独自フォント導入
	 *  ( PixelMplusフォント: http://itouhiro.hatenablog.com/entry/20130602/font )
	 */
	private void setFontToEnv() {
		try {
			Font mplusfont10 = Font.createFont(Font.TRUETYPE_FONT, new File( FilePath.fontDirPath
					+ "PixelMplus_20130602" + FilePath.fs + "PixelMplus10_Regular.ttf" ));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(mplusfont10);
			
			Font mplusfont12 = Font.createFont(Font.TRUETYPE_FONT, new File( FilePath.fontDirPath
					+ "PixelMplus_20130602" + FilePath.fs + "PixelMplus12_Regular.ttf" ));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(mplusfont12);
			
		} catch (FontFormatException | IOException e) {
			JOptionPane.showMessageDialog(null, "フォントファイルの読み込みに失敗しました。",
					GAME_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * メインメソッド
	 */
	public static void main(String[] args) throws Exception {
		//		System.out.println(System.getProperty("java.class.path"));
		//		System.out.println(System.getProperty("java.library.path"));
		//		System.out.println(System.getProperty("java.home"));

		Main m = new Main();

		// Javaバージョンチェック
		m.checkJavaVer();

		// 二重起動のチェック
		PreventMultiInstance pmi = new PreventMultiInstance(new File(FilePath.baseDirPath));
		try {
			if (pmi.tryLock()) {  // 二重起動がなければ、lockをかけてゲーム起動
				// ==================================================
				// 初期化処理

				// 独自フォント導入
				m.setFontToEnv();

				// データベース接続
				DataBase.openDB();
				
				// test code(テスト用テーブル生成)
				try {
					// 手持ちモンスターテーブル(仮)
					DataBase.wExecuteUpdate("drop table if exists testhasmonsters");
					DataBase.wExecuteUpdate("create table testhasmonsters("
							+ "getId int,"
							+ "orderNum int,"
							+ "monsterId int,"
							+ "name text,"
							+ "img text,"
							+ "level int,"
							+ "exp int,"
							+ "hp int,"
							+ "maxhp int,"
							+ "state int,"
							+ "attack int,"
							+ "defense int,"
							+ "speed int,"
							+ "skill1Id int,"
							+ "skill1Point int,"
							+ "skill2Id int,"
							+ "skill2Point int,"
							+ "skill3Id int,"
							+ "skill3Point int,"
							+ "skill4Id int,"
							+ "skill4Point int)");
					// モンスター図鑑テーブル(仮)
					DataBase.wExecuteUpdate("drop table if exists testmonsters");
					DataBase.wExecuteUpdate("create table testmonsters("
							+ "monsterId int,"
							+ "name text,"
							+ "img text,"
							+ "type1 int,"
							+ "type2 int,"
							+ "bAttack int,"
							+ "bDefense int,"
							+ "bSpeed int)");
					// 経験値テーブル(仮)
					DataBase.wExecuteUpdate("drop table if exists testexp");
					DataBase.wExecuteUpdate("create table testexp("
							+ "level int,"
							+ "exp int,"
							+ "attackInc int,"
							+ "defenseInc int,"
							+ "speedInc int)");
					// 技テーブル(仮)
					DataBase.wExecuteUpdate("drop table if exists testskills");
					DataBase.wExecuteUpdate("create table testskills("
							+ "skillId int,"
							+ "name text,"
							+ "type int,"
							+ "sp int,"
							+ "power int,"
							+ "hitrate int)");
					// アイテムテーブル(仮)
					DataBase.wExecuteUpdate("drop table if exists testitems");
					DataBase.wExecuteUpdate("create table testitems("
							+ "itemId int,"
							+ "name text,"
							+ "price int,"
							+ "img text,"
							+ "handler text,"
							+ "description text,"
							+ "count int)");
					// NPC情報テーブル(仮)
					DataBase.wExecuteUpdate("drop table if exists testnpcs");
					DataBase.wExecuteUpdate("create table testnpcs("
							+ "id int,"
							+ "name text,"
							+ "talk text,"
							+ "productIds text)");

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

					DataBase.wExecuteUpdate("insert into testmonsters values ("
							+ "1, 'gorityuu',"
							+ "'gorityu.png', 1, 2, 10, 10, 10)");

					DataBase.wExecuteUpdate("insert into testexp values ("
							+ "34, 10000, 2, 3, 5)");
					DataBase.wExecuteUpdate("insert into testexp values ("
							+ "56, 20000, 3, 1, 0)");

					DataBase.wExecuteUpdate("insert into testskills values ("
							+ "1, 'たいあたり', 2, 30, 50, 90)");
					DataBase.wExecuteUpdate("insert into testskills values ("
							+ "2, 'かみなり', 1, 20, 90, 90)");

					DataBase.wExecuteUpdate("insert into testitems values ("
							+ "1, 'キズぐすり', 100, 'kizugusuri.png', 'Kizugusuri', 'HPを50だけ回復する', 10)");
					DataBase.wExecuteUpdate("insert into testitems values ("
							+ "2, 'いいキズぐすり', 150, 'kizugusuri.png', 'Kizugusuri', 'HPを100だけ回復する', 0)");
					DataBase.wExecuteUpdate("insert into testitems values ("
							+ "3, 'すごいキズぐすり', 200, 'kizugusuri.png', 'SugoiKizugusuri', 'HPを150だけ回復する', 3)");
					DataBase.wExecuteUpdate("insert into testitems values ("
							+ "4, '自転車のベル', 0, 'kizugusuri.png', 'BellOfBike', '自転車のベルの音がなるよ〜', 5)");
					
					DataBase.wExecuteUpdate("insert into testnpcs values ("
							+ "1, 'まさお', '', '1, 2, 3, 4')");
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
				////////

				// フレーム(ウィンドウ生成)
				MainFrame mainFrame = new MainFrame(GAME_TITLE);
				mainFrame.setVisible(true);
				
				// サウンドインスタンス生成・初期化
				Sound s = new Sound();

				// 画面遷移管理初期化
				PanelController.init(mainFrame);

				// ==================================================
				// ゲーム内メインルーチン

				while(true) {
					// 終了フラグチェック -> フラグが立っていれば終了する
					if (exitFlag != RUNNING) {
						break;
					}

					// 画面/状態遷移管理開始
					PanelController.loop(s);

					Thread.sleep(100);
				}

				// ==================================================
				// 後処理

				// データベース切断
				DataBase.closeDB();

				// lock解除
				pmi.release();

			} else {  // 二つ目以降の起動は、エラーメッセージを出して終了
				JOptionPane.showMessageDialog(null, "'" + GAME_TITLE + "' はすでに起動しています",
						GAME_TITLE, JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "エラーが発生しました。ゲームを終了します。",
					GAME_TITLE, JOptionPane.ERROR_MESSAGE);
		} finally {
			// データベース切断
			DataBase.closeDB();
			
			// ロックファイルのクローズ
			pmi.close();
		}

		System.exit(0);
	}

}
