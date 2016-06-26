package itemhandler;

/**
 * アイテムハンドラの雛形
 *   -> アイテムに関する処理は、このクラスを継承したクラスに記述してください
 *   -> 作成されたすべてのアイテムハンドラクラスは、ユーザーの操作によって、ItemHandlerDriverから呼び出されます
 * @author kitayamahideya
 *
 */
abstract class ItemHandler {
	// 定数
	// アイテム効果のバトル中の発動の有無
	static final int ITEM_USE_ON_BATTLE_NG = 0;  // 無し
	static final int ITEM_USE_ON_BATTLE_OK = 1;  // 有り
	
	/**
	 * アイテムフラグ: アイテム効果のバトル中の発動の有無 (0:無し, 1:有り)
	 * (デフォルト -> itemFlag = 0 (無し))
	 */
	protected int itemFlag = ITEM_USE_ON_BATTLE_NG;
	
	/**
	 * アイテムフラグの取得
	 * @return itemFlag 値
	 */
	abstract int getItemFlag();
	//	各ハンドラには、以下のコードを必ず記述
	//	==========
	//	@Override
	//	int getItemFlag() {
	//		return itemFlag;
	//	}
	//	==========
	
	/**
	 * アイテム効果発動時の処理
	 * @throws ItemHandlingFailedException
	 */
	abstract void itemEffect() throws ItemHandlingFailedException;
	
	/**
	 * アイテムの破棄処理
	 * @throws ItemHandlingFailedException
	 */
	abstract void itemThrowAway() throws ItemHandlingFailedException;

}
