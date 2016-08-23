package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class BasePanel extends JPanel implements KeyListener {
	// 定数
	// ループの優先度(ネストした場合の深さ)
	public static final int PRIORITY_UNDEF = -1;
	public static final int PRIORITY_FIRST = 0;
	public static final int PRIORITY_SECOND = 1;
	// ループの状態
	public static final boolean LOOP_RUNNING = false;
	public static final boolean LOOP_EXIT = true;
	// キー
	public static final int KEY_NO_TYPED = 0;
	public static final int KEY_UP = 1;
	public static final int KEY_DOWN = 2;
	public static final int KEY_LEFT = 3;
	public static final int KEY_RIGHT = 4;
	public static final int KEY_Z = 5;
	public static final int KEY_X = 6;
	public static final int KEY_SPACE = 7;
	
	// 変数
	protected PanelController mController;
	
	// ~~~ 今回，未実装部分 ~~~
	// ループの優先度(ループがネストしている場合の深さ)を表す変数．
	// 値が大きいほど，ループのネストが深いことを意味する．
	// あるループを停止(離脱)させた場合，それより深いループはすべて停止(離脱)する．
	// (以下，優先度とよぶ)
	protected int mPriority;
	// ~~~~~~
	
	protected static boolean loopState;
	protected static int keyState;
	
	// ==================================================
	//  コンストラクタ
	// ==================================================
	/**
	 * @deprecated コンストラクタ
	 * @param controller パネルコントローラ
	 */
	@Deprecated
	public BasePanel(PanelController controller) {
		super();
		
		this.mController = controller;
		this.mPriority = PRIORITY_UNDEF;
		
		keyState = KEY_NO_TYPED;
		loopState = LOOP_EXIT;
		
		this.setLayout(null);
		this.setSize(PanelController.PANEL_WIDTH, PanelController.PANEL_HEIGHT);
		this.addKeyListener(this);
		this.setFocusable(true);
	}
	
	/**
	 * コンストラクタ
	 * @param controller
	 * @param priority 優先度(以降，変更不可)
	 * @throws IllegalArgumentException priorityの値が1以下のとき
	 */
	public BasePanel(PanelController controller, int priority) throws IllegalArgumentException {
		this(controller);
//		super();
//		
//		this.mController = controller;
		if (priority <= PRIORITY_SECOND && priority != PRIORITY_UNDEF) {
			this.mPriority = PRIORITY_UNDEF;
			throw new IllegalArgumentException(
					"Priority is not minus, and 0(PRIORITY_FIRST), 1(PRIORITY_SECOND) are reserved.");
		} else {
			this.mPriority = priority;
		}
		
//		keyState = KEY_NO_TYPED;
//		loopState = LOOP_EXIT;
//		
//		this.setLayout(null);
//		this.setSize(PanelController.PANEL_WIDTH, PanelController.PANEL_HEIGHT);
//		this.addKeyListener(this);
//		this.setFocusable(true);
	}
	
	/**
	 * コンストラクタ
	 * @param controller
	 * @param priority
	 * @param width パネルの幅
	 * @param height パネルの高さ
	 * @throws IllegalArgumentException priorityの値が1以下のとき
	 */
	public BasePanel(PanelController controller, int priority, int width, int height)
			throws IllegalArgumentException {
		this(controller, priority);
		
		this.setSize(width, height);
	}
	
	// ==================================================
	//  ゲッター
	// ==================================================
	/**
	 * 優先度の取得
	 * @return 優先度
	 */
	public int getPriority() {
		return this.mPriority;
	}
	
	// ==================================================
	//  基本処理
	// ==================================================
	/**
	 * パネルの可視化
	 */
	public void showPanel() {
		this.setVisible(true);
		this.requestFocus(true);
	}
	
	/**
	 * 入力待機，処理ループ
	 */
	public void loop() {
		keyState = KEY_NO_TYPED;
		loopState = LOOP_RUNNING;
		
		while(true) {
//			if (loopState != LOOP_RUNNING) {
//				// TODO: priorityに応じたループ停止処理(今回，未実装部分)
//				break;
//			}
			if (loopState != LOOP_RUNNING || Main.exitFlag != Main.RUNNING) {
				break;
			}
			
			this.periodicOpBeforeInput();
			
			switch(keyState) {
			case KEY_UP:
				this.onPressKeyUp();
				break;
				
			case KEY_DOWN:
				this.onPressKeyDown();
				break;
				
			case KEY_LEFT:
				this.onPressKeyLeft();
				break;
				
			case KEY_RIGHT:
				this.onPressKeyRight();
				break;
				
			case KEY_Z:
				this.onPressKeyZ();
				break;
				
			case KEY_X:
				this.onPressKeyX();
				break;
				
			case KEY_SPACE:
				this.onPressKeySpace();
				break;
				
			default:
				break;
			}
			keyState = KEY_NO_TYPED;
			
			this.periodicOpAfterInput();
			
			try {
				Thread.sleep(33);  // about 30 fps
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * ループ停止処理
	 */
	public void stop() {
		loopState = LOOP_EXIT;
	}
	
	/**
	 * ループ停止処理(与えられた優先度に応じて)
	 * @param priority 優先度
	 */
	public void stop(int priority) {
		if (this.mPriority > priority || this.mPriority == PRIORITY_UNDEF) {
			loopState = LOOP_EXIT;
		}
	}
	
	// ==================================================
	//  ループ内処理
	//  (ここを書き換えてください)
	// ==================================================
	/**
	 * キー入力受付前の定期処理
	 */
	protected void periodicOpBeforeInput() {
		// No operation
	}
	
	/**
	 * 上矢印キー入力受付時の処理
	 */
	protected void onPressKeyUp() {
		// No operation
	}
	
	/**
	 * 下矢印キー入力受付時の処理
	 */
	protected void onPressKeyDown() {
		// No operation
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
		// No operation
	}
	
	/**
	 * Xキー入力受付時の処理
	 */
	protected void onPressKeyX() {
		// No operation
	}
	
	/**
	 * スペースキー入力受付時の処理
	 */
	protected void onPressKeySpace() {
		// No operation
	}
	
	/**
	 * キー入力受付後の定期処理
	 */
	protected void periodicOpAfterInput() {
		this.repaint();
	}

	// ==================================================
	//  キーリスナー
	// ==================================================
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key;

		key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_UP:
			keyState = KEY_UP;
			break;
		case KeyEvent.VK_DOWN:
			keyState = KEY_DOWN;
			break;
		case KeyEvent.VK_LEFT:
			keyState = KEY_LEFT;
			break;
		case KeyEvent.VK_RIGHT:
			keyState = KEY_RIGHT;
			break;
		case KeyEvent.VK_Z:
			keyState = KEY_Z;
			break;
		case KeyEvent.VK_X:
			keyState = KEY_X;
			break;
		case KeyEvent.VK_SPACE:
			keyState = KEY_SPACE;
			break;
		case KeyEvent.VK_TAB:
			// tabキーによるフォーカス移動を防止
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
