package gamesystem;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class MessageBoard extends JPanel {
	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 100;
	
	private int width = DEFAULT_WIDTH;
	private int height = DEFAULT_HEIGHT;
	
	JLabel messageTextArea;
	
	/**
	 * コンストラクタ
	 * 600*100のメッセージボードを生成します
	 */
	public MessageBoard() {
		this.setBorder(new LineBorder(Color.black,2,true));
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setBackground(Color.white);
		
		messageTextArea = new JLabel();
		messageTextArea.setFont(new Font("PixelMplus10",Font.PLAIN,20));
		messageTextArea.setForeground(Color.BLACK);
		messageTextArea.setHorizontalAlignment(JLabel.CENTER);
		messageTextArea.setVerticalAlignment(JLabel.CENTER);
		messageTextArea.setBounds(10, 10, DEFAULT_WIDTH-20, DEFAULT_HEIGHT-20);
		this.add(messageTextArea);
		
		this.setVisible(false);
	}
	
	/**
	 * コンストラクタ
	 * @param w 生成するメッセージボードの横の大きさ
	 * @param h 生成するメッセージボードの縦の大きさ
	 */
	public MessageBoard(int w, int h) {
		width = w;
		height = h;
		
		this.setBorder(new LineBorder(Color.black,2,true));
		this.setSize(width, height);
		this.setBackground(Color.white);
		
		messageTextArea = new JLabel();
		messageTextArea.setFont(new Font("PixelMplus10",Font.PLAIN,20));
		messageTextArea.setForeground(Color.BLACK);
		messageTextArea.setHorizontalAlignment(JLabel.CENTER);
		messageTextArea.setVerticalAlignment(JLabel.CENTER);
		messageTextArea.setBounds(10, 10, width-20, height-20);
		this.add(messageTextArea);
		
		this.setVisible(false);
	}
	
	/**
	 * メッセージボードにメッセージを設定します(可視化の操作はしません)
	 * @param msg 設定したいメッセージ
	 */
	public void setMessage(String msg) {
		messageTextArea.setText(msg);
	}
	
	/**
	 * メッセージボードにメッセージを設定して表示(可視化)します
	 * @param msg 設定したいメッセージ
	 */
	public void showMessage(String msg) {
		setMessage(msg);
		this.setVisible(true);
	}
	
	/**
	 * 表示中のメッセージを消去して、メッセージボードを非表示にします
	 */
	public void deleteMessageBoard() {
		setMessage("");
		this.setVisible(false);
	}

}
