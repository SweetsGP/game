package gamesystem;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Main;
import main.PanelController;

public class CharaMake extends JPanel implements KeyListener{
	// ループ終了フラグ定数
	public static final int RUNNING_CHARA_MAKE_LOOP = 0;
	public static final int EXIT_CHARA_MAKE_LOOP = 1;
	// ループ終了フラグ
	public static int exitCHARA_MAKELoopFlag = RUNNING_CHARA_MAKE_LOOP;

	private PanelController controller;
	private static int key_state = 0;
	JLabel lName,lSex;
	JTextField tfName;

	public CharaMake(PanelController pc){
		controller=pc;

		this.setLayout(null);
		this.setSize(300, 400);
		this.setBackground(Color.white);

		this.setFocusable(true);
		this.setVisible(false);
		this.addKeyListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int key;

		/* キーコードの格納 */
		key = e.getKeyCode();

		switch(key) {
		case KeyEvent.VK_Z:
			key_state = 1;
			break;
		case KeyEvent.VK_UP:
			key_state = 2;
			break;
		case KeyEvent.VK_DOWN:
			key_state = 3;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	public void selectType(){

	}

	public void showCharaMake(int EventID){
		this.setVisible(true);
		this.requestFocus(true);
		key_state = 0;
		try{
			while(true){
				// 終了フラグチェック -> フラグが立っていれば終了する
				if (Main.exitFlag != Main.RUNNING) {
					break;
				}
				if(key_state == 0){
				}else if(key_state == 1){
					System.out.println("!!!");
					key_state = 0;
				}else if(key_state == 2 || key_state == 3){
					key_state = 0;
					this.requestFocus(false);
					this.setVisible(false);
					break;
				}


				/* 待機時間を少し設ける */
				Thread.sleep(10);
			}
		}catch(Exception e){}


	}
}
