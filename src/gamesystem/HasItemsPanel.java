package gamesystem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import main.FilePath;
import main.Main;
import main.PanelController;

public class HasItemsPanel extends JPanel implements KeyListener {
	public static final int RUNNING_HIP_LOOP = 0;
	public static final int EXIT_HIP_LOOP = 1;

	protected static final int ITEM_NO_SELECT = -1;
	protected static final int ITEM_SELECTED = 1;
	
	public static final int MODE_DEFAULT = 0;
	public static final int MODE_SELL = 1;

	public static int exitHIPLoopFlag = RUNNING_HIP_LOOP;
	
	private static int mode = MODE_DEFAULT;

	protected int selectState = ITEM_NO_SELECT;

	protected PanelController controller;
	protected ArrayList<HashMap<String, String>> hasItems = null;

	MenuEvent me;
	protected static int key_state = 0;

	JPanel pItemList;
	JScrollPane scrollItemList;
	JButton btnBack;

	JButton[] btnItems = null;
	protected int numOfItems;

	protected HasItemsPanel() {}
	public HasItemsPanel(PanelController c) {
		controller = c;
		me = new MenuEvent();

		this.setLayout(null);
		this.setSize(300, 540);
		this.setBackground(Color.white);
		this.setBorder(new LineBorder(Color.black, 2, true));

		this.setFocusable(true);
		this.addKeyListener(this);

		// アイテム一覧表示領域 サイズ可変
		pItemList = new JPanel();
		pItemList.setLayout(null);
		pItemList.setPreferredSize(new Dimension(280, 450));
		pItemList.setOpaque(false);
		scrollItemList = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollItemList.setViewportView(pItemList);
		scrollItemList.setBounds(5, 5, 290, 460);
		scrollItemList.setVisible(false);
		this.add(scrollItemList);

		// もどるボタン
		btnBack = new JButton("もどる");
		btnBack.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnBack.setBounds(150, 470, 140, 60);
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				exitHIPLoopFlag = EXIT_HIP_LOOP;
			}
		});
		this.add(btnBack);

		this.setVisible(false);
	}

	/**
	 * @deprecated 引数なしのほうをつかってください.
	 * キャラクタが保持しているどうぐ(アイテム)の一覧を表示する
	 * @param hi 所持しているアイテムの情報
	 */
	@Deprecated
	public void showHasItems(ArrayList<HashMap<String, String>> hi) {
		hasItems = hi;

		// 更新(2回目以降のアクセス)なら、以前のボタンを除去して、アイテムのリスト表示領域のサイズをリセット
		if (!Objects.equals(btnItems, null)) {
			for(int i=0; i<btnItems.length; i++) {
				if (!Objects.equals(btnItems[i], null)) {
					pItemList.remove(btnItems[i]);
				}
			}

			pItemList.setPreferredSize(new Dimension(280, 450));
		}

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

			btnItems[i] = new JButton(hasItems.get(i).get("name") + "(" + hasItems.get(i).get("count") + ")", iconItems[i]);
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

		// 表示
		this.setVisible(true);
		scrollItemList.setVisible(true);
		this.requestFocus(true);
	}

	/**
	 * キャラクタが保持しているどうぐ(アイテム)の一覧を表示する
	 */
	public void showHasItems() {
		showHasItems(me.getHasItemsInfo());
	}
	
	public void setMode(int m) {
		switch (m) {
		case MODE_DEFAULT:
			mode = m;
			break;
			
		case MODE_SELL:
			mode = m;
			break;
			
		default:
			break;
		}
	}

	/**
	 * 各アイテムボタンをクリックしたときの処理
	 * @param e
	 */
	public void onClickBtnItem(ActionEvent e) {
		for (int i = 0; i < btnItems.length; i++) {
			if (e.getSource() == btnItems[i]) {
				selectState = i + ITEM_SELECTED;
				setCursor(i);
				break;
			}
		}
	}

	/**
	 * 入力待機用ループ
	 */
	public void loop() {
		ItemDetailPanel idp;
		
		int cursorLocation = ITEM_NO_SELECT;

		key_state = 0;
		cursorLocation = ITEM_NO_SELECT;
		exitHIPLoopFlag = RUNNING_HIP_LOOP;

		while(true) {
			if (Main.exitFlag != Main.RUNNING || exitHIPLoopFlag != RUNNING_HIP_LOOP) {
				break;
			}

			// アイテム選択時の処理
			if (selectState != ITEM_NO_SELECT) {
				processBtnItem();
			}
			selectState = ITEM_NO_SELECT;

			// キー入力受付
			switch (key_state) {
			case 1:  // UP
				if (cursorLocation > 0) {
					cursorLocation--;
				}
				break;
			case 2:  // DOWN
				if (cursorLocation < numOfItems - 1) {
					cursorLocation++;
				}
				break;
			case 3:  // LEFT
				// no operation
				break;
			case 4:  // RIRHT
				// no operation
				break;
			case 5:  // Z
				if (cursorLocation != ITEM_NO_SELECT) {
					btnItems[cursorLocation].doClick();
				}
				break;
			case 6:  // X
				// no operation
				break;
			case 7:  // スペースキー
				this.requestFocus(false);
				this.setVisible(false);
				exitHIPLoopFlag = EXIT_HIP_LOOP;
				break;
			default:
				break;
			}
			key_state = 0;

			// カーソル表示 (暫定的に青枠表示)
			if (cursorLocation != ITEM_NO_SELECT) {
				setCursor(cursorLocation);
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		this.requestFocus(false);
		this.setVisible(false);

	}
	
	/**
	 * 与えられた番号の位置にカーソル表示をセットする
	 * @param cursorLocation カーソル表示を設置したい項目の位置番号
	 */
	public void setCursor(int cursorLocation) {
		for (int i = 0; i < numOfItems; i++) {
			if (i == cursorLocation) {
				btnItems[i].setBorderPainted(true);
				btnItems[i].setBorder(new LineBorder(Color.BLUE, 2, true));
			} else {
				btnItems[i].setBorderPainted(false);
			}
		}
		this.repaint();
	}
	
	/**
	 * アイテム選択時の処理
	 */
	public void processBtnItem() {
		ItemDetailPanel idp;

		idp = (ItemDetailPanel )controller.getPanelInstance(PanelController.ITEM_DETAIL);
		if (mode == MODE_SELL) {
			idp.setMode(ItemDetailPanel.MODE_PRODUCT);
			idp.setBuyOrSell(ItemDetailPanel.PRODUCT_SELL);
		}
		
		controller.showItemDetailPanel(hasItems.get(selectState - ITEM_SELECTED));
		idp.loop();
		
		if (mode == MODE_SELL) {
			idp.setMode(ItemDetailPanel.MODE_DEFAULT);
		}
		
		this.setVisible(false);
		this.showHasItems();
		//				this.requestFocus(true);
		
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int key;
		
		/* キーコードの格納 */
		key = e.getKeyCode();

		switch(key) {
		case KeyEvent.VK_UP:
			key_state = 1;
			break;
		case KeyEvent.VK_DOWN:
			key_state = 2;
			break;
		case KeyEvent.VK_LEFT:
			key_state = 3;
			break;
		case KeyEvent.VK_RIGHT:
			key_state = 4;
			break;
		case KeyEvent.VK_Z:
			key_state = 5;
			break;
		case KeyEvent.VK_X:
			key_state = 6;
			break;
		case KeyEvent.VK_SPACE:
			key_state = 7;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
