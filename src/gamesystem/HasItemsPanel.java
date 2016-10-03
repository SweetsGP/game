package gamesystem;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import main.FilePath;
import main.PanelController;

public class HasItemsPanel extends ItemsPanel {
	// 暫定 -> 後ほど削除
	public static final int RUNNING_HIP_LOOP = 0;
	public static final int EXIT_HIP_LOOP = 1;
	
	public static int exitHIPLoopFlag = RUNNING_HIP_LOOP;
	
	public static final int MODE_DEFAULT = 0;
	public static final int MODE_SELL = 1;
	
	protected static int mMode = MODE_DEFAULT;
	// =====
	
	protected ArrayList<HashMap<String, String>> mHasItems;
	
	public HasItemsPanel(PanelController pc) {
		super(pc);
		
		mHasItems = null;
	}
	
	/**
	 * パネルの可視化
	 */
	@Override
	public void showPanel() {
		mHasItems = dba.getHasItemsInfo();

		// 更新(2回目以降のアクセス)なら、以前のボタンを除去して、アイテムのリスト表示領域のサイズをリセット
		if (!Objects.equals(btnItems, null)) {
			for(int i=0; i<btnItems.length; i++) {
				if (!Objects.equals(btnItems[i], null)) {
					pItemList.remove(btnItems[i]);
				}
			}

			pItemList.setPreferredSize(new Dimension(280, 450));
			resetCursorArr();
		}

		mNumOfItems = mHasItems.size();  // どうぐの種類数 -> キズぐすり、まひなおし といった単位で
		btnItems = new JButton[mNumOfItems];
		ImageIcon[] iconItems = new ImageIcon[mNumOfItems];

		for (int i=0; i<mNumOfItems; i++) {
			if (i == 6) {
				// panelサイズ変更
				pItemList.setPreferredSize(new Dimension(280, 450 + 75*(mNumOfItems - i)));
				revalidate();
			}

			iconItems[i] = new ImageIcon(FilePath.itemsDirPath + mHasItems.get(i).get("img"));

			btnItems[i] = new JButton(mHasItems.get(i).get("name") + "(" + mHasItems.get(i).get("count") + ")", iconItems[i]);
			btnItems[i].setFont(new Font("PixelMplus10",Font.PLAIN,13));
			btnItems[i].setHorizontalAlignment(JButton.LEFT);
			btnItems[i].setBorderPainted(false);
			btnItems[i].setBounds(0, 75*i, 260, 70);
			btnItems[i].addActionListener(new ActionListener(){
				private int mBtnNum;
				
				public ActionListener setBtnNum(int btnNum) {
					this.mBtnNum = btnNum;
					return this;
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					mSelectedItem = this.mBtnNum;
				}
			}.setBtnNum(i));
			pItemList.add(btnItems[i]);
			addToCursorArr(btnItems[i]);
		}
		
		super.showPanel();
	}
	
	// 暫定
	/**
	 * キー入力受付前の定期処理
	 */
	@Override
	protected void periodicOpBeforeInput() {
		if (exitHIPLoopFlag != RUNNING_HIP_LOOP) {
			stop();
		}
		
		super.periodicOpBeforeInput();
	}
	// =====
	
	/**
	 * アイテム選択時の処理
	 * @param selectedItemIndex 選択されたアイテムのインデックス
	 */
	@Override
	public void actionItemSelected(int selectedItemIndex) {
		ItemDetailPanel idp;

		idp = (ItemDetailPanel )mController.getPanelInstance(PanelController.ITEM_DETAIL_PANEL);
		if (mMode == MODE_SELL) {
			idp.setMode(ItemDetailPanel.MODE_PRODUCT);
			idp.setBuyOrSell(ItemDetailPanel.PRODUCT_SELL);
		}
		
		mController.showItemDetailPanel(mHasItems.get(selectedItemIndex));
//		idp.loop();
		PanelController.callLoop(PanelController.ITEM_DETAIL_PANEL);
		
		if (mMode == MODE_SELL) {
			idp.setMode(ItemDetailPanel.MODE_DEFAULT);
		}
		
		this.setVisible(false);
		this.showPanel();
	}
	
	/**
	 * 表示モードの設定を行います
	 * @param m
	 */
	public void setMode(int m) {
		switch (m) {
		case MODE_DEFAULT:
			mMode = m;
			break;

		case MODE_SELL:
			mMode = m;
			break;

		default:
			break;
		}
	}
}
