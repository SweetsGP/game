package gamesystem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import main.FilePath;
import main.HasPlaceholderJTextField;
import main.Main;
import main.PanelController;

public class CharacterMakingPanel extends JPanel implements KeyListener {
	
	public static final int RUNNING_CM_LOOP = 0;
	public static final int EXIT_CM_LOOP = 1;
	
	public static final int STAGE_INTRO = 0;
	public static final int STAGE_SET_PLAYER_NAME = 1;
	public static final int STAGE_SET_RIVAL_NAME = 2;
	public static final int STAGE_READY = 3;
	
	public static final int INPUT_NOW = 0;
	public static final int INPUT_FINISHED = 1;
	
	public static final int KEY_ENTER_ON_TXTFEILD = 8;
	
	public static int exitCMLoopFlag = RUNNING_CM_LOOP;
	public static int stage = STAGE_INTRO;
	public static int inputFlag = INPUT_NOW;
	
	private PanelController controller;
	
	MessageBoard messageBoard;
	HasPlaceholderJTextField txtfName;
	JButton btnOk;
	
	private static int key_state = 0;
	
	public CharacterMakingPanel(PanelController pc) {
		controller = pc;

		this.setLayout(null);
		this.setSize(PanelController.PANEL_WIDTH, PanelController.PANEL_HEIGHT);
		this.setBackground(Color.white);
		this.addKeyListener(this);
		
		messageBoard = new MessageBoard(600, 100);
		messageBoard.setLocation(0, 450);
		this.add(messageBoard);
		
		txtfName = new HasPlaceholderJTextField();
		txtfName.setBounds(100, 200, 450, 50);
		txtfName.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				key_state = KEY_ENTER_ON_TXTFEILD;  // Enterキー
			}
		});
		txtfName.setEnabled(false);
		txtfName.setVisible(false);
		this.add(txtfName);
		
		btnOk = new JButton("OK");
		btnOk.setBounds(600, 200, 100, 50);
		btnOk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				inputFlag = INPUT_FINISHED;
			}
		});
		btnOk.setEnabled(false);
		btnOk.setVisible(false);
		this.add(btnOk);

		this.setFocusable(true);
		this.setVisible(false);
	}
	
	/**
	 * 可視化
	 */
	public void showCharacterMakingPanel() {
		this.setVisible(true);
		this.requestFocus(true);
	}
	
	/**
	 * キャラクターメイク処理ループ
	 */
	public void loop() {
		int counter = 0;
		String tmpName;
		
		exitCMLoopFlag = RUNNING_CM_LOOP;
		key_state = 0;
		
		while (true) {
			if (exitCMLoopFlag != RUNNING_CM_LOOP || Main.exitFlag != Main.RUNNING) {
				break;
			}
			
			switch (stage) {
			case STAGE_INTRO:
				if (counter == 0) {
					messageBoard.showMessageTextFile(FilePath.textDirPath + "CharaMake1.txt");
					counter++;
				} else if (key_state == 5) {  // zキーで会話送り
					if (messageBoard.showMessageTextFile(null)) {
						counter = 0;
						stage = STAGE_SET_PLAYER_NAME;
					} else {
						counter++;
					}
					key_state = 0;
				}
				break;
				
			case STAGE_SET_PLAYER_NAME:
				if (counter == 0) {
					txtfName.setPlaceholder("主人公の名前");
					txtfName.setEnabled(true);
					txtfName.setVisible(true);
					btnOk.setEnabled(true);
					btnOk.setVisible(true);
					
					txtfName.requestFocus(true);
					counter++;
				}
				
				if (inputFlag == INPUT_FINISHED) {
					tmpName = txtfName.getText();
					System.out.println("Player's name:" + tmpName);
					
					txtfName.setText("");
					txtfName.setEnabled(false);
					txtfName.setVisible(false);
					btnOk.setEnabled(false);
					btnOk.setVisible(false);
					inputFlag = INPUT_NOW;
					
					counter = 0;
					stage = STAGE_SET_RIVAL_NAME;
					
				} else if (key_state == KEY_ENTER_ON_TXTFEILD) {
					// テキストフィールド内でEnter -> ok
					key_state = 0;
					btnOk.doClick();
				}
				break;
				
			case STAGE_SET_RIVAL_NAME:
				if (counter == 0) {
					messageBoard.showMessageTextFile(FilePath.textDirPath + "CharaMake2.txt");
					counter++;
				} else if (key_state == 5) {  // zキーで会話送り
					if (messageBoard.showMessageTextFile(null)) {
						txtfName.setPlaceholder("ライバルの名前");
						txtfName.setEnabled(true);
						txtfName.setVisible(true);
						btnOk.setEnabled(true);
						btnOk.setVisible(true);
						
						txtfName.requestFocus(true);
						counter++;
					} else {
						counter++;
					}
					key_state = 0;
				}
				
				if (inputFlag == INPUT_FINISHED) {
					tmpName = txtfName.getText();
					System.out.println("Rival's name:" + tmpName);
					
					txtfName.setText("");
					txtfName.setEnabled(false);
					txtfName.setVisible(false);
					btnOk.setEnabled(false);
					btnOk.setVisible(false);
					inputFlag = INPUT_NOW;
					
					counter = 0;
					stage = STAGE_READY;
					
				} else if (key_state == KEY_ENTER_ON_TXTFEILD) {
					// テキストフィールド内でEnter -> ok
					key_state = 0;
					btnOk.doClick();
				}
				break;
				
			case STAGE_READY:
				if (counter == 0) {
					messageBoard.showMessageTextFile(FilePath.textDirPath + "CharaMake3.txt");
					counter++;
				} else if (key_state == 5) {  // zキーで会話送り
					if (messageBoard.showMessageTextFile(null)) {
						counter = 0;
						
						// マップ画面に遷移
						controller.showMapPanel();
						exitCMLoopFlag = EXIT_CM_LOOP;
					} else {
						counter++;
					}
					key_state = 0;
				}
				break;
			
			default:
				break;
			}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int key;

		/* getting key_code */
		key = e.getKeyCode();

		/* each key_action */
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
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
