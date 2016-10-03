package gamesystem;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import Battle.Battlemain;
import gameutil.DBAccess;
import main.DataBase;
import main.FilePath;
import main.PanelController;
import map.map_main;

public class HasMonstersPanel extends ItemsPanel {
	// 暫定
	public static final int RUNNING_HMP_LOOP = 0;
	public static final int EXIT_HMP_LOOP = 1;
	
	public static int exitHMPLoopFlag = RUNNING_HMP_LOOP;
	// =====
	
	public static final int ACTION_NO_SELECT = -1;
	public static final int ACTION_SHOW_DETAIL = 0;
	public static final int ACTION_CHANGE_ORDER = 1;
	public static final int ACTION_BACK = -2;
	
	DBAccess dba;
	
	protected ArrayList<HashMap<String, String>> mHasMonsters;
	
	SubPanel subPanel;
	protected String[] mControlList;
	
	private boolean mIsDisplaySubPanel, mIsChangeOrder;
	private int mSrc;
	
	public HasMonstersPanel() {
		super();
		
		dba = new DBAccess();
		
		mIsDisplaySubPanel = false;
		mIsChangeOrder = false;
	}
	
	public HasMonstersPanel(PanelController pc, SubPanel sb) {
		this();
		
		this.mController = pc;
		
		mControlList = new String[2];
		mControlList[0] = "つよさをみる";
		mControlList[1] = "ならびかえ";
		subPanel = sb;
		subPanel.setControlNames(mControlList);
	}
	
	/**
	 * パネルの可視化
	 */
	@Override
	public void showPanel() {
		HashMap<String, String> monsterInfo = null;
		
		mHasMonsters = dba.getHasMonstersInfo();

		// 更新(2回目以降のアクセス)なら、以前のボタンを除去して、アイテムのリスト表示領域のサイズをリセット
		if (!Objects.equals(btnItems, null)) {
			for(int i=0; i<btnItems.length; i++) {
				if (!Objects.equals(btnItems[i], null)) {
					pItemList.remove(btnItems[i]);
				}
			}

			pItemList.setPreferredSize(new Dimension(280, 450));
			resetCursorArr();
		}

		mNumOfItems = mHasMonsters.size();
		btnItems = new JButton[mNumOfItems];
		ImageIcon[] iconItems = new ImageIcon[mNumOfItems];
		
		// 引数から得た手持ちモンスターの情報から各モンスターのボタンを生成
		for (int i=0; i<mNumOfItems; i++) {
			if (i == 6) {
				// panelサイズ変更
				pItemList.setPreferredSize(new Dimension(280, 450 + 75*(mNumOfItems - i)));
				revalidate();
			}
			
			monsterInfo = mHasMonsters.get(i);

			iconItems[i] = new ImageIcon(FilePath.monstersDirPath + mHasMonsters.get(i).get("img"));

			btnItems[i] = new JButton("<html>" + monsterInfo.get("name") + "  " + monsterInfo.get("state")
				+ "<br/>HP: " + monsterInfo.get("hp") + " / " + monsterInfo.get("maxhp") + "</html>", iconItems[i]);
			btnItems[i].setFont(new Font("PixelMplus10",Font.PLAIN,13));
			btnItems[i].setHorizontalAlignment(JButton.LEFT);
			btnItems[i].setBorderPainted(false);
			btnItems[i].setBounds(0, 75*i, 260, 70);
			btnItems[i].addActionListener(new ActionListener(){
				private int mBtnNum;
				
				public ActionListener setBtnNum(int btnNum) {
					this.mBtnNum = btnNum;
					return this;
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					mSelectedItem = this.mBtnNum;
				}
			}.setBtnNum(i));
			pItemList.add(btnItems[i]);
			addToCursorArr(btnItems[i]);
		}
		
		super.showPanel();
	}
	
	// ==================================================
	//  ループ内処理
	// ==================================================
	/**
	 * キー入力受付前の定期処理
	 */
	@Override
	protected void periodicOpBeforeInput() {
		int selectedSubPanelControl;
		
		// 暫定
		if (exitHMPLoopFlag != RUNNING_HMP_LOOP) {
			stop();
		}
		// =====
		
		if (mIsDisplaySubPanel == true) {
			// サブパネル各項目処理チェック
			selectedSubPanelControl = subPanel.getSelectedControl();
			if (selectedSubPanelControl != SubPanel.CONTROL_NO_SELECTED) {
				// サブパネル各項目処理
				actionSubPanelControlSelected(selectedSubPanelControl);
			} else {
				// サブパネルのルーチンに戻る
				subPanel.loop();
			}
			
		} else {
			// 本パネルの各項目処理チェック
			super.periodicOpBeforeInput();
		}
	}
	
	/**
	 * 上矢印キー入力受付時の処理
	 */
	@Override
	protected void onPressKeyUp() {
		if (mIsDisplaySubPanel == false) {
			moveCursor(CURSOR_INDEX_MINUS);
		}
	}

	/**
	 * 下矢印キー入力受付時の処理
	 */
	@Override
	protected void onPressKeyDown() {
		if (mIsDisplaySubPanel == false) {
			moveCursor(CURSOR_INDEX_PLUS);
		}
	}

	/**
	 * Zキー入力受付時の処理
	 */
	@Override
	protected void onPressKeyZ() {
		if (mIsDisplaySubPanel == false) {
			clickOnCursor();
		}
	}

	/**
	 * スペースキー入力受付時の処理
	 */
	@Override
	protected void onPressKeySpace() {
		if (mIsDisplaySubPanel == false) {
			btnBack.doClick();
		}
	}
	
	/**
	 * ループ処理終了直前の処理(最後の1回のみ)
	 */
	@Override
	protected void finalOp() {
		// もどるボタンによるループ離脱の処理
		if (mIsChangeOrder == true) {
			// 順序入れ替え時 -> サブパネル表示状態に戻す
			subPanel.showPanel();
			mIsDisplaySubPanel = true;
			
			setEnabledInner(false);
		} else {
			// ↑以外 -> このパネルを非表示に
			this.requestFocus(false);
			this.setVisible(false);
		}
	}
	
	/**
	 * アイテム選択時の処理
	 * @param selectedItemIndex 選択されたアイテムのインデックス
	 */
	@Override
	public void actionItemSelected(int selectedItemIndex) {
		map_main mp;
		Battlemain bp;
		
		// サブパネル表示 or 順番入れ替え チェック -> 処理
		if (mIsChangeOrder == true) {
			// 順序入れ替え
			if (selectedItemIndex != mSrc) {
				try {
					DataBase.wExecuteUpdate("update testhasmonsters set orderNum = 7 where orderNum = "
							+ Integer.toString(selectedItemIndex + 1));
					DataBase.wExecuteUpdate("update testhasmonsters set orderNum = "+ Integer.toString(selectedItemIndex + 1) +" where orderNum = "
							+ Integer.toString(mSrc + 1));
					DataBase.wExecuteUpdate("update testhasmonsters set orderNum = " + Integer.toString(mSrc + 1)
					+ " where orderNum = 7");
				} catch(SQLException sqle) {
					sqle.printStackTrace();
				}
			}
			
			// 入れ替えに伴うカーソル位置の変更
			showPanel();
			showCursor(mSrc);
			
			// サブパネル表示
			subPanel.showPanel();
			mIsDisplaySubPanel = true;
			
			setEnabledInner(false);
			mIsChangeOrder = false;
			
		} else {
			setEnabledInner(false);
			mSrc = selectedItemIndex;
			
			// サブパネル表示
			if (PanelController.getNowCalledLoopNum() == PanelController.BATTLE_PANEL) {
				bp = (Battlemain )mController.getPanelInstance(PanelController.BATTLE_PANEL);
				bp.setSubPanelLocation(500, 70 + 75*selectedItemIndex);
			} else {
				mp = (map_main )mController.getPanelInstance(PanelController.MAP_PANEL);
				mp.setSubPanelLocation(500, 70 + 75*selectedItemIndex);
			}
			this.requestFocus(false);
			mIsDisplaySubPanel = true;
			
			subPanel.showPanel();
			subPanel.loop();
		}
	}
	
	/**
	 * サブパネルの項目選択時の処理
	 * @param selectedSubPanelControlIndex 選択されたサブパネルの項目のインデックス
	 */
	public void actionSubPanelControlSelected(int selectedSubPanelControlIndex) {
		HasMonsterDetailPanel hmdp;
		
		switch (selectedSubPanelControlIndex) {
		case ACTION_SHOW_DETAIL:
			this.requestFocus(false);
			mController.showHasMonsterDetailPanel(mHasMonsters.get(mSrc));
			PanelController.callLoop(PanelController.HAS_MONSTER_DETAIL_PANEL);
//			hmdp = (HasMonsterDetailPanel )mController.getPanelInstance(PanelController.HAS_MONSTER_DETAIL_PANEL);
//			hmdp.loop();

			// サブパネルのルーチンに戻る
			subPanel.requestFocus(true);
			subPanel.loop();
			break;

		case ACTION_CHANGE_ORDER:
			if (PanelController.getNowCalledLoopNum() == PanelController.BATTLE_PANEL) {
				// バトル中の場合は，選択したモンスターを先頭に
				try {
					DataBase.wExecuteUpdate("update testhasmonsters set orderNum = 7 where orderNum = "
							+ Integer.toString(mSrc + 1));
					DataBase.wExecuteUpdate("update testhasmonsters set orderNum = "+ Integer.toString(mSrc + 1) +" where orderNum = 1");
					DataBase.wExecuteUpdate("update testhasmonsters set orderNum = 1 where orderNum = 7");
				} catch(SQLException sqle) {
					sqle.printStackTrace();
				}

				// test code -> バトルへのモンスター情報&モンスター入れ替えエフェクト通知
				showPanel();
				repaint();
				////

				subPanel.stop();
				subPanel.setVisible(false);
				mIsDisplaySubPanel = false;

				setEnabledInner(true);
				this.requestFocus(true);
				stop();

				Battlemain.changeMonsterFlag = true;

			} else {
				// それ以外の場合は，入れ替え先のモンスターも選択
				mIsChangeOrder = true;

				subPanel.setVisible(false);
				mIsDisplaySubPanel = false;

				setEnabledInner(true);
				btnItems[mSrc].setEnabled(false);
				this.requestFocus(true);
			}
			break;

		case ACTION_BACK:
			subPanel.setVisible(false);
			mIsDisplaySubPanel = false;

			setEnabledInner(true);
			this.requestFocus(true);
			break;

		default:
			break;
		}
	}
	
	// ==================================================
	//  その他
	// ==================================================
	public void setEnabledInner(boolean b) {
		for (int i=0; i<mNumOfItems; i++) {
			btnItems[i].setEnabled(b);
		}
		btnBack.setEnabled(b);
	}
}
