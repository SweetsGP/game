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
//import javax.swing.JLabel;

import main.FilePath;
import main.PanelController;

public class ProductsListPanel extends ItemsPanel {
	
	protected ArrayList<HashMap<String, String>> mProducts;
	
////JLabel lHasMoney, lCounter;
	
	public ProductsListPanel(PanelController pc) {
		super(pc);
		
		mProducts = null;
		
////	// 金額表示
////	lHasMoney = new JLabel();
////	lHasMoney.setBorder(new LineBorder(Color.black, 2, true));
////	lHasMoney.setFont(new Font("PixelMplus10",Font.PLAIN,13));
////	lHasMoney.setForeground(Color.BLACK);
////	lHasMoney.setHorizontalAlignment(JLabel.LEFT);
////	lHasMoney.setVerticalAlignment(JLabel.CENTER);
////	lHasMoney.setBounds(310, 10, 280, 150);
////	this.add(lHasMoney);
////
////	// アイテム購入予定個数、合計金額表示
////	lCounter = new JLabel();
////	lCounter.setBorder(new LineBorder(Color.black, 2, true));
////	lCounter.setFont(new Font("PixelMplus10",Font.PLAIN,13));
////	lCounter.setForeground(Color.BLACK);
////	lCounter.setHorizontalAlignment(JLabel.LEFT);
////	lCounter.setVerticalAlignment(JLabel.CENTER);
////	lCounter.setBounds(310, 260, 280, 150);
////	this.add(lCounter);
	}
	
	/**
	 * パネルの可視化
	 */
	public void showPanel(ArrayList<HashMap<String, String>> productsInfo) {
		mProducts = productsInfo;

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

		mNumOfItems = mProducts.size();  // どうぐの種類数 -> キズぐすり、まひなおし といった単位で
		btnItems = new JButton[mNumOfItems];
		ImageIcon[] iconItems = new ImageIcon[mNumOfItems];

		for (int i=0; i<mNumOfItems; i++) {
			if (i == 6) {
				// panelサイズ変更
				pItemList.setPreferredSize(new Dimension(280, 450 + 75*(mNumOfItems - i)));
				revalidate();
			}

			iconItems[i] = new ImageIcon(FilePath.itemsDirPath + mProducts.get(i).get("img"));
			
			btnItems[i] = new JButton(mProducts.get(i).get("name") + "　　(" + mProducts.get(i).get("price") + "円)", iconItems[i]);
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
	
	/**
	 * アイテム選択時の処理
	 * @param selectedItemIndex 選択されたアイテムのインデックス
	 */
	@Override
	public void actionItemSelected(int selectedItemIndex) {
		ItemDetailPanel idp;

		idp = (ItemDetailPanel )mController.getPanelInstance(PanelController.ITEM_DETAIL);
		idp.setMode(ItemDetailPanel.MODE_PRODUCT);
		idp.setBuyOrSell(ItemDetailPanel.PRODUCT_BUY);
		
		mController.showItemDetailPanel(mProducts.get(mSelectedItem));
		idp.loop();
		
		idp.setMode(ItemDetailPanel.MODE_DEFAULT);
		this.setVisible(true);
		this.requestFocus(true);
	}
}
