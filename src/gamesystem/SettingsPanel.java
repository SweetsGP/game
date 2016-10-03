package gamesystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Env;
import main.Init;
import main.Main;
import main.PanelController;
import main.Sound;
import map.MenuPanel;
import map.map_main;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

/**
 * 設定画面クラス
 *
 */
public class SettingsPanel extends BasePanel {
	
	private static final int NO_SELECTED = 0;
	private static final int RESET = 1;
	private static final int VOLUME_BGM = 2;
	private static final int VOLUME_SE = 3;
	
	private int mSelectedSettingItem;
	
	private Sound sound;
	
	private JLabel pSettings;
	private JLabel lVolumeBgm, lVolumeSe;
	private JButton btnReset,btnVolumeBgm, btnVolumeSe, btnBack;
	private JSlider slVolumeBgm, slVolumeSe;
	
	public SettingsPanel(PanelController pc, Sound s) {
		super(pc);
		
		sound = s;
		
		mSelectedSettingItem = NO_SELECTED;
		
		pSettings = new JLabel("せってい");
		pSettings.setBounds(100, 5, 400, 40);
		pSettings.setFont(new Font("PixelMplus10", Font.PLAIN, 30));
		this.add(pSettings);

		btnReset = new JButton("リセット");
		btnReset.setBounds(20, 50, 150, 40);
		btnReset.setBorderPainted(false);
		btnReset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				mSelectedSettingItem = RESET;
//				setCursor(RESET_SELECTED);
			}
		});
		this.add(btnReset);
		addToCursorArr(btnReset);
		
		btnVolumeBgm = new JButton("BGM音量");
		btnVolumeBgm.setBounds(20, 100, 150, 40);
		btnVolumeBgm.setBorderPainted(false);
		btnVolumeBgm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				mSelectedSettingItem = VOLUME_BGM;
//				setCursor(VOLUME_BGM_SELECTED);
			}
		});
		this.add(btnVolumeBgm);
		addToCursorArr(btnVolumeBgm);
		
		slVolumeBgm = new JSlider();
		slVolumeBgm.setValue(Main.saveData.getVolumeBgm());
		slVolumeBgm.setBounds(180, 100, 300, 40);
		slVolumeBgm.addChangeListener(new ChangeListener () {
			public void stateChanged (ChangeEvent e) {
				sound.setVolumeBgm(slVolumeBgm.getValue());
				lVolumeBgm.setText("" + slVolumeBgm.getValue());
			}
		});
		this.add(slVolumeBgm);
		
		lVolumeBgm = new JLabel(Integer.toString(Main.saveData.getVolumeBgm()));
		lVolumeBgm.setBounds(490, 100, 150, 40);
		lVolumeBgm.setFont(new Font("PixelMplus10", Font.PLAIN, 18));
		this.add(lVolumeBgm);
		
		btnVolumeSe = new JButton("SE音量");
		btnVolumeSe.setBounds(20, 150, 150, 40);
		btnVolumeSe.setBorderPainted(false);
		btnVolumeSe.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				mSelectedSettingItem = VOLUME_SE;
//				setCursor(VOLUME_SE_SELECTED);
			}
		});
		this.add(btnVolumeSe);
		addToCursorArr(btnVolumeSe);
		
		slVolumeSe = new JSlider();
		slVolumeSe.setValue(Main.saveData.getVolumeSe());
		slVolumeSe.setBounds(180, 150, 300, 40);
		slVolumeSe.addChangeListener(new ChangeListener () {
			public void stateChanged (ChangeEvent e) {
				sound.setVolumeSe(slVolumeSe.getValue());
				lVolumeSe.setText("" + slVolumeSe.getValue());
			}
		});
		this.add(slVolumeSe);
		
		lVolumeSe = new JLabel(Integer.toString(Main.saveData.getVolumeSe()));
		lVolumeSe.setBounds(490, 150, 150, 40);
		lVolumeSe.setFont(new Font("PixelMplus10", Font.PLAIN, 18));
		this.add(lVolumeSe);
		
		btnBack = new JButton("もどる");
		btnBack.setBounds(200, 200, 150, 40);
		btnBack.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				stop();
				if (PanelController.getBeforeCalledLoopNum() == PanelController.MAP_PANEL) {
					mController.showMapPanel();
					PanelController.callBack();
				} else {
					mController.showTitlePanel();
					PanelController.reserveCallLoop(PanelController.TITLE_PANEL);
				}
			}
		});
		this.add(btnBack);
		
		this.setVisible(false);
	}
	
	// ==================================================
	//  ループ内処理
	// ==================================================
	/**
	 * キー入力受付前の定期処理
	 */
	protected void periodicOpBeforeInput() {
		if (mSelectedSettingItem != NO_SELECTED) {
			actionItemSelected(mSelectedSettingItem);
			mSelectedSettingItem = NO_SELECTED;
		}
	}

	/**
	 * 上矢印キー入力受付時の処理
	 */
	protected void onPressKeyUp() {
		moveCursor(CURSOR_INDEX_MINUS);
	}

	/**
	 * 下矢印キー入力受付時の処理
	 */
	protected void onPressKeyDown() {
		moveCursor(CURSOR_INDEX_PLUS);
	}

	/**
	 * 左矢印キー入力受付時の処理
	 */
	protected void onPressKeyLeft() {
		// No operation
	}

	/**
	 * 右矢印キー入力受付時の処理
	 */
	protected void onPressKeyRight() {
		// No operation
	}

	/**
	 * Zキー入力受付時の処理
	 */
	protected void onPressKeyZ() {
		clickOnCursor();
	}

	/**
	 * スペースキー入力受付時の処理
	 */
	protected void onPressKeySpace() {
		btnBack.doClick();
	}

	/**
	 * キー入力受付後の定期処理
	 */
	protected void periodicOpAfterInput() {
		// no operation
	}
	
	// ==================================================
	//  設定項目選択時処理
	// ==================================================
	/**
	 * 設定項目選択時の処理
	 * @param selectedItemIndex 選択された設定項目のインデックス
	 */
	public void actionItemSelected(int selectedItemIndex) {
		switch (selectedItemIndex) {
		case RESET:
			int resetConfirmAns = JOptionPane.showConfirmDialog(null,
					"セーブデータをリセットします．よろしいですか？" + Env.crlf + "(以降復元することはできません)",
					Main.GAME_TITLE, JOptionPane.YES_NO_OPTION);
			if (resetConfirmAns == JOptionPane.YES_OPTION) {
				Init.deleteSaveData();
				Init.resetDataBases();
				stop();
				MenuPanel.exitMenuLoopFlag = MenuPanel.EXIT_MENU_LOOP;
				map_main.exitMapLoopFlag = map_main.EXIT_MAP_LOOP;
				mController.showTitlePanel();
				PanelController.reserveCallLoop(PanelController.TITLE_PANEL);
			}
			break;
			
		case VOLUME_BGM:
		case VOLUME_SE:
			break;
			
		default:
			break;
		}
	}
}
