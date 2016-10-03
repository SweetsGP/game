package gamesystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import itemhandler.ItemHandlerDriver;
import itemhandler.ItemHandlingAreaPanel;
import itemhandler.ItemHandlingFailedException;
import main.DataBase;
import main.FilePath;
import main.Main;
import main.PanelController;

public class ItemDetailPanel extends JPanel implements KeyListener {
	public static final int RUNNING_IDP_LOOP = 0;
	public static final int EXIT_IDP_LOOP = 1;
	
	// モード
	public static final int MODE_DEFAULT = 0;
	public static final int MODE_PRODUCT = 1;

	public static final int PRODUCT_BUY = 0;
	public static final int PRODUCT_SELL = 1;
	
	private static final int ITEM_NO_ACTION = -1;
	
	// MODE_DEFAULT
	private static final int ITEM_THROW = 0;
	private static final int ITEM_USE = 1;
	
	// MODE_PRODUCT
	private static final int ITEM_MINUS = 0;
	private static final int ITEM_PLUS = 1;
	private static final int ITEM_BUY = 2;
	
	private static final int MAX_PRODUCTS_CAN_BUY = 99;
	
	public static int exitIDPLoopFlag = RUNNING_IDP_LOOP;
	
	private static int mode = MODE_DEFAULT;
//	private static int mode = MODE_PRODUCT;
	private static int productMode = PRODUCT_BUY;
	
	private int selectState = ITEM_NO_ACTION;
	
	protected PanelController controller;

	protected JLabel lItemName, lItemImg, lDescription;
	protected String itemId, itemName;
	// MODE_DEFAULT
	protected JButton btnBack, btnThrow, btnUse;
	protected JButton[] btnActions;
	protected ItemHandlingAreaPanel ihap;
	
	protected String itemHandlerName;
	
	// MODE_PRODUCT
	protected JLabel lHasMoney, lCount;
	protected JButton btnPlus, btnMinus, btnBuyOrSell;
	protected JButton[] btnActionsOnProductMode;
	protected ItemHandlingAreaPanel ihapProduct;
	
	protected int hasMoney = 1000;  // test code
	protected int productCount = 0;
	protected int price = 0;
	
	protected int hasCount = 0; 
	
	private static int key_state = 0;

	public ItemDetailPanel(PanelController pc) {
		controller = pc;

		this.setLayout(null);
		this.setSize(PanelController.PANEL_WIDTH, PanelController.PANEL_HEIGHT);

		this.setFocusable(true);
		this.addKeyListener(this);

		lItemName = new JLabel();
		lItemName.setBorder(new LineBorder(Color.cyan, 5, true));
		lItemName.setBackground(Color.white);
		lItemName.setFont(new Font("PixelMplus10", Font.PLAIN, 28));
		lItemName.setBounds(10, 10, 380, 90);
		this.add(lItemName);

		lItemImg = new JLabel();
		lItemImg.setBorder(new LineBorder(Color.cyan, 5, true));
		lItemImg.setBackground(Color.white);
		lItemImg.setBounds(10, 100, 380, 190);
		this.add(lItemImg);
		
		lDescription = new JLabel();
		lDescription.setBorder(new LineBorder(Color.cyan, 5, true));
		lDescription.setBackground(Color.white);
		lDescription.setFont(new Font("PixelMplus10", Font.PLAIN, 28));
		lDescription.setBounds(10, 300, 380, 180);
		this.add(lDescription);

		btnBack = new JButton("もどる");
		btnBack.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnBack.setBounds(10, 500, 120, 60);
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				exitIDPLoopFlag = EXIT_IDP_LOOP;
				requestFocus(false);
				setVisible(false);
				if (PanelController.getBeforeCalledLoopNum() == PanelController.MAP_PANEL) {
					controller.showMapPanel();
				} else {
					controller.showBattlemainPanel();
				}
			}
		});
		this.add(btnBack);
		
		// MODE_DEFAULTインタフェース
		btnActions = new JButton[2];
		
		btnThrow = new JButton("すてる");
		btnThrow.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnThrow.setBorderPainted(false);
		btnThrow.setBounds(140, 500, 120, 60);
		btnThrow.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				onClickBtnThrow();
			}
		});
		this.add(btnThrow);
		btnActions[0] = btnThrow;
		
		btnUse = new JButton("つかう");
		btnUse.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnUse.setBorderPainted(false);
		btnUse.setBounds(270, 500, 120, 60);
		btnUse.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				onClickBtnUse();
			}
		});
		this.add(btnUse);
		btnActions[1] = btnUse;
		
		ihap = new ItemHandlingAreaPanel(controller);
		ihap.setLocation(400, 0);
		this.add(ihap);
		
		// MODE_PRODUCTインタフェース
		btnActionsOnProductMode = new JButton[3];
		
		ihapProduct = new ItemHandlingAreaPanel(controller);
		ihapProduct.setLocation(400, 0);
		this.add(ihapProduct);
		
		lHasMoney = new JLabel("所持金：1000");
		lHasMoney.setBorder(new LineBorder(Color.cyan, 5, true));
		lHasMoney.setBackground(Color.white);
		lHasMoney.setFont(new Font("PixelMplus10", Font.PLAIN, 28));
		lHasMoney.setBounds(10, 155, 380, 90);
		ihapProduct.add(lHasMoney);

		btnMinus = new JButton("-");
		btnMinus.setFont(new Font("PixelMplus10",Font.PLAIN,28));
		btnMinus.setBorderPainted(false);
		btnMinus.setBounds(10, 255, 120, 90);
		btnMinus.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				selectState = ITEM_MINUS;
			}
		});
		ihapProduct.add(btnMinus);
		btnActionsOnProductMode[0] = btnMinus;
		
		lCount = new JLabel("0");
		lCount.setBorder(new LineBorder(Color.cyan, 5, true));
		lCount.setBackground(Color.white);
		lCount.setFont(new Font("PixelMplus10", Font.PLAIN, 28));
		lCount.setBounds(130, 255, 120, 90);
		ihapProduct.add(lCount);

		btnPlus = new JButton("+");
		btnPlus.setFont(new Font("PixelMplus10",Font.PLAIN,28));
		btnPlus.setBorderPainted(false);
		btnPlus.setBounds(260, 255, 120, 90);
		btnPlus.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				selectState = ITEM_PLUS;
			}
		});
		ihapProduct.add(btnPlus);
		btnActionsOnProductMode[1] = btnPlus;
		
		btnBuyOrSell = new JButton("かう");
		btnBuyOrSell.setFont(new Font("PixelMplus10",Font.PLAIN,28));
		btnBuyOrSell.setBorderPainted(false);
		btnBuyOrSell.setBounds(140, 355, 120, 90);
		btnBuyOrSell.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				selectState = ITEM_BUY;
			}
		});
		ihapProduct.add(btnBuyOrSell);
		btnActionsOnProductMode[2] = btnBuyOrSell;

		this.setVisible(false);
	}
	
	/**
	 * このパネルのインタフェース切り替え用にモードを設定します
	 * MODE_DEFAULT -> 所持アイテム使用、破棄処理用
	 * MODE_PRODUCT -> アイテム購入処理用
	 * @param m モード
	 */
	public void setMode(int m) {
		switch (m) {
		case MODE_DEFAULT:
//			ihapProduct.setVisible(false);
			mode = m;
			break;
		case MODE_PRODUCT:
//			ihapProduct.setVisible(true);
			mode = m;
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @param b
	 */
	public void setBuyOrSell(int b) {
//		if (mode == MODE_PRODUCT) {
			switch (b) {
			case PRODUCT_BUY:
				productMode = b;
				btnBuyOrSell.setText("かう");
				break;
				
			case PRODUCT_SELL:
				productMode = b;
				btnBuyOrSell.setText("うる");
				break;
				
			default:
				break;
			}
//		}
	}
	
	/**
	 * 1つのアイテムに関する詳細情報を表示 (各コンポーネントに情報をセット)
	 * @param itemInfo
	 */
	public void showDetailOfItem(HashMap<String, String> itemInfo) {
		itemId = itemInfo.get("itemId");
		// 名前
		itemName = itemInfo.get("name");
		lItemName.setText(itemName);
		// 画像
		ImageIcon iconItem = new ImageIcon(FilePath.itemsDirPath + itemInfo.get("img"));
		lItemImg.setIcon(iconItem);
		// 説明
		lDescription.setText(itemInfo.get("description"));
		
		switch(mode) {
		case MODE_DEFAULT:
			btnThrow.setVisible(true);
			btnUse.setVisible(true);
			
			ihap.setVisible(true);
			ihapProduct.setVisible(false);
			
			// アイテムハンドラの番号をセット
			itemHandlerName = itemInfo.get("handler");
			break;
			
		case MODE_PRODUCT:
			btnThrow.setVisible(false);
			btnUse.setVisible(false);
			
			ihap.setVisible(false);
			ihapProduct.setVisible(true);
			
			// 価格を取得
			price = Integer.parseInt(itemInfo.get("price"));
			hasCount = Integer.parseInt(itemInfo.get("count"));
			
			break;
			
		default:
			break;
		}
		
		this.setVisible(true);
		this.requestFocus(true);
	}
	
	/**
	 * 「つかう」ボタンの処理
	 * @param e
	 */
	public void onClickBtnUse() {
		selectState = ITEM_USE;
	}
	
	/**
	 * 「すてる」ボタンの処理
	 * @param e
	 */
	public void onClickBtnThrow() {
		selectState = ITEM_THROW;
	}
	
	/**
	 * 入力待機用ループ
	 */
	public void loop() {
		int cursorLocation = ITEM_NO_ACTION;
		exitIDPLoopFlag = RUNNING_IDP_LOOP;
		productCount = 0;
		
		while(true) {
			if (Main.exitFlag != Main.RUNNING || exitIDPLoopFlag != RUNNING_IDP_LOOP) {
				break;
			}
			
			// 各アイテム項目処理
			if (selectState != ITEM_NO_ACTION) {
				switch(mode) {
				case MODE_DEFAULT:
					operationOnDefaultMode();
					break;

				case MODE_PRODUCT:
					operationOnProductMode();
					break;

				default:
					break;
				}
			}
			selectState = ITEM_NO_ACTION;
			
			// キー入力受付
			switch (key_state) {
			case 1:  // UP
				// no operation
				break;
			case 2:  // DOWN
				// no operation
				break;
			case 3:  // LEFT
				if (cursorLocation > 0) {
					cursorLocation--;
				}
				break;
			case 4:  // RIRHT
				switch(mode) {
				case MODE_DEFAULT:
					if (cursorLocation < ITEM_USE) {
						cursorLocation++;
					}
					break;
					
				case MODE_PRODUCT:
					if (cursorLocation < ITEM_BUY) {
						cursorLocation++;
					}
					break;
					
				default:
					break;
				}
				break;
			case 5:  // Z
				if (cursorLocation != ITEM_NO_ACTION) {
					switch(mode) {
					case MODE_DEFAULT:
						btnActions[cursorLocation].doClick();
						break;
						
					case MODE_PRODUCT:
						btnActionsOnProductMode[cursorLocation].doClick();
						break;
						
					default:
						break;
					}
				}
				break;
			case 6:  // X
				// no operation
				break;
			case 7:  // スペースキー
				btnBack.doClick();
				break;
			default:
				break;
			}
			key_state = 0;
			
			// カーソル表示 (暫定的に青枠表示)
			if (cursorLocation != ITEM_NO_ACTION) {
//				setCursor(cursorLocation);
				switch(mode) {
				case MODE_DEFAULT:
					for (int i = 0; i < ITEM_USE + 1; i++) {
						if (i == cursorLocation) {
							btnActions[i].setBorderPainted(true);
							btnActions[i].setBorder(new LineBorder(Color.BLUE, 2, true));
						} else {
							btnActions[i].setBorderPainted(false);
						}
					}
					this.repaint();
					break;
					
				case MODE_PRODUCT:
					for (int i = 0; i < ITEM_BUY + 1; i++) {
						if (i == cursorLocation) {
							btnActionsOnProductMode[i].setBorderPainted(true);
							btnActionsOnProductMode[i].setBorder(new LineBorder(Color.BLUE, 2, true));
						} else {
							btnActionsOnProductMode[i].setBorderPainted(false);
						}
					}
					ihapProduct.repaint();
					break;
					
				default:
					break;
				}
			}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * デフォルトメニューにおける各項目の処理
	 */
	public void operationOnDefaultMode() {
		btnBack.setEnabled(false);
		btnThrow.setEnabled(false);
		btnUse.setEnabled(false);
		ihap.clear();

		switch (selectState) {
		case ITEM_USE:
			try {
				ItemHandlerDriver itemHandler = new ItemHandlerDriver(itemHandlerName, ihap);
				itemHandler.use();
			} catch (ItemHandlingFailedException ihfe) {
				ihfe.printStackTrace();
				JOptionPane.showMessageDialog(null, "アイテムの処理に失敗しました。",
						Main.GAME_TITLE, JOptionPane.ERROR_MESSAGE);
				Main.exitFlag = Main.EXIT_ERROR;
			}
			break;

		case ITEM_THROW:
			try {
				ItemHandlerDriver itemHandler = new ItemHandlerDriver(itemHandlerName, ihap);
				itemHandler.throwAway();
			} catch (ItemHandlingFailedException ihfe) {
				ihfe.printStackTrace();
				JOptionPane.showMessageDialog(null, "アイテムの処理に失敗しました。",
						Main.GAME_TITLE, JOptionPane.ERROR_MESSAGE);
				Main.exitFlag = Main.EXIT_ERROR;
			}
			break;

		default:
			break;
		}

		ihap.clear();
		this.requestFocus(true);
		btnBack.setEnabled(true);
		btnThrow.setEnabled(true);
		btnUse.setEnabled(true);
	}
	
	/**
	 * 商品詳細メニューにおける各項目の処理
	 */
	public void operationOnProductMode() {
		int totalPrice = 0;
//		ihapProduct.clear();

		switch (selectState) {
		case ITEM_MINUS:
			if (productCount > 0) {
				productCount--;
				lCount.setText(Integer.toString(productCount));
			}
			ihapProduct.repaint();
			break;

		case ITEM_PLUS:
			switch (productMode) {
			case PRODUCT_BUY:
				if (productCount < MAX_PRODUCTS_CAN_BUY) {
					productCount++;
					lCount.setText(Integer.toString(productCount));
				}
				break;
				
			case PRODUCT_SELL:
				if (productCount < hasCount) {
					productCount++;
					lCount.setText(Integer.toString(productCount));
				}
				break;
				
			default:
				break;
			}
			ihapProduct.repaint();
			break;
			
		case ITEM_BUY:
			switch (productMode) {
			case PRODUCT_BUY:
				totalPrice = price * productCount;
				if (hasMoney >= totalPrice) {
					try {
						DataBase.wExecuteUpdate("update testitems set count = count + " + Integer.toString(productCount)
						+ " where itemId = " + itemId);
						// TODO:所持金減算処理
					} catch(SQLException sqle) {
						sqle.printStackTrace();
					}
				}
				break;
				
			case PRODUCT_SELL:
				try {
					DataBase.wExecuteUpdate("update testitems set count = count - " + Integer.toString(productCount)
					+ " where itemId = " + itemId);
					// TODO:所持金減算処理
				} catch(SQLException sqle) {
					sqle.printStackTrace();
				}
				hasCount = hasCount - productCount;
				break;
				
			default:
				break;
			}
			break;

		default:
			break;
		}

//		ihapProduct.clear();
//		this.requestFocus(true);
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
