package map;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import gamesystem.HasItemsPanel;
import gamesystem.HasMonstersPanel;
import gamesystem.ItemsPanel;
import gamesystem.SettingsPanel;
import gameutil.SaveDataAccess;
import main.Env;
import main.Main;
import main.PanelController;

public class MenuPanel extends ItemsPanel {
	// 暫定
	public static final int RUNNING_MENU_LOOP = 0;
	public static final int EXIT_MENU_LOOP = 1;
	
	public static int exitMenuLoopFlag = RUNNING_MENU_LOOP;
	// =====
	
	public static final int MENU_HAS_MONSTERS = 0;
	public static final int MENU_ITEM = 1;
	public static final int MENU_MONSTERS_GUIDE = 2;
	public static final int MENU_SETTINGS = 3;
	public static final int MENU_WRITE_REPORT = 4;
	public static final int MENU_TO_TITLE = 5;
	
	SaveDataAccess sda;
	
	HasMonstersPanel hasMonstersPanel;
	HasItemsPanel hasItemsPanel;
	JButton btnHasMonsters, btnItem, btnMonstersGuide, btnSettings, btnWriteReport, btnToTitle;
	
	public MenuPanel(PanelController pc, HasMonstersPanel hmp, HasItemsPanel hip) {
		super(pc);
		
		hasMonstersPanel = hmp;
		hasItemsPanel = hip;
		
		sda = new SaveDataAccess();
		
		btnHasMonsters = new JButton("てもち");
		btnHasMonsters.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnHasMonsters.setBorderPainted(false);
		btnHasMonsters.setBounds(10, 20, 260, 50);
		btnHasMonsters.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mSelectedItem = MENU_HAS_MONSTERS;
//				setCursor(MENU_ITEM_A);
			}
		});
		pItemList.add(btnHasMonsters);
		addToCursorArr(btnHasMonsters);
		
		btnItem = new JButton("アイテム");
		btnItem.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnItem.setBorderPainted(false);
		btnItem.setBounds(10, 90, 260, 50);
		btnItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mSelectedItem = MENU_ITEM;
//				setCursor(MENU_ITEM_B);
			}
		});
		pItemList.add(btnItem);
		addToCursorArr(btnItem);
		
		btnMonstersGuide = new JButton("ずかん");
		btnMonstersGuide.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnMonstersGuide.setBorderPainted(false);
		btnMonstersGuide.setBounds(10, 160, 260, 50);
		btnMonstersGuide.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mSelectedItem = MENU_MONSTERS_GUIDE;
//				setCursor(MENU_ITEM_C);
			}
		});
		pItemList.add(btnMonstersGuide);
		addToCursorArr(btnMonstersGuide);
		
		btnSettings = new JButton("せってい");
		btnSettings.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnSettings.setBorderPainted(false);
		btnSettings.setBounds(10, 230, 260, 50);
		btnSettings.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mSelectedItem = MENU_SETTINGS;
//				setCursor(MENU_ITEM_D);
			}
		});
		pItemList.add(btnSettings);
		addToCursorArr(btnSettings);
		
		btnWriteReport = new JButton("レポートを書く");
		btnWriteReport.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnWriteReport.setBorderPainted(false);
		btnWriteReport.setBounds(10, 300, 260, 50);
		btnWriteReport.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mSelectedItem = MENU_WRITE_REPORT;
//				setCursor(MENU_ITEM_E);
//				menuEvent.writeReport();
			}
		});
		pItemList.add(btnWriteReport);
		addToCursorArr(btnWriteReport);
		
		btnToTitle = new JButton("タイトルへ");
		btnToTitle.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnToTitle.setBorderPainted(false);
		btnToTitle.setBounds(10, 370, 260, 50);
		btnToTitle.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mSelectedItem = MENU_TO_TITLE;
//				setCursor(MENU_ITEM_F);
			}
		});
		pItemList.add(btnToTitle);
		addToCursorArr(btnToTitle);
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
		case MENU_HAS_MONSTERS:  // てもち
			this.requestFocus(false);
//			setEnabledInner(false);
			this.setVisible(false);
//			hasMonstersPanel.showHasMonsters();
			hasMonstersPanel.showPanel();
			hasMonstersPanel.loop();
			this.setVisible(true);
//			setEnabledInner(true);
			this.requestFocus(true);
			break;
			
		case MENU_ITEM:  // アイテム
			setEnabledInner(false);
//			hasItemsPanel.showHasItems();
			hasItemsPanel.showPanel();
			hasItemsPanel.loop();
			setEnabledInner(true);
			this.requestFocus(true);
			break;
			
		case MENU_MONSTERS_GUIDE:  // ずかん
			// TODO: ずかん処理
			break;

		case MENU_SETTINGS:  // せってい
			SettingsPanel sp;
			
			this.requestFocus(false);
			mController.showSettingsPanel();
//			sp = (SettingsPanel )mController.getPanelInstance(PanelController.SETTINGS_PANEL);
//			sp.loop();
			PanelController.callLoop(PanelController.SETTINGS_PANEL);
			this.requestFocus(true);
			break;
			
		case MENU_WRITE_REPORT:  // レポートを書く
			this.requestFocus(false);
			sda.writeReport();
			this.requestFocus(true);
			break;
			
		case MENU_TO_TITLE:  // タイトルへ
			int goToTitleConfirmAns = JOptionPane.showConfirmDialog(null,
					"タイトルに戻りますか？"+ Env.crlf +"(保存していないセーブデータは失われます)",
					Main.GAME_TITLE, JOptionPane.YES_NO_OPTION);
			if (goToTitleConfirmAns == JOptionPane.YES_OPTION) {
//				exitMenuLoopFlag = EXIT_MENU_LOOP;
//				deleteCursor();
				stop();
				setVisible(false);
//				mapMain.button1.setVisible(true);
				map_main.exitMapLoopFlag = map_main.EXIT_MAP_LOOP;
				mController.showTitlePanel();
				PanelController.reserveCallLoop(PanelController.TITLE_PANEL);
			}
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
		btnHasMonsters.setEnabled(b);
		btnItem.setEnabled(b);
		btnMonstersGuide.setEnabled(b);
		btnSettings.setEnabled(b);
		btnWriteReport.setEnabled(b);
		btnToTitle.setEnabled(b);
	}
	
}
