package Battle;

public class TypeInfo {
	// タイプ(属性)表 -> データベースにはインデックスを登録して、この配列からタイプ名文字列取得
	public static String[] typeName = {"", "でんき", "ノーマル"};
	
	// タイプ相性表(TODO:ここらへんは調整してください)
	// 0 -> 普通
	// -1 -> 悪い
	// 1 -> 良い
	public static int[][] compatiType = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
}
