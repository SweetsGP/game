package gamesystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import main.FilePath;
import main.Main;
import main.PanelController;
//import main.Sound;

/**
 * タイトル画面クラス
 *
 */
public class TitlePanel extends BasePanel {
	
	private static final int ACTION_NEW_GAME = 0;
	private static final int ACTION_CONTINUE = 1;
	private static final int ACTION_SETTINGS = 2;
	private static final int ACTION_EXIT = 3;

	private MainFrame mainFrame;

	private BufferedImage backgroundImage;
	private JLabel pTitle;
	private JButton btnNew,btnContinue,btnSetting, btnExit;
	private JButton btnBattle;

	/**
	 * コンストラクタ
	 * @param mf メインフレーム
	 * @param pc 画面遷移管理インスタンス
	 */
	public TitlePanel(MainFrame mf, PanelController pc) {
		super(pc);
		mainFrame = mf;

		try {
			this.backgroundImage = ImageIO.read(new File(FilePath.imgDirPath + "title"
					+ FilePath.fs + "sample.jpg"));
		} catch (IOException ex) {
			ex.printStackTrace();
			this.backgroundImage = null;
		}
		this.setOpaque(false);

		//		pTitle = new JLabel(Main.GAME_TITLE);
		pTitle = new JLabel("TestImage");
		pTitle.setFont(new Font("PixelMplus10", Font.PLAIN, 30));
		pTitle.setForeground(Color.red);
		pTitle.setBounds(100, 5, 400, 40);
		this.add(pTitle);

		btnNew = new JButton("New Game");
		btnNew.setFont(new Font("PixelMplus10",Font.PLAIN,16));
		btnNew.setForeground(Color.white);
		btnNew.setContentAreaFilled(false);
		btnNew.setBorderPainted(false);
		btnNew.setBounds(50, 500, 150, 40);
		btnNew.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
//				showCursor(ACTION_NEW_GAME);
				mController.showCharacterMakingPanel();
				PanelController.reserveCallLoop(PanelController.CHARACTER_CREATING_PANEL);
				stop();
				deleteCursor();
			}
		});
		this.add(btnNew);
		addToCursorArr(btnNew);
		
		// test code -> バトル画面へのダイレクトアクセス
		btnBattle = new JButton("Battle");
		btnBattle.setFont(new Font("PixelMplus10",Font.PLAIN,16));
		btnBattle.setForeground(Color.white);
		btnBattle.setContentAreaFilled(false);
		btnBattle.setBorderPainted(false);
		btnBattle.setBounds(230, 450, 150, 40);
		btnBattle.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				mController.showBattlemainPanel();
				PanelController.reserveCallLoop(PanelController.BATTLE_PANEL);
				stop();
				deleteCursor();
			}
		});
		this.add(btnBattle);
		addToCursorArr(btnBattle);
		// =====

//		btnContinue = new JButton("Continue");
		btnContinue = new JButton("Map");
		btnContinue.setFont(new Font("PixelMplus10",Font.PLAIN,16));
		btnContinue.setForeground(Color.white);
		btnContinue.setContentAreaFilled(false);
		btnContinue.setBorderPainted(false);
		btnContinue.setBounds(230, 500, 150, 40);
		btnContinue.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
//				showCursor(ACTION_CONTINUE);
//				if (SaveDataIO.checkExistsSaveData()) {
					mController.showMapPanel();
					PanelController.reserveCallLoop(PanelController.MAP_PANEL);
//				} else {
//					// セーブデータが見つからない場合は遷移しない
//					JOptionPane.showMessageDialog(null, "Not found save data.",
//							Main.GAME_TITLE, JOptionPane.ERROR_MESSAGE);
//				}
					stop();
					deleteCursor();
			}
		});
		this.add(btnContinue);
		addToCursorArr(btnContinue);

		btnSetting = new JButton("Settings");
		btnSetting.setFont(new Font("PixelMplus10",Font.PLAIN,16));
		btnSetting.setForeground(Color.white);
		btnSetting.setContentAreaFilled(false);
		btnSetting.setBorderPainted(false);
		btnSetting.setBounds(410, 500, 150, 40);
		btnSetting.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
//				showCursor(ACTION_SETTINGS);
				mController.showSettingsPanel();
				PanelController.reserveCallLoop(PanelController.SETTINGS_PANEL);
				stop();
				deleteCursor();
			}
		});
		this.add(btnSetting);
		addToCursorArr(btnSetting);

		btnExit = new JButton("Quit");
		btnExit.setFont(new Font("PixelMplus10",Font.PLAIN,16));
		btnExit.setForeground(Color.white);
		btnExit.setContentAreaFilled(false);
		btnExit.setBorderPainted(false);
		btnExit.setBounds(590, 500, 150, 40);
		btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
//				showCursor(ACTION_EXIT);
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
		addToCursorArr(btnExit);
	}
	
	// ==================================================
	//  ループ内処理
	// ==================================================
	/**
	 * 左矢印キー入力受付時の処理
	 */
	@Override
	protected void onPressKeyLeft() {
		moveCursor(CURSOR_INDEX_MINUS);
	}

	/**
	 * 右矢印キー入力受付時の処理
	 */
	@Override
	protected void onPressKeyRight() {
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
	 * キー入力受付後の定期処理
	 */
	@Override
	protected void periodicOpAfterInput() {
		// no operation
	}
	
	/**
	 * ループ処理終了直前の処理
	 */
	@ Override
	protected void finalOp() {
		// no operation
	}

	// ==================================================
	//  ペイント処理
	// ==================================================
	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, MainFrame.WINDOW_WIDTH, MainFrame.WINDOW_HEIGHT, this);
		super.paint(g);
	}
}
