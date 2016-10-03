package itemhandler;

import gamesystem.HasMonstersPanel;
import main.PanelController;

public class TargetMonstersPanel extends HasMonstersPanel {
	private int mSelectedTarget = ITEM_NO_SELECTED;

	public TargetMonstersPanel(PanelController c) {
		super();
	}
	
	/**
	 * キー入力受付前の定期処理
	 */
	@Override
	protected void periodicOpBeforeInput() {
		if (mSelectedItem != ITEM_NO_SELECTED) {
			actionItemSelected(mSelectedItem);
			mSelectedItem = ITEM_NO_SELECTED;
		}
	}
	
	/**
	 * ループ処理終了直前の処理(最後の1回のみ)
	 */
	@Override
	protected void finalOp() {
		// no operation
	}
	
	/**
	 * アイテム選択時の処理
	 * @param selectedItemIndex 選択されたアイテムのインデックス
	 */
	@Override
	public void actionItemSelected(int selectedItemIndex) {
		mSelectedTarget = selectedItemIndex;
		stop();
	}
	
	/**
	 * もどるボタンの処理
	 */
	public void onClickBtnBack() {
		this.requestFocus(false);
		this.setVisible(false);
		stop();
	}
	
	/**
	 * 選択されたモンスターのインデックスを返す
	 * @return
	 */
	public int getSelectedMonster() {
		int returnVal = mSelectedTarget;
		mSelectedTarget = ITEM_NO_SELECTED;
		return returnVal;
	}

}
