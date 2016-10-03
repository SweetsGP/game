package gamesystem;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import main.Main;
import main.PanelController;

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
	// カーソル位置
	public static final int CURSOR_UNLOC = -1;
	private int CURSOR_LOC_MAX = 0;
	// カーソル移動方向
	public static final boolean CURSOR_INDEX_PLUS = true;
	public static final boolean CURSOR_INDEX_MINUS = false;
	
	// 変数
	protected PanelController mController;
	
	// ~~~ 今回，未実装部分 ~~~
	// ループの優先度(ループがネストしている場合の深さ)を表す変数．
	// 値が大きいほど，ループのネストが深いことを意味する．
	// あるループを停止(離脱)させた場合，それより深いループはすべて停止(離脱)する．
	// (以下，優先度とよぶ)
	protected int mPriority;
	// ~~~~~~
	
	// ループ処理状態
	protected boolean mLoopState;
	// キー入力状態
	protected int mKeyState;
	
	// カーソル操作対象コンポーネント格納用配列
	protected ArrayList<JButton> mCursorArr;
	// カーソル位置
	protected int mCursorLoc;
	
	// ==================================================
	//  コンストラクタ
	// ==================================================
	/**
	 * 埋め込み用
	 */
	public BasePanel() {
		super();
		
		this.mPriority = PRIORITY_UNDEF;
		
		this.mKeyState = KEY_NO_TYPED;
		this.mLoopState = LOOP_EXIT;
		
		this.mCursorArr = null;
		this.mCursorLoc = CURSOR_UNLOC;
		
		this.setLayout(null);
		this.setSize(PanelController.PANEL_WIDTH, PanelController.PANEL_HEIGHT);
		this.addKeyListener(this);
		this.setFocusable(true);
	}
	
	/**
	 * @deprecated コンストラクタ
	 * @param controller パネルコントローラ
	 */
	@Deprecated
	public BasePanel(PanelController controller) {
		this();
		
		this.mController = controller;
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
		this.mKeyState = KEY_NO_TYPED;
		this.mLoopState = LOOP_RUNNING;
		
		this. firstOp();
		
		while(true) {
//			if (loopState != LOOP_RUNNING) {
//				// TODO: priorityに応じたループ停止処理(今回，未実装部分)
//				break;
//			}
			if (this.mLoopState != LOOP_RUNNING || Main.exitFlag != Main.RUNNING) {
				break;
			}
			
			this.periodicOpBeforeInput();
			
			switch(this.mKeyState) {
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
			this.mKeyState = KEY_NO_TYPED;
			
			this.periodicOpAfterInput();
			
			try {
				Thread.sleep(33);  // about 30 fps
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		this.finalOp();
	}
	
	/**
	 * ループ停止処理
	 */
	public void stop() {
		this.mLoopState = LOOP_EXIT;
	}
	
	/**
	 * ループ停止処理(与えられた優先度に応じて)
	 * @param priority 優先度
	 */
	public void stop(int priority) {
		if (this.mPriority > priority || this.mPriority == PRIORITY_UNDEF) {
			this.mLoopState = LOOP_EXIT;
		}
	}
	
	// ==================================================
	//  ループ内処理
	//  (ここを書き換えてください)
	// ==================================================
	/**
	 * ループ処理開始直後の処理(はじめの1回のみ)
	 */
	protected void firstOp() {
		// no operation
	}
	
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
	
	/**
	 * ループ処理終了直前の処理(最後の1回のみ)
	 */
	protected void finalOp() {
		this.requestFocus(false);
		this.setVisible(false);
	}
	
	// ==================================================
	//  カーソル操作
	// ==================================================
	/**
	 * カーソル操作の対象となるコンポーネント(ボタン)を追加します
	 * (追加した(本メソッドを呼び出した)順にカーソルが移動することになります)
	 * @param btn カーソル操作の対象にしたいボタン
	 */
	protected void addToCursorArr(JButton btn) {
		if (Objects.equals(this.mCursorArr, null)) {
			this.mCursorArr = new ArrayList<JButton>();
		}
		
		this.mCursorArr.add(btn);
		this.CURSOR_LOC_MAX++;
	}
	
	/**
	 * カーソル操作対象配列をクリアします
	 */
	protected void resetCursorArr() {
		if (!Objects.equals(this.mCursorArr, null)) {
			this.mCursorArr.clear();
			this.mCursorLoc = CURSOR_UNLOC;
			this.CURSOR_LOC_MAX = 0;
		}
	}
	
	/**
	 * 表示中のカーソルを非表示にします
	 */
	protected void deleteCursor() {
		JButton btn;
		
		if (this.mCursorLoc != CURSOR_UNLOC) {
			btn = this.mCursorArr.get(this.mCursorLoc);
			btn.setBorderPainted(false);
		}
		
		this.repaint();
	}
	
	/**
	 * 引数で与えられた位置のコンポーネントにカーソルを表示します
	 * @param cursor 位置
	 */
	protected void showCursor(int cursor) {
		JButton btn;
		
		this.mCursorLoc = cursor;
		btn = this.mCursorArr.get(this.mCursorLoc);
		btn.setBorderPainted(true);
		btn.setBorder(new LineBorder(Color.red, 2, true));
		
		this.repaint();
	}
	
	/**
	 * 与えられた方向にカーソルを1だけ移動します
	 * @param direction 
	 *   CURSOR_INDEX_PLUS  -> カーソル操作対象コンポーネント格納用の配列において，インデックスがプラスとなる向き
	 *     (後に追加したコンポーネントにカーソルが向かう)
	 *   CURSOR_INDEX_MINUS -> 同様に，インデックスがマイナスとなる向き(先に追加したコンポーネントにカーソルが向かう)
	 */
	protected void moveCursor(boolean direction) {
		this.deleteCursor();
		
		if (direction == CURSOR_INDEX_PLUS) {
			if (this.mCursorLoc < this.CURSOR_LOC_MAX - 1) {
				this.mCursorLoc++;
			}
		} else {
			if (this.mCursorLoc > 0) {
				this.mCursorLoc--;
			}
		}
		
		if (this.mCursorLoc != CURSOR_UNLOC) {
			this.showCursor(this.mCursorLoc);
		}
	}
	
	/**
	 * 現在，カーソルがあるコンポーネント(ボタン)をクリックします
	 */
	protected void clickOnCursor() {
		JButton btn;
		
		if (this.mCursorLoc != CURSOR_UNLOC) {
			btn = this.mCursorArr.get(this.mCursorLoc);
			btn.doClick();
		}
		
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
			this.mKeyState = KEY_UP;
			break;
		case KeyEvent.VK_DOWN:
			this.mKeyState = KEY_DOWN;
			break;
		case KeyEvent.VK_LEFT:
			this.mKeyState = KEY_LEFT;
			break;
		case KeyEvent.VK_RIGHT:
			this.mKeyState = KEY_RIGHT;
			break;
		case KeyEvent.VK_Z:
			this.mKeyState = KEY_Z;
			break;
		case KeyEvent.VK_X:
			this.mKeyState = KEY_X;
			break;
		case KeyEvent.VK_SPACE:
			this.mKeyState = KEY_SPACE;
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
