package gamesystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

import main.PanelController;

public class SubPanel extends BasePanel {
	
	public static final int CONTROL_NO_SELECTED = -1;
	public static final int CONTROL_RETURN = -2;
	
	JButton btnBack, btnControls[];
	private int mNumOfControls;
	private int mSelectedControl;
	
	public SubPanel(PanelController pc) {
		super(pc);
		
		this.setBackground(Color.white);
		this.setBorder(new LineBorder(Color.black, 2, true));
		
		mNumOfControls = 0;
		mSelectedControl = CONTROL_NO_SELECTED;
		
		// もどるボタン
		btnBack = new JButton("もどる");
		btnBack.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnBack.setBounds(10, 10, 140, 60);
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mLoopState = LOOP_EXIT;
				mSelectedControl = CONTROL_RETURN;
			}
		});
		this.add(btnBack);

		this.setVisible(false);
	}
	
	/**
	 * 各項目名を設定し，ボタンを生成する
	 * !! 項目数の上限はないが，画面上の配置によって，一部画面外の描写になる可能性を考慮する必要あり．
	 * !! showPanel()の前にコールすること．
	 * @param controlNames controlNames[0] => btnControls[0]の名前
	 * @throws IllegalArgumentException contorlNamesがnullのとき
	 */
	public void setControlNames(String[] controlNames) throws IllegalArgumentException {
		int i;
		
		if (Objects.equals(controlNames, null)) {
			throw new IllegalArgumentException();
		}
		
		resetCursorArr();
		mNumOfControls = controlNames.length;
		btnControls = new JButton[mNumOfControls];
		// パネルのサイズ確定
		this.setSize(160, 10 + 60*(mNumOfControls + 1));
		// 各項目のボタン配置
		for (i=0; i<mNumOfControls; i++) {
			btnControls[i] = new JButton(controlNames[i]);
			btnControls[i].setFont(new Font("PixelMplus10",Font.PLAIN,13));
			btnControls[i].setBorderPainted(false);
			btnControls[i].setBounds(10, 10 + 60*i, 140, 50);
			btnControls[i].addActionListener(new ActionListener(){
				private int mBtnNum;
				
				public ActionListener setBtnNum(int btnNum) {
					this.mBtnNum = btnNum;
					return this;
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					mSelectedControl = this.mBtnNum;
				}
			}.setBtnNum(i));
			this.add(btnControls[i]);
			addToCursorArr(btnControls[i]);
		}
		
		// もどるボタンを再配置
		btnBack.setBounds(10, 10 + 60*i, 140, 60);
	}
	
	/**
	 * パネルの可視化
	 * !! setControlNames()がコールされていない場合は可視化しない
	 */
	@Override
	public void showPanel() {
		if (mNumOfControls == 0) {
			return;
		}
		
		this.setVisible(true);
		this.requestFocus(true);
	}
	
	// ==================================================
	//  ゲッター
	// ==================================================
	public int getSelectedControl() {
		int returnVal = mSelectedControl;
		
		mSelectedControl = CONTROL_NO_SELECTED;
		return returnVal;
	}
	
	// ==================================================
	//  ループ内処理
	// ==================================================
	/**
	 * キー入力受付前の定期処理
	 */
	@Override
	protected void periodicOpBeforeInput() {
		if (mSelectedControl != CONTROL_NO_SELECTED) {
			// ループを一旦離脱 -> 呼び出し元で選択した項目を取得して処理すること
			stop();
		}
	}

	/**
	 * 上矢印キー入力受付時の処理
	 */
	@Override
	protected void onPressKeyUp() {
		moveCursor(CURSOR_INDEX_MINUS);
	}

	/**
	 * 下矢印キー入力受付時の処理
	 */
	@Override
	protected void onPressKeyDown() {
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
	 * スペースキー入力受付時の処理
	 */
	@Override
	protected void onPressKeySpace() {
		btnBack.doClick();
	}

	/**
	 * キー入力受付後の定期処理
	 */
	@Override
	protected void periodicOpAfterInput() {
		// no operation
	}
	
	/**
	 * ループ処理終了直前の処理(最後の1回のみ)
	 */
	@Override
	protected void finalOp() {
		// no operation
	}

	// ==================================================
	//  その他
	// ==================================================
	/**
	 * コントロールボックス内の全ボタンの有効化/無効化
	 * @param b true -> 有効化 / false -> 無効化
	 */
	public void setEnabledInner(boolean b) {
		for (int i=0; i<mNumOfControls; i++) {
			btnControls[i].setEnabled(b);
		}
		
		btnBack.setEnabled(b);
	}

}
