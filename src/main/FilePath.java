package main;

import java.io.File;

/**
 * 各ディレクトリへのファイルパスを取得
 * @author kitayamahideya
 *
 */
public class FilePath {
	// os情報
//	private static String osName;
//	private static int osNum;
	
//	private static final int UNDEF_OS = -1;
//	private static final int IS_WINDOWS = 1;
//	private static final int IS_MAC = 2;
//	private static final int IS_LINUX = 3;
//	private static final int IS_SUNOS = 4;
	
	// パス情報
	// パス区切り文字(Windows -> '\', Mac, Linux -> '/')
	public static final String fs = File.separator;
	
	// カレントディレクトリへのパス
	// (Windows -> "C:\path\to\GameProject\", Mac, Linux -> "/path/to/GameProject/")
	public static final String baseDirPath = System.getProperty( "user.dir" ) + fs;
	// dbフォルダまでのパス
	public static final String dbDirPath = baseDirPath + "db" + fs;
	// fontフォルダまでのパス
	public static final String fontDirPath = baseDirPath + "font" + fs;
	// imgフォルダまでのパス
	public static final String imgDirPath = baseDirPath + "img" + fs;
	// soundフォルダまでのパス
	public static final String soundDirPath = baseDirPath + "sound" + fs;
	// textフォルダまでのパス
	public static final String textDirPath = baseDirPath + "text" + fs;
	
	// img/
	// charaフォルダまでのパス
	public static final String charaDirPath = imgDirPath + "chara" + fs;
	// battleフォルダまでのパス
	public static final String battleDirPath = imgDirPath + "battle" + fs;
	// mapフォルダまでのパス
	public static final String mapDirPath = imgDirPath + "map" + fs;
	// mostersフォルダまでのパス
	public static final String monstersDirPath = imgDirPath + "monsters" + fs;
	// itemsフォルダまでのパス
	public static final String itemsDirPath = imgDirPath + "items" + fs;
	
	// sound/
	// bgmフォルダまでのパス
	public static final String bgmDirPath = soundDirPath + "bgm" + fs;
	// seフォルダまでのパス
	public static final String seDirPath = soundDirPath + "se" + fs;
	
	public FilePath() {
//		osName = System.getProperty("os.name").toLowerCase();
//		
//		if (osName.startsWith("windows")) {
//			osNum = IS_WINDOWS;
//		} else if ( osName.startsWith("macos") ) {
//			osNum = IS_MAC;
//		} else if ( osName.startsWith("linux") ) {
//			osNum = IS_LINUX;
//		} else {
//			osNum = UNDEF_OS;
//		}
	}

}
