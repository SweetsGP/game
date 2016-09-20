package npc;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;

import gamesystem.HasItemsPanel;
import gamesystem.ItemsPanel;
import gamesystem.ProductsListPanel;
import main.PanelController;

public class MerchantMenuPanel extends ItemsPanel {
	// 暫定
	public static final int RUNNING_MENU_LOOP = 0;
	public static final int EXIT_MENU_LOOP = 1;

	public static int exitMenuLoopFlag = RUNNING_MENU_LOOP;
	// =====

	public static final int MENU_BUY = 0;
	public static final int MENU_SELL = 1;
	
	ProductsListPanel productsListPanel;
	HasItemsPanel hasItemsPanel;
	JButton btnBuy, btnSell;
	
	private ArrayList<HashMap<String, String>> mProductsInfo;
	
	public MerchantMenuPanel(PanelController pc, ProductsListPanel plp, HasItemsPanel hip) {
		super(pc);
		
		productsListPanel = plp;
		hasItemsPanel = hip;
		
		btnBuy = new JButton("かう");
		btnBuy.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnBuy.setBorderPainted(false);
		btnBuy.setBounds(10, 20, 260, 50);
		btnBuy.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mSelectedItem = MENU_BUY;
//				setCursor(MENU_ITEM_A);
			}
		});
		pItemList.add(btnBuy);
		addToCursorArr(btnBuy);
		
		btnSell = new JButton("うる");
		btnSell.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnSell.setBorderPainted(false);
		btnSell.setBounds(10, 90, 260, 50);
		btnSell.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mSelectedItem = MENU_SELL;
//				setCursor(MENU_ITEM_B);
			}
		});
		pItemList.add(btnSell);
		addToCursorArr(btnSell);
		
		btnBack.setText("やめる");
	}
	
	public void setMerchantProducts(ArrayList<HashMap<String, String>> productsInfo) {
		mProductsInfo = productsInfo;
	}
	
	// ==================================================
	//  ループ内処理
	// ==================================================
	/**
	 * キー入力受付前の定期処理
	 */
	@Override
	protected void periodicOpBeforeInput() {
		// 暫定
		if (exitMenuLoopFlag != RUNNING_MENU_LOOP) {
			stop();
		}
		// =====

		if (mSelectedItem != ITEM_NO_SELECTED) {
			actionItemSelected(mSelectedItem);
			mSelectedItem = ITEM_NO_SELECTED;
		}
	}

	/**
	 * 上矢印キー入力受付時の処理
	 */
	@Override
	protected void onPressKeyUp() {
		moveCursor(CURSOR_INDEX_MINUS);
	}

	/**
	 * 下矢印キー入力受付時の処理
	 */
	@Override
	protected void onPressKeyDown() {
		moveCursor(CURSOR_INDEX_PLUS);
	}

	/**
	 * Zキー入力受付時の処理
	 */
	@Override
	protected void onPressKeyZ() {
		clickOnCursor();
	}

	/**
	 * スペースキー入力受付時の処理
	 */
	@Override
	protected void onPressKeySpace() {
		btnBack.doClick();
	}

	/**
	 * キー入力受付後の定期処理
	 */
	@Override
	protected void periodicOpAfterInput() {
		// no operation
	}

	// ==================================================
	//  アイテム選択時処理
	// ==================================================
	/**
	 * アイテム選択時の処理
	 * @param selectedItemIndex 選択されたアイテムのインデックス
	 */
	@Override
	public void actionItemSelected(int selectedItemIndex) {
		switch(selectedItemIndex) {
		case MENU_BUY:  // てもち
			setEnabledInner(false);
			productsListPanel.showPanel(mProductsInfo);
			productsListPanel.loop();
			setEnabledInner(true);
			this.requestFocus(true);
			break;

		case MENU_SELL:  // アイテム
			setEnabledInner(false);
			hasItemsPanel.showPanel();
			hasItemsPanel.setMode(HasItemsPanel.MODE_SELL);
			hasItemsPanel.loop();
			hasItemsPanel.setMode(HasItemsPanel.MODE_DEFAULT);
			setEnabledInner(true);
			this.requestFocus(true);
			break;

		default:
			break;
		}
	}

	// ==========
	// その他
	// ==========
	/**
	 * 各メニュー項目のボタンの有効化/無効化
	 * @param b true -> 有効化, false -> 無効化
	 */
	public void setEnabledInner(boolean b) {
		btnBuy.setEnabled(b);
		btnSell.setEnabled(b);
		btnBack.setEnabled(b);
	}
}

