package map;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Battle.Battlemain;
import gamesystem.HasItemsPanel;
import gamesystem.HasMonstersPanel;
import gamesystem.MenuEvent;
import gamesystem.ProductsListPanel;
import gamesystem.SettingsPanel;
import main.Env;
import main.Main;
import main.PanelController;

public class MenuPanel extends JPanel implements KeyListener {
	// ループ終了フラグ定数
	public static final int RUNNING_MENU_LOOP = 0;
	public static final int EXIT_MENU_LOOP = 1;
	// メニュー項目定数
	private static final int MENU_NO_SELECTED = 0;
	private static final int MENU_ITEM_A = 1;
	private static final int MENU_ITEM_B = 2;
	private static final int MENU_ITEM_C = 3;
	private static final int MENU_ITEM_D = 4;
	private static final int MENU_ITEM_E = 5;
	private static final int MENU_ITEM_F = 6;
	// メニュー種別
	public static final int DEFAULT_MENU = 0;
	public static final int MERCHANT_MENU = 1;
	
	// ループ終了フラグ
	public static int exitMenuLoopFlag = RUNNING_MENU_LOOP;
	// メニュー種別
	private static int menuState = DEFAULT_MENU;
//	private static int menuState = MERCHANT_MENU;
	// キー操作におけるカーソル位置
	private int cursorLocation = MENU_NO_SELECTED;
	// カーソル移動範囲上限
	private int cursorUpperLimit = MENU_ITEM_A;
	// カーソル移動範囲下限
	private int cursorLowerLimit = MENU_ITEM_F;
//	private int cursorLowerLimit = MENU_ITEM_C;
	// メニュー項目選択状態
	private int selectState = MENU_NO_SELECTED;
	
	private PanelController controller;
	private MenuEvent menuEvent;
	
	JButton btnA, btnB, btnC, btnD, btnE, btnF;
	JButton[] btnMenuItems;
	
	// メニュー項目
	String[] defaultMenu = {"", "てもち", "アイテム", "ずかん", "せってい", "レポートを書く", "タイトルへ"};
	String[] merchantMenu = {"", "かう", "うる", "やめる"};
	
	ArrayList<HashMap<String, String>> productsInfo;
	
	private static int key_state = 0;
	
	HasMonstersPanel hasMonstersPanel;
	HasItemsPanel hasItemsPanel;
	
	ProductsListPanel productsListPanel;
	
	public MenuPanel(PanelController c, HasMonstersPanel hmp, HasItemsPanel hip, ProductsListPanel plp) {
		controller = c;
		hasMonstersPanel = hmp;
		hasItemsPanel = hip;
		productsListPanel = plp;
		
		this.setLayout(null);
		this.setSize(300, 450);
		this.setBackground(Color.white);
		this.setBorder(new LineBorder(Color.black, 2, true));
		
		menuEvent = new MenuEvent();
		
		// メニューパネル
		this.setFocusable(true);
		this.addKeyListener(this);
		
		btnMenuItems = new JButton[6];
		
//		btnHasMonsters = new JButton("てもち");
		btnA = new JButton();
		btnA.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnA.setBorderPainted(false);
		btnA.setBounds(10, 20, 280, 50);
		btnA.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				selectState = MENU_ITEM_A;
				setCursor(MENU_ITEM_A);
			}
		});
		this.add(btnA);
		btnMenuItems[0] = btnA;
		
//		btnItem = new JButton("アイテム");
		btnB = new JButton();
		btnB.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnB.setBorderPainted(false);
		btnB.setBounds(10, 90, 280, 50);
		btnB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				selectState = MENU_ITEM_B;
				setCursor(MENU_ITEM_B);
			}
		});
		this.add(btnB);
		btnMenuItems[1] = btnB;
		
//		btnMonstersGuide = new JButton("ずかん");
		btnC = new JButton();
		btnC.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnC.setBorderPainted(false);
		btnC.setBounds(10, 160, 280, 50);
		btnC.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				selectState = MENU_ITEM_C;
				setCursor(MENU_ITEM_C);
			}
		});
		this.add(btnC);
		btnMenuItems[2] = btnC;
		
//		btnSettings = new JButton("せってい");
		btnD = new JButton();
		btnD.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnD.setBorderPainted(false);
		btnD.setBounds(10, 230, 280, 50);
		btnD.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				selectState = MENU_ITEM_D;
				setCursor(MENU_ITEM_D);
			}
		});
		this.add(btnD);
		btnMenuItems[3] = btnD;
		
//		btnWriteReport = new JButton("レポートを書く");
		btnE = new JButton();
		btnE.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnE.setBorderPainted(false);
		btnE.setBounds(10, 300, 280, 50);
		btnE.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				selectState = MENU_ITEM_E;
				setCursor(MENU_ITEM_E);
//				menuEvent.writeReport();
			}
		});
		this.add(btnE);
		btnMenuItems[4] = btnE;
		
//		btnToTitle = new JButton("タイトルへ");
		btnF = new JButton();
		btnF.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnF.setBorderPainted(false);
		btnF.setBounds(10, 370, 280, 50);
		btnF.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				selectState = MENU_ITEM_F;
				setCursor(MENU_ITEM_F);
			}
		});
		this.add(btnF);
		btnMenuItems[5] = btnF;
		
		this.setVisible(false);
	}
	
	/**
	 * メニュー表示
	 */
	public void showMenu() {
		setMenuView();
		this.setVisible(true);
		this.requestFocus(true);

		key_state = 0;
		cursorLocation = MENU_NO_SELECTED;
		selectState = MENU_NO_SELECTED;
		
		exitMenuLoopFlag = RUNNING_MENU_LOOP;

		// 入力待機ループ
		while(true) {
			// ループ終了フラグチェック
			if (Main.exitFlag != Main.RUNNING || exitMenuLoopFlag != RUNNING_MENU_LOOP) {
				break;
			}
			
			// 各メニュー項目の処理
			switch (menuState) {
			case DEFAULT_MENU:
				actionOfDefaultMenu();
				break;
				
			case MERCHANT_MENU:
				actionOfMerchantMenu();
				break;
				
			default:
				break;
			}
			
			// キー入力受付
			switch (key_state) {
			case 1:  // UP
				if (cursorLocation > cursorUpperLimit) {
					cursorLocation--;
				}
				break;
			case 2:  // DOWN
				if (cursorLocation < cursorLowerLimit) {
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
				if (cursorLocation != MENU_NO_SELECTED) {
					btnMenuItems[cursorLocation-1].doClick();
				}
				break;
			case 6:  // X
				deleteCursor();
				this.requestFocus(false);
				this.setVisible(false);
				exitMenuLoopFlag = EXIT_MENU_LOOP;
				break;
			case 7:  // スペースキー
				deleteCursor();
				this.requestFocus(false);
				this.setVisible(false);
				exitMenuLoopFlag = EXIT_MENU_LOOP;
				break;
			default:
				break;
			}
			key_state = 0;
			
			// カーソル表示 (暫定的に青枠表示)
			if (cursorLocation != MENU_NO_SELECTED) {
				setCursor(cursorLocation);
			}

			/* 待機時間を少し設ける */
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}
	
	// ==========
	// メニュー設定
	// ==========
	/**
	 * メニュー種別を設定
	 * @param menuType
	 */
	public void setMenuType(int menuType) {
		switch (menuType) {
		case DEFAULT_MENU:
			menuState = menuType;
			cursorUpperLimit = MENU_ITEM_A;
			cursorLowerLimit = MENU_ITEM_F;
			break;
			
		case MERCHANT_MENU:
			menuState = menuType;
			cursorUpperLimit = MENU_ITEM_A;
			cursorLowerLimit = MENU_ITEM_C;
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * menuStateに応じて、メニュー項目を設定する
	 */
	private void setMenuView() {
		switch (menuState) {
		case DEFAULT_MENU:
			setDefaultMenu();
			break;
			
		case MERCHANT_MENU:
			setMerchantMenu();
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * デフォルトのメニューを設定
	 */
	private void setDefaultMenu() {
		for (int i=0; i<MENU_ITEM_F; i++) {
			btnMenuItems[i].setEnabled(true);
			btnMenuItems[i].setText(defaultMenu[i+1]);
		}
	}
	
	/**
	 * 商人用のメニューを設定
	 */
	private void setMerchantMenu() {
		for (int i=0; i<MENU_ITEM_F; i++) {
			if (i < MENU_ITEM_C) {
				btnMenuItems[i].setEnabled(true);
				btnMenuItems[i].setText(merchantMenu[i+1]);
			} else {
				btnMenuItems[i].setEnabled(false);
				btnMenuItems[i].setText("");
			}
		}
	}
	
	/**
	 * 商人が扱う商品情報を設定します
	 * @param p
	 */
	public void setMerchantProducts(ArrayList<HashMap<String, String>> p) {
		productsInfo = p;
	}
	
	// ==========
	// メニュー項目処理設定
	// ==========
	/**
	 * デフォルトの各メニュー項目の処理
	 */
	private void actionOfDefaultMenu() {
		
		switch(selectState) {
		case MENU_ITEM_A:  // てもち
			this.requestFocus(false);
//			setEnabledInner(false);
			this.setVisible(false);
			hasMonstersPanel.showHasMonsters(menuEvent.getHasMonstersInfo());
			hasMonstersPanel.loop();
			this.setVisible(true);
//			setEnabledInner(true);
			this.requestFocus(true);
			break;
			
		case MENU_ITEM_B:  // アイテム
			setEnabledInner(false);
			hasItemsPanel.showHasItems();
			hasItemsPanel.loop();
			setEnabledInner(true);
			this.requestFocus(true);
			break;
			
		case MENU_ITEM_C:  // ずかん
			// TODO: ずかん処理
			break;

		case MENU_ITEM_D:  // せってい
			SettingsPanel sp;
			
			System.out.println("in");
			controller.showSettingsPanel();
			sp = (SettingsPanel )controller.getPanelInstance(PanelController.SETTINGS);
			sp.loop();
			System.out.println("out");
			break;
			
		case MENU_ITEM_E:  // レポートを書く
			menuEvent.writeReport();
			break;
			
		case MENU_ITEM_F:  // タイトルへ
			int goToTitleConfirmAns = JOptionPane.showConfirmDialog(null,
					"タイトルに戻りますか？"+ Env.crlf +"(保存していないセーブデータは失われます)",
					Main.GAME_TITLE, JOptionPane.YES_NO_OPTION);
			if (goToTitleConfirmAns == JOptionPane.YES_OPTION) {
				exitMenuLoopFlag = EXIT_MENU_LOOP;
				deleteCursor();
				setVisible(false);
//				mapMain.button1.setVisible(true);
				map_main.exitMapLoopFlag = map_main.EXIT_MAP_LOOP;
				controller.showTitlePanel();
			}
			break;
		default:
			break;
		}
		selectState = MENU_NO_SELECTED;
	}
	
	/**
	 * 商人用のメニューの各項目の処理
	 */
	private void actionOfMerchantMenu() {
		switch(selectState) {
		case MENU_ITEM_A:  // かう
			setEnabledInner(false);
			productsListPanel.showProducts(productsInfo);
			productsListPanel.loop();
			setEnabledInner(true);
			this.requestFocus(true);
			break;
			
		case MENU_ITEM_B:  // うる
			setEnabledInner(false);
			hasItemsPanel.showHasItems();
			hasItemsPanel.setMode(HasItemsPanel.MODE_SELL);
			hasItemsPanel.loop();
			hasItemsPanel.setMode(HasItemsPanel.MODE_DEFAULT);
			setEnabledInner(true);
			this.requestFocus(true);
			break;
			
		case MENU_ITEM_C:  // やめる
			key_state = 7;
			break;
			
		default:
			break;
		}
		selectState = MENU_NO_SELECTED;
	}
	
	// ==========
	// カーソル操作
	// ==========
	/**
	 * 与えられた番号の位置にカーソル表示をセットする
	 * @param cursor カーソル表示を設置したい項目の位置番号
	 */
	private void setCursor(int cursor) {
		for (int i = 0; i < MENU_ITEM_F; i++) {
			if (i == cursor - 1) {
				btnMenuItems[i].setBorderPainted(true);
				btnMenuItems[i].setBorder(new LineBorder(Color.BLUE, 2, true));
			} else {
				btnMenuItems[i].setBorderPainted(false);
			}
		}
		this.repaint();
	}
	
	/**
	 * カーソル表示を削除し、項目未選択の状態にします
	 */
	private void deleteCursor() {
		if (cursorLocation != MENU_NO_SELECTED) {
			btnMenuItems[cursorLocation - 1].setBorderPainted(false);
		}
		cursorLocation = MENU_NO_SELECTED;
	}
	
	// ==========
	// その他
	// ==========
	/**
	 * 各メニュー項目のボタンの有効化/無効化
	 * @param b true -> 有効化, false -> 無効化
	 */
	public void setEnabledInner(boolean b) {
		for (int i=0; i<MENU_ITEM_F; i++) {
			btnMenuItems[i].setEnabled(b);
		}
	}

	// ==========
	// キー操作
	// ==========
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
