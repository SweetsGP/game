package gamesystem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import Battle.Battlemain;
import main.DataBase;
import main.FilePath;
import main.Main;
import main.PanelController;

public class ProductsListPanel extends HasItemsPanel {
	
//	JLabel lHasMoney, lCounter;
	
	// test code
//	private int hasMoney = 1000;
	
	public ProductsListPanel(PanelController c) {
		super(c);
		
//		// 金額表示
//		lHasMoney = new JLabel();
//		lHasMoney.setBorder(new LineBorder(Color.black, 2, true));
//		lHasMoney.setFont(new Font("PixelMplus10",Font.PLAIN,13));
//		lHasMoney.setForeground(Color.BLACK);
//		lHasMoney.setHorizontalAlignment(JLabel.LEFT);
//		lHasMoney.setVerticalAlignment(JLabel.CENTER);
//		lHasMoney.setBounds(310, 10, 280, 150);
//		this.add(lHasMoney);
//
//		// アイテム購入予定個数、合計金額表示
//		lCounter = new JLabel();
//		lCounter.setBorder(new LineBorder(Color.black, 2, true));
//		lCounter.setFont(new Font("PixelMplus10",Font.PLAIN,13));
//		lCounter.setForeground(Color.BLACK);
//		lCounter.setHorizontalAlignment(JLabel.LEFT);
//		lCounter.setVerticalAlignment(JLabel.CENTER);
//		lCounter.setBounds(310, 260, 280, 150);
//		this.add(lCounter);
		
		this.setVisible(false);
	}
	
	/**
	 * 商品一覧を表示
	 * @param products 商品情報
	 */
	public void showProducts(ArrayList<HashMap<String, String>> products) {
		hasItems = products;

		// 商品リスト
		numOfItems = hasItems.size();  // どうぐの種類数 -> キズぐすり、まひなおし といった単位で
		btnItems = new JButton[numOfItems];
		ImageIcon[] iconItems = new ImageIcon[numOfItems];

		for (int i=0; i<numOfItems; i++) {
			if (i == 6) {
				// panelサイズ変更
				pItemList.setPreferredSize(new Dimension(280, 450 + 75*(numOfItems - i)));
				revalidate();
			}

			iconItems[i] = new ImageIcon(FilePath.itemsDirPath + hasItems.get(i).get("img"));

//			btnItems[i] = new JButton(hasItems.get(i).get("name") + "(" + hasItems.get(i).get("count") + ")", iconItems[i]);
			btnItems[i] = new JButton(hasItems.get(i).get("name") + "　　(" + hasItems.get(i).get("price") + "円)", iconItems[i]);
			btnItems[i].setFont(new Font("PixelMplus10",Font.PLAIN,13));
			btnItems[i].setHorizontalAlignment(JButton.LEFT);
			btnItems[i].setBorderPainted(false);
			btnItems[i].setBounds(0, 75*i, 260, 70);
			btnItems[i].addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onClickBtnItem(e);
				}
			});
			pItemList.add(btnItems[i]);
		}
		
		// 所持金額
//		lHasMoney.setText(Integer.toString(hasMoney) + "円");

		// 表示
		System.out.println("show");
		this.setVisible(true);
		scrollItemList.setVisible(true);
		this.requestFocus(true);
	}
	
	/**
	 * showProductsメソッドと同じ
	 * @param hi 
	 */
	@Deprecated
	@Override
	public void showHasItems(ArrayList<HashMap<String, String>> hi) {
		showProducts(hi);
	}
	
	/**
	 * 何もしない
	 */
	@Deprecated
	@Override
	public void showHasItems() {
		/* no operation */
	}
	
	/**
	 * アイテム選択時の処理
	 */
	@Override
	public void processBtnItem() {
		ItemDetailPanel idp;

		idp = (ItemDetailPanel )controller.getPanelInstance(PanelController.ITEM_DETAIL);
		idp.setMode(ItemDetailPanel.MODE_PRODUCT);
		idp.setBuyOrSell(ItemDetailPanel.PRODUCT_BUY);
		
		controller.showItemDetailPanel(hasItems.get(selectState - ITEM_SELECTED));
		idp.loop();
		
		idp.setMode(ItemDetailPanel.MODE_DEFAULT);
		this.setVisible(true);
		this.requestFocus(true);
	}

}
