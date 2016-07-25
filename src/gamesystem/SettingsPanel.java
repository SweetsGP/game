package gamesystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
 * @author kitayamahideya
 *
 */
public class SettingsPanel extends JPanel implements KeyListener {
	private static final int RUNNING_SETTINGS_LOOP = 0;
	private static final int EXIT_SETTINGS_LOOP = 1;
	
	private static final int NO_SELECTED = 0;
	private static final int RESET_SELECTED = 1;
	private static final int VOLUME_BGM_SELECTED = 2;
	private static final int VOLUME_SE_SELECTED = 3;
	
	private static int exitSettingsLoopFlag = RUNNING_SETTINGS_LOOP;
	private static int selectState = NO_SELECTED;
	private static int cursorLocation = NO_SELECTED;
	
	private PanelController controller;
	private JLabel pSettings;
	private JLabel lVolumeBgm, lVolumeSe;
	private JButton btnReset,btnVolumeBgm, btnVolumeSe, btnBack;
	private JButton[] btnSettings;
	private JSlider slVolumeBgm, slVolumeSe;
	
	private Sound soundControl;
	
	private static int key_state = 0;
	
	/**
	 * コンストラクタ
	 * @param pc 画面遷移管理インスタンス
	 */
	public SettingsPanel(PanelController pc, Sound s) {
		controller = pc;
		soundControl = s;
		
		this.setName("Settings");
		this.setLayout(null);
		this.setSize(PanelController.PANEL_WIDTH,PanelController.PANEL_HEIGHT);
		this.addKeyListener(this);
		
		this.setFocusable(true);

		pSettings = new JLabel("せってい");
		pSettings.setBounds(100, 5, 400, 40);
		pSettings.setFont(new Font("PixelMplus10", Font.PLAIN, 30));
		this.add(pSettings);
		
		btnSettings = new JButton[3];

		btnReset = new JButton("リセット");
		btnReset.setBounds(20, 50, 150, 40);
		btnReset.setBorderPainted(false);
		btnReset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				selectState = RESET_SELECTED;
				setCursor(RESET_SELECTED);
			}
		});
		this.add(btnReset);
		btnSettings[0] = btnReset;
		
		btnVolumeBgm = new JButton("BGM音量");
		btnVolumeBgm.setBounds(20, 100, 150, 40);
		btnVolumeBgm.setBorderPainted(false);
		btnVolumeBgm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				selectState = VOLUME_BGM_SELECTED;
				setCursor(VOLUME_BGM_SELECTED);
			}
		});
		this.add(btnVolumeBgm);
		btnSettings[1] = btnVolumeBgm;
		
		slVolumeBgm = new JSlider();
		slVolumeBgm.setBounds(180, 100, 300, 40);
		slVolumeBgm.addChangeListener(new ChangeListener () {
			public void stateChanged (ChangeEvent e) {
				soundControl.setVolumeBgm(slVolumeBgm.getValue());
				lVolumeBgm.setText("" + slVolumeBgm.getValue());
			}
		});
		this.add(slVolumeBgm);
		
		lVolumeBgm = new JLabel("50");
		lVolumeBgm.setBounds(490, 100, 150, 40);
		lVolumeBgm.setFont(new Font("PixelMplus10", Font.PLAIN, 18));
		this.add(lVolumeBgm);
		
		btnVolumeSe = new JButton("SE音量");
		btnVolumeSe.setBounds(20, 150, 150, 40);
		btnVolumeSe.setBorderPainted(false);
		btnVolumeSe.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				selectState = VOLUME_SE_SELECTED;
				setCursor(VOLUME_SE_SELECTED);
			}
		});
		this.add(btnVolumeSe);
		btnSettings[2] = btnVolumeSe;
		
		slVolumeSe = new JSlider();
		slVolumeSe.setBounds(180, 150, 300, 40);
		slVolumeSe.addChangeListener(new ChangeListener () {
			public void stateChanged (ChangeEvent e) {
				soundControl.setVolumeSe(slVolumeSe.getValue());
				lVolumeSe.setText("" + slVolumeSe.getValue());
			}
		});
		this.add(slVolumeSe);
		
		lVolumeSe = new JLabel("50");
		lVolumeSe.setBounds(490, 150, 150, 40);
		lVolumeSe.setFont(new Font("PixelMplus10", Font.PLAIN, 18));
		this.add(lVolumeSe);
		
		btnBack = new JButton("もどる");
		btnBack.setBounds(200, 200, 150, 40);
		btnBack.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				exitSettingsLoopFlag = EXIT_SETTINGS_LOOP;
//				controller.showTitlePanel();
				controller.showMapPanel();
			}
		});
		this.add(btnBack);
		
		this.setVisible(false);
	}
	
	/**
	 * 設定画面可視化
	 */
	public void showSettings() {
		this.setVisible(true);
		this.requestFocus(true);
	}
	
	/**
	 * 設定画面処理ルーチン
	 */
	public void loop() {
		exitSettingsLoopFlag = RUNNING_SETTINGS_LOOP;
		
		key_state = 0;
		cursorLocation = NO_SELECTED;
		selectState = NO_SELECTED;
		
		while (true) {
			// ループ終了判定
			if (exitSettingsLoopFlag != RUNNING_SETTINGS_LOOP || Main.exitFlag != Main.RUNNING) {
				break;
			}
			
			// 各項目の処理
			switch (selectState) {
			case RESET_SELECTED:
				int resetConfirmAns = JOptionPane.showConfirmDialog(null,
						"セーブデータをリセットします．よろしいですか？" + Env.crlf + "(以降復元することはできません)",
						Main.GAME_TITLE, JOptionPane.YES_NO_OPTION);
				if (resetConfirmAns == JOptionPane.YES_OPTION) {
					Init.deleteSaveData();
					Init.resetDataBases();
					exitSettingsLoopFlag = EXIT_SETTINGS_LOOP;
					MenuPanel.exitMenuLoopFlag = MenuPanel.EXIT_MENU_LOOP;
					map_main.exitMapLoopFlag = map_main.EXIT_MAP_LOOP;
					controller.showTitlePanel();
				}
				break;
				
			case VOLUME_BGM_SELECTED:
			case VOLUME_SE_SELECTED:
				break;
				
			default:
				break;
			}
			selectState = NO_SELECTED;
			
			// キー入力受付，反映
			switch (key_state) {
			case 1:  // UP
				if (cursorLocation > RESET_SELECTED) {
					cursorLocation--;
				}
				break;
			case 2:  // DOWN
				if (cursorLocation < VOLUME_SE_SELECTED) {
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
				if (cursorLocation != NO_SELECTED) {
					btnSettings[cursorLocation-1].doClick();
				}
				break;
			case 6:  // X
				// no operation
				break;
			case 7:  // スペースキー
				deleteCursor();
				btnBack.doClick();
				break;
			default:
				break;
			}
			key_state = 0;
			
			// カーソル表示 (暫定的に青枠表示)
			if (cursorLocation != NO_SELECTED) {
				setCursor(cursorLocation);
			}
			
			// 待機時間
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 与えられた番号の位置にカーソル表示をセットする
	 * @param cursor カーソル表示を設置したい項目の位置番号
	 */
	private void setCursor(int cursor) {
		for (int i = 0; i < VOLUME_SE_SELECTED; i++) {
			if (i == cursor-1) {
				btnSettings[i].setBorderPainted(true);
				btnSettings[i].setBorder(new LineBorder(Color.BLUE, 2, true));
			} else {
				btnSettings[i].setBorderPainted(false);
			}
		}
		this.repaint();
	}
	
	/**
	 * カーソル表示を削除し、項目未選択の状態にします
	 */
	private void deleteCursor() {
		if (cursorLocation != NO_SELECTED) {
			btnSettings[cursorLocation-1].setBorderPainted(false);
		}
		cursorLocation = NO_SELECTED;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// no operation
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key;

		/* getting key_code */
		key = e.getKeyCode();

		/* each key_action */
		switch(key){
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
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// no operation
	}

}
