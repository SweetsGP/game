package gamesystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import main.BasePanel;
import main.FilePath;
import main.HasPlaceholderJTextField;
import main.PanelController;

public class CharacterCreatingPanel extends BasePanel {
	// 定数
	// 優先度
//	public static final int PRIORITY_CHARACTER_CREATE = 2;
	// 進行度
	public static final int STAGE_FIRST_INTRO = 0;
	public static final int STAGE_SET_PLAYER_NAME = 1;
	public static final int STAGE_SECOND_INTRO = 2;
	public static final int STAGE_SET_RIVAL_NAME = 3;
	public static final int STAGE_READY = 4;
	// テキストフィールド入力状態
	public static final int INPUT_NOW = 0;
	public static final int INPUT_FINISHED = 1;
	// キー
	public static final int KEY_ENTER_ON_TXTFIELD = 8;
	
	// 変数
	private static int stage = STAGE_FIRST_INTRO;
	private static int inputFlag = INPUT_NOW;
	
	private static int readLineCounter = 0;
	
	JLabel lDoctor, lMale, lFemale;
	MessageBoard messageBoard;
	HasPlaceholderJTextField txtfName;
	JButton btnOk;
	
	// test code
	String playerName = "";
	String rivalName = "";
	
	/**
	 * コンストラクタ
	 * @param controller
	 */
	public CharacterCreatingPanel(PanelController controller) {
//		super(controller, PRIORITY_CHARACTER_CREATE);
		super(controller);
		
		lDoctor = new JLabel(new ImageIcon(FilePath.charaDirPath + "hakase.jpg"));
		lDoctor.setBounds(300, 100, 200, 200);
		lDoctor.setVisible(false);
		this.add(lDoctor);
		
		lMale = new JLabel(new ImageIcon(FilePath.charaDirPath + "syujinkou0.png"));
		lMale.setBounds(300, 100, 200, 200);
		lMale.setVisible(false);
		this.add(lMale);
		
		lFemale = new JLabel(new ImageIcon(FilePath.charaDirPath + "syujinkou1.png"));
		lFemale.setBounds(300, 100, 200, 200);
		lFemale.setVisible(false);
		this.add(lFemale);
		
		messageBoard = new MessageBoard(700, 100);
		messageBoard.setLocation(50, 450);
		this.add(messageBoard);
		
		txtfName = new HasPlaceholderJTextField();
		txtfName.setBounds(100, 350, 450, 50);
		txtfName.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mKeyState = KEY_ENTER_ON_TXTFIELD;  // Enterキー
			}
		});
		txtfName.setEnabled(false);
		txtfName.setVisible(false);
		this.add(txtfName);
		
		btnOk = new JButton("OK");
		btnOk.setBounds(600, 350, 100, 50);
		btnOk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				inputFlag = INPUT_FINISHED;
			}
		});
		btnOk.setEnabled(false);
		btnOk.setVisible(false);
		this.add(btnOk);

		this.setVisible(false);
	}
	
	/**
	 * キー入力受付前の定期処理
	 */
	@Override
	protected void periodicOpBeforeInput() {
		switch (stage) {
		case STAGE_FIRST_INTRO:
			if (readLineCounter == 0) {
				lDoctor.setVisible(true);
				messageBoard.showMessageTextFile(FilePath.textDirPath + "CharaMake1.txt");
				readLineCounter++;
			}
			break;
			
		case STAGE_SET_PLAYER_NAME:
			if (readLineCounter == 0) {
				lDoctor.setVisible(false);
				lMale.setVisible(true);
				txtfName.setPlaceholder("主人公の名前[Enterで決定]");
				this.setTextFieldEnabled(true);
				readLineCounter++;
			}
			
			if (inputFlag == INPUT_FINISHED) {
				playerName = txtfName.getText();
				System.out.println("Player's name:" + playerName);
				
				lMale.setVisible(false);
				this.setTextFieldEnabled(false);
				readLineCounter = 0;
				stage = STAGE_SECOND_INTRO;
				
			} else if (mKeyState == KEY_ENTER_ON_TXTFIELD) {
				// テキストフィールド内でEnter -> ok
				mKeyState = KEY_NO_TYPED;
				btnOk.doClick();
			}
			break;
			
		case STAGE_SECOND_INTRO:
			if (readLineCounter == 0) {
				lDoctor.setVisible(true);
				messageBoard.showMessage("ふむ．．．");
				readLineCounter++;  // readLineCounter = 1で待機
			} else if (readLineCounter == 2) {
				messageBoard.showMessage(playerName + "　と　いうんだな！");
				readLineCounter++;  // readLineCounter = 3で待機
			} else if (readLineCounter == 4){
				messageBoard.showMessageTextFile(FilePath.textDirPath + "CharaMake2.txt");
				readLineCounter++;
			}
			break;
			
		case STAGE_SET_RIVAL_NAME:
			if (readLineCounter == 0) {
				lDoctor.setVisible(false);
				lFemale.setVisible(true);
				txtfName.setPlaceholder("ライバルの名前[Enterで決定]");
				this.setTextFieldEnabled(true);
				readLineCounter++;
			}
			
			if (inputFlag == INPUT_FINISHED) {
				rivalName = txtfName.getText();
				System.out.println("Rival's name:" + rivalName);
				
				lFemale.setVisible(false);
				this.setTextFieldEnabled(false);
				readLineCounter = 0;
				stage = STAGE_READY;
				
			} else if (mKeyState == KEY_ENTER_ON_TXTFIELD) {
				// テキストフィールド内でEnter -> ok
				mKeyState = KEY_NO_TYPED;
				btnOk.doClick();
			}
			break;
			
		case STAGE_READY:
			if (readLineCounter == 0) {
				lDoctor.setVisible(true);
				messageBoard.showMessage("そうだ　そうだ！　おもいだしたぞ");
				readLineCounter++;  // readLineCounter = 1で待機
			} else if (readLineCounter == 2) {
				messageBoard.showMessage(rivalName + "　というなまえだ");
				readLineCounter++;  // readLineCounter = 3で待機
			} else if (readLineCounter == 4) {
				messageBoard.showMessage(playerName + "！");
				readLineCounter++;  // readLineCounter = 5で待機
			} else if (readLineCounter == 6){
				messageBoard.showMessageTextFile(FilePath.textDirPath + "CharaMake3.txt");
				readLineCounter++;
			}
			break;
		
		default:
			break;
		}
	}
	
	/**
	 * Zキー入力受付時の処理
	 */
	@Override
	protected void onPressKeyZ() {
		if (((stage == STAGE_SECOND_INTRO && readLineCounter < 4)
				|| (stage == STAGE_READY && readLineCounter < 6))) {
			readLineCounter++;
			return;
		}
		
		if (messageBoard.showMessageTextFile(null)) {
			readLineCounter = 0;
			if (stage == STAGE_READY) {
				// マップ画面に遷移
				mController.showMapPanel();
//				mLoopState = LOOP_EXIT;
				stop();
			} else {
				stage++;  // 次のステージへ
			}
		} else {
			readLineCounter++;  // 次の行へ
		}
	}
	
	/**
	 * キー入力受付後の定期処理
	 */
	@Override
	protected void periodicOpAfterInput() {
		// no operation
	}
	
	/**
	 * テキストフィールドの有効化/無効化
	 * @param b true -> 有効化 / false -> 無効化
	 */
	private void setTextFieldEnabled(boolean b) {
		if (b == false) {
			txtfName.setText("");
		}
		
		inputFlag = INPUT_NOW;
		txtfName.setEnabled(b);
		txtfName.setVisible(b);
		btnOk.setEnabled(b);
		btnOk.setVisible(b);
		
		txtfName.requestFocus(b);
	}

}
