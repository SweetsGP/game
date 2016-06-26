package gamesystem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.FilePath;
import main.Main;
import main.PanelController;

public class CharaMessage extends JPanel implements KeyListener{
	private PanelController controller;
	JPanel message_board;
	JLabel message;
	BufferedReader buffered_reader;
	String line_message;
	private static final int CANVAS_WIDTH = 800;
	private static final int CANVAS_HEIGHT = 150;
	private static final int VIEW_WIDTH = 800;
	private static final int VIEW_HEIGHT = 450;
	private static int x = 0;
	private static int y = 450;
	private static int key_state = 0;
	int i=1;
	int eventID = 0;
	public static int textfile_num = 3;
	// ループ終了フラグ定数
	public static final int RUNNING_CHARA_MAKE_LOOP = 0;
	public static final int EXIT_CHARA_MAKE_LOOP = 1;
	// ループ終了フラグ
	public static int exitMapLoopFlag = RUNNING_CHARA_MAKE_LOOP;
	// openテキストファイルフラグ
	public static int openTextFileFlag = 0;
	CharaMake charamake;

	public CharaMessage(){
		this.setName("CharaMessage");
		this.setLayout(null);
		this.setSize(MainFrame.WINDOW_WIDTH, MainFrame.WINDOW_HEIGHT);

		setLayout(null);
		setSize(PanelController.PANEL_WIDTH,PanelController.PANEL_HEIGHT);
		setFocusable(true);
		charamake = new CharaMake(controller);
		charamake.setLocation(500, 0);

		message_board = CreateMessageboard();
		this.addKeyListener(this);
		message = CreateMessage();
		add(message_board);
		message_board.add(message);
		setVisible(true);
		add(charamake);
	}

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		int key;

		/* キーコードの格納 */
		key = e.getKeyCode();

		/* キーコードの種類分けとフラグ管理 */
		switch(key){
		case KeyEvent.VK_Z:
			key_state = 1;
			break;
		default:
			break;

		}

	}

	public void keyReleased(KeyEvent e) {}


	public  JLabel CreateMessage(){
		JLabel m = new JLabel();
		m.setFont(new Font("PixelMplus12",Font.PLAIN,25));
		m.setPreferredSize(new Dimension(CANVAS_WIDTH,CANVAS_HEIGHT));
		m.setHorizontalAlignment(JLabel.LEFT);
		m.setVerticalAlignment(JLabel.TOP);
		return m;
	}

	public JPanel CreateMessageboard(){
		JPanel mb = new JPanel();
		mb.setSize(600, 100);
		mb.setBounds(x,y,CANVAS_WIDTH,CANVAS_HEIGHT);
		mb.setBackground(Color.white);
		return mb;
	}

	public JPanel CreateView(){
		JPanel cv = new JPanel();
		cv.setSize(600, 100);
		cv.setBounds(0,0,VIEW_WIDTH,VIEW_HEIGHT);
		cv.setBackground(Color.white);
		return cv;
	}

	public void OpenTextFile(){
		try{
			buffered_reader = new BufferedReader(new FileReader(FilePath.textDirPath + "CharaMake"+ i +".txt"));
			openTextFileFlag = 1;
			i++;
		}catch(FileNotFoundException e){
			System.out.println(e);
		}

	}

	public void CloseTextFile(){
		try{
			buffered_reader.close();
			openTextFileFlag = 0;
		}catch(IOException e){
			System.out.println(e);
		}
		System.out.println(key_state);
	}

	public void CharaMake(){
		switch(i){
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		default:
			break;
		}
	}


	public void loop(){
		try{
			while(true){
				// 終了フラグチェック -> フラグが立っていれば終了する
				if (Main.exitFlag != Main.RUNNING) {
					break;
				}

				if(key_state==0){

				}else if(key_state==1){
					if(openTextFileFlag==0 && i < textfile_num){
						OpenTextFile();
					}
					if((line_message=buffered_reader.readLine()) != null){
						message.setText(line_message);
					}else{
						CloseTextFile();
						this.requestFocus(false);
						charamake.showCharaMake(eventID);
						this.requestFocus(true);
					}
					key_state=0;
				}
				/* 待機時間を少し設ける */
				Thread.yield();
			}
		}catch(Exception e){}
	}
}

