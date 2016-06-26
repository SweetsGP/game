package gamesystem;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import main.FilePath;
import main.Main;
import main.PanelController;
import main.Sound;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

/**
 * タイトル画面クラス
 * @author kitayamahideya
 *
 */
public class TitlePanel extends JPanel implements KeyListener {
	public static final int RUNNING_TITLE_LOOP = 0;
	public static final int EXIT_TITLE_LOOP = 1;
	
	private static final int ACTION_NO_SELECTED = -1;
	private static final int ACTION_NEW_GAME = 0;
	private static final int ACTION_CONTINUE = 1;
	private static final int ACTION_SETTINGS = 2;
	private static final int ACTION_EXIT = 3;
	
	public static int exitTitleLoopFlag = RUNNING_TITLE_LOOP;
	
	private MainFrame mainFrame;
	private PanelController controller;

	private BufferedImage backgroundImage;
	private JLabel pTitle;
	private JButton btnNew,btnContinue,btnSetting, btnExit;
	private JButton btnActions[];
	
	private static int key_state = 0;

	/**
	 * コンストラクタ
	 * @param mf メインフレーム
	 * @param pc 画面遷移管理インスタンス
	 */
	public TitlePanel(MainFrame mf, PanelController pc) {
		mainFrame = mf;
		controller = pc;

		try {
			this.backgroundImage = ImageIO.read(new File(FilePath.imgDirPath + "title"
					+ FilePath.fs + "sample.jpg"));
		} catch (IOException ex) {
			ex.printStackTrace();
			this.backgroundImage = null;
		}
		this.setOpaque(false);

		this.setName("Title");
		this.setLayout(null);
		this.setSize(PanelController.PANEL_WIDTH,PanelController.PANEL_HEIGHT);
		
		this.setFocusable(true);
		this.addKeyListener(this);

		//		pTitle = new JLabel(Main.GAME_TITLE);
		pTitle = new JLabel("TestImage");
		pTitle.setFont(new Font("PixelMplus10", Font.PLAIN, 30));
		pTitle.setForeground(Color.red);
		pTitle.setBounds(100, 5, 400, 40);
		this.add(pTitle);
		
		btnActions = new JButton[4];

		btnNew = new JButton("New Game");
		btnNew.setFont(new Font("PixelMplus10",Font.PLAIN,16));
		btnNew.setForeground(Color.white);
		btnNew.setBorderPainted(false);
		btnNew.setBounds(50, 500, 150, 40);
		btnNew.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				setCursor(ACTION_NEW_GAME);
				controller.showCharaMessagePanel();
				exitTitleLoopFlag = EXIT_TITLE_LOOP;
				deleteCursor(ACTION_NEW_GAME);
			}
		});
		this.add(btnNew);
		btnActions[0] = btnNew;

//		btnContinue = new JButton("Continue");
		btnContinue = new JButton("Map");
		btnContinue.setFont(new Font("PixelMplus10",Font.PLAIN,16));
		btnContinue.setForeground(Color.white);
		btnContinue.setBorderPainted(false);
		btnContinue.setBounds(230, 500, 150, 40);
		btnContinue.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				setCursor(ACTION_CONTINUE);
//				if (SaveDataIO.checkExistsSaveData()) {
					controller.showMapPanel();
//				} else {
//					// セーブデータが見つからない場合は遷移しない
//					JOptionPane.showMessageDialog(null, "Not found save data.",
//							Main.GAME_TITLE, JOptionPane.ERROR_MESSAGE);
//				}
					exitTitleLoopFlag = EXIT_TITLE_LOOP;
					deleteCursor(ACTION_CONTINUE);
			}
		});
		this.add(btnContinue);
		btnActions[1] = btnContinue;

//		btnSetting = new JButton("Settings");
		btnSetting = new JButton("Battle");
		btnSetting.setFont(new Font("PixelMplus10",Font.PLAIN,16));
		btnSetting.setForeground(Color.white);
		btnSetting.setBorderPainted(false);
		btnSetting.setBounds(410, 500, 150, 40);
		btnSetting.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				setCursor(ACTION_SETTINGS);
//				controller.showSettingsPanel();
				controller.showBattlemainPanel();
				exitTitleLoopFlag = EXIT_TITLE_LOOP;
				deleteCursor(ACTION_SETTINGS);
			}
		});
		this.add(btnSetting);
		btnActions[2] = btnSetting;

		btnExit = new JButton("Quit");
		btnExit.setFont(new Font("PixelMplus10",Font.PLAIN,16));
		btnExit.setForeground(Color.white);
		btnExit.setBorderPainted(false);
		btnExit.setBounds(590, 500, 150, 40);
		btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				setCursor(ACTION_EXIT);
				int exitConfirmAns = JOptionPane.showConfirmDialog(mainFrame,
						"ゲームを終了しますか？",
						Main.GAME_TITLE, JOptionPane.YES_NO_OPTION);
				if (exitConfirmAns == JOptionPane.YES_OPTION) {
					mainFrame.dispose();
					Main.exitFlag = Main.EXIT_OK;
				}
			}
		});
		this.add(btnExit);
		btnActions[3] = btnExit;
	}
	
	/**
	 * 入力待機ループ
	 */
	public void loop(Sound s) {
		int cursorLocation = ACTION_NO_SELECTED;
		
		exitTitleLoopFlag = RUNNING_TITLE_LOOP;
		key_state = 0;
		
		while (true) {
			if (Main.exitFlag != Main.RUNNING || exitTitleLoopFlag != RUNNING_TITLE_LOOP) {
				break;
			}
			
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
				if (cursorLocation < ACTION_EXIT) {
					cursorLocation++;
				}
				break;
			case 5:  // Z
				if (cursorLocation != ACTION_NO_SELECTED) {
					btnActions[cursorLocation].doClick();
				}
				break;
			case 6:  // X
				// no operation
				break;
			case 7:  // スペースキー
				// no operation
				break;
			default:
				break;
			}
			key_state = 0;

			// カーソル表示 (暫定的に青枠表示)
			if (cursorLocation != ACTION_NO_SELECTED) {
				setCursor(cursorLocation);
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 与えられた番号の位置にカーソル表示をセットする
	 * @param cursorLocation カーソル表示を設置したい項目の位置番号
	 */
	private void setCursor(int cursorLocation) {
		for (int i = 0; i < ACTION_EXIT + 1; i++) {
			if (i == cursorLocation) {
				btnActions[i].setBorderPainted(true);
				btnActions[i].setBorder(new LineBorder(Color.white, 2, true));
			} else {
				btnActions[i].setBorderPainted(false);
			}
		}
		this.repaint();
	}
	
	/**
	 * カーソル表示を削除し、項目未選択の状態にします
	 */
	private void deleteCursor(int cursorLocation) {
		if (cursorLocation != ACTION_NO_SELECTED) {
			btnActions[cursorLocation].setBorderPainted(false);
		}
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, MainFrame.WINDOW_WIDTH, MainFrame.WINDOW_HEIGHT, this);
		super.paint(g);
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
