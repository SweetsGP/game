package gamesystem;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import main.FilePath;

public class MessageBoard extends JPanel {
	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 100;
	
	private int width = DEFAULT_WIDTH;
	private int height = DEFAULT_HEIGHT;
	
	BufferedReader buf = null;
	private boolean isOpened = false;
	
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
	 * 外部のテキストファイルに書き込まれているメッセージを読み込んで出力します．
	 * ただし，改行コードが途中で存在する場合は，その直前までのメッセージを出力します．
	 * 以降，このメソッドをコールするごとに1行ずつ出力します．この場合，引数にはnullを指定してください．
	 * 出力途中のテキストファイルが存在する状態で別のテキストファイルを引数で指定した場合，
	 * 出力途中のテキストファイルはクローズされ，それに関する情報は破棄されます．
	 * @param filePath 表示したいメッセージが書き込まれたテキストファイルのパス or null
	 * @return ファイルがクローズされた状態で終了 -> true / それ以外 -> false
	 */
	public boolean showMessageTextFile(String filePath) {
		String lineMsg;
		
		// 引数チェック
		// 引数ありの場合
		if (filePath != null) {
			// オープン済みのファイルが存在する場合はクローズする
			if (isOpened) {
				try {
					buf.close();
//					isOpened = false;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// ファイルオープン
			try{
				buf = new BufferedReader(new FileReader(filePath));
				isOpened = true;
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
			
		} else {
			// 引数がnullの場合 -> ファイルオープン済み
			if (buf == null) {
				return true;
			}
		}
		
		// メッセージ読み込み
		try {
			if ((lineMsg = buf.readLine()) != null) {
				showMessage(lineMsg);
			} else {
				try {
					buf.close();
					isOpened = false;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return !isOpened;
	}
	
	/**
	 * 表示中のメッセージを消去して、メッセージボードを非表示にします
	 */
	public void deleteMessageBoard() {
		setMessage("");
		this.setVisible(false);
	}

}
