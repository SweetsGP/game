package gamesystem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import Battle.Battlemain;
import main.DataBase;
import main.Env;
import main.FilePath;
import main.Main;
import main.PanelController;

public class HasMonstersPanel extends JPanel implements KeyListener {
	public static final int RUNNING_HMP_LOOP = 0;
	public static final int EXIT_HMP_LOOP = 1;

	public static final int MONSTER_NO_SELECT = -1;

	public static final int ACTION_NO_SELECT = -1;
	public static final int ACTION_SHOW_DETAIL = 0;
	public static final int ACTION_CHANGE_ORDER = 1;
	public static final int ACTION_BACK = -2;

	public static int exitHMPLoopFlag = RUNNING_HMP_LOOP;

	protected boolean hasCb = true;
	protected int mSelected = MONSTER_NO_SELECT;
	protected int cbSelected = ACTION_NO_SELECT;

	protected boolean changeOrderFlag = false;
	protected int changeOrderMonsterDest = MONSTER_NO_SELECT;

	protected PanelController controller;
	protected ArrayList<HashMap<String, String>> hasMonstersInfo;
	protected int hasMonstersNum;

	protected JButton btnMonster1 = null, btnMonster2 = null, btnMonster3 = null,
			btnMonster4 = null, btnMonster5 = null, btnMonster6 = null;
	protected JButton[] btnMonsters = {btnMonster1, btnMonster2, btnMonster3,
			btnMonster4, btnMonster5, btnMonster6};
	protected JButton btnBack;

	protected ImageIcon iconMonster1 = null, iconMonster2 = null, iconMonster3 = null,
			iconMonster4 = null, iconMonster5 = null, iconMonster6 = null;
	protected ImageIcon[] iconMonsters = {iconMonster1, iconMonster2, iconMonster3,
			iconMonster4, iconMonster5, iconMonster6};

	protected ControlBox cbAction;
	protected String[] controlList;

	MenuEvent me;
	private static int key_state = 0;

	public HasMonstersPanel() {}
	public HasMonstersPanel(PanelController c) {
		controller = c;
		me = new MenuEvent();

		this.setLayout(null);
		this.setSize(600, 540);
		this.setOpaque(false);

		this.setFocusable(true);
		this.addKeyListener(this);

		controlList = new String[2];
		controlList[0] = "つよさをみる";
		controlList[1] = "ならびかえ";
		cbAction = new ControlBox(controller, 2);
		cbAction.setControlNames(controlList);
		cbAction.setLocation(310, 230);
		cbAction.setVisible(false);
		this.add(cbAction);

		btnBack = new JButton("もどる");
		btnBack.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnBack.setBounds(150, 470, 140, 60);
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				onClickBtnBack();
			}
		});
		this.add(btnBack);

		this.setVisible(false);
	}

	/**
	 * @deprecated 引数なしのほうをつかってください.
	 * てもちモンスター情報表示用のパネルを表示
	 * @param hmi てもちモンスターの情報を格納した配列
	 */
	@Deprecated
	public void showHasMonsters(ArrayList<HashMap<String, String>> hmi) {
		HashMap<String, String> monsterInfo = null;

		hasMonstersInfo = hmi;
		hasMonstersNum = hmi.size();

		// 引数から得た手持ちモンスターの情報から各モンスターのボタンを生成
		for (int i = 0; i < hasMonstersNum; i++) {
			// リロードの場合には作成済みのボタンを破棄
			// -> nullチェックして、nullでなければ、パネルからボタンをremoveする。
			if(!Objects.equals(btnMonsters[i], null)) {
				this.remove(btnMonsters[i]);
			}

			monsterInfo = hasMonstersInfo.get(i);

			iconMonsters[i] = new ImageIcon(FilePath.monstersDirPath + monsterInfo.get("img"));
			btnMonsters[i] = new JButton(
					"<html>" + monsterInfo.get("name") + "  " + monsterInfo.get("state")
					+ "<br/>HP: " + monsterInfo.get("hp") + " / " + monsterInfo.get("maxhp") + "</html>",
					iconMonsters[i]);
			btnMonsters[i].setFont(new Font("PixelMplus10",Font.PLAIN,13));
			btnMonsters[i].setHorizontalAlignment(JButton.LEFT);
			btnMonsters[i].setBorderPainted(false);
			btnMonsters[i].setBounds(10, 5 + 75*i, 280, 70);
			btnMonsters[i].addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onClickBtnMonster(e);
				}
			});
			this.add(btnMonsters[i]);
			
		}
		
		// 表示更新時、カーソル表示も更新が必要な場合は一緒に更新
		if (mSelected != MONSTER_NO_SELECT) {
			setCursor(mSelected);
		}

		// 表示
		this.setVisible(true);
		this.requestFocus(true);
	}

	/**
	 * キャラクタが保持しているモンスターの一覧を表示する
	 */
	public void showHasMonsters() {
		showHasMonsters(me.getHasMonstersInfo());
	}

	/**
	 * 各モンスターのボタンをクリックしたときの処理
	 * @param e
	 */
	public void onClickBtnMonster(ActionEvent e) {
		for (int i = 0; i < 6; i++) {
			if (e.getSource() == btnMonsters[i]) {
				if (changeOrderFlag == true) {
					changeOrderMonsterDest = i;
					setCursor(i);
				}else{
					mSelected = i;
					setCursor(i);
				}
				break;
			}
		}
	}

	/**
	 * 
	 * @param btn
	 */
	public void lockChangeOrderMonsterBtn(int btn) {
		btnMonsters[btn].setEnabled(false);
	}

	/**
	 * 
	 * @param btn
	 */
	public void unlockChangeOrderMonsterBtn(int btn) {
		btnMonsters[btn].setEnabled(true);
	}

	/**
	 * もどるボタンをクリックしたときの処理　
	 */
	public void onClickBtnBack() {
		exitHMPLoopFlag = EXIT_HMP_LOOP;
	}

	/**
	 * 各モンスターの項目に相当するボタンの有効化/無効化
	 * @param b true -> 有効化 / false -> 無効化
	 */
	public void setEnabledMonsters(boolean b) {
		for (int i = 0; i < 6; i++) {
			if(!Objects.equals(btnMonsters[i], null)) {
				btnMonsters[i].setEnabled(b);
			}
		}
		btnBack.setEnabled(b);
	}

	/**
	 * コントロールボックス内部のコンポーネントの有効化/無効化
	 * @param b true -> 有効化 / false -> 無効化
	 */
	public void setEnabledControlBox(boolean b) {
		cbAction.setEnabledInner(b);
	}

	/**
	 * 入力待機用ループ
	 */
	public void loop() {
		int cursorLocation = MONSTER_NO_SELECT;
		
		key_state = 0;
		exitHMPLoopFlag = RUNNING_HMP_LOOP;

		while(true) {
			if (Main.exitFlag != Main.RUNNING || exitHMPLoopFlag != RUNNING_HMP_LOOP) {
				break;
			}

			// モンスター選択時 -> コントロールボックスを表示し、次の処理の選択肢を表示
			if (mSelected != MONSTER_NO_SELECT) {
				setEnabledMonsters(false);
				cbAction.setVisible(true);
				
				cbLoop();
				
				cbAction.setVisible(false);
				setEnabledMonsters(true);
			}
			mSelected = MONSTER_NO_SELECT;

			// キー入力受付
			cursorLocation = checkCursorOperation(cursorLocation);

			// カーソル表示 (暫定的に青枠表示)
			if (cursorLocation != MONSTER_NO_SELECT) {
				setCursor(cursorLocation);
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		this.requestFocus(false);
		this.setVisible(false);
	}
	
	/**
	 * カーソル操作受付
	 * @param nowCursor 現在のカーソル位置
	 * @return 次のカーソル位置
	 */
	public int checkCursorOperation(int nowCursor) {
		int nextCursor = nowCursor;
		
		switch (key_state) {
		case 1:  // UP
			if (nextCursor > 0) {
				nextCursor--;
			}
			break;
		case 2:  // DOWN
			if (nextCursor < hasMonstersNum - 1) {
				nextCursor++;
			}
			break;
		case 3:  // LEFT
			// no operation
			break;
		case 4:  // RIRHT
			// no operation
			break;
		case 5:  // Z
			if (nextCursor != MONSTER_NO_SELECT) {
				btnMonsters[nextCursor].doClick();
			}
			break;
		case 6:  // X
			// no operation
			break;
		case 7:  // スペースキー
			this.requestFocus(false);
			this.setVisible(false);
			exitHMPLoopFlag = EXIT_HMP_LOOP;
			break;
		default:
			break;
		}
		key_state = 0;
		
		return nextCursor;
	}
	
	/**
	 * 与えられた番号の位置にカーソル表示をセットする
	 * @param cursorLocation カーソル表示を設置したい項目の位置番号
	 */
	public void setCursor(int cursorLocation) {
		for (int i = 0; i < hasMonstersNum; i++) {
			if (i == cursorLocation) {
				btnMonsters[i].setBorderPainted(true);
				if (changeOrderFlag) {
					if (i != mSelected) {
						btnMonsters[i].setBorder(new LineBorder(Color.RED, 2, true));
					} else {
						btnMonsters[i].setBorder(new LineBorder(Color.BLUE, 2, true));
					}
				} else {
					btnMonsters[i].setBorder(new LineBorder(Color.BLUE, 2, true));
				}
			} else {
				if (!(changeOrderFlag && i == mSelected)) {
					btnMonsters[i].setBorderPainted(false);
				}
			}
		}
		this.repaint();
	}
	
	/**
	 * コントロールボックス表示時の入力待機処理
	 */
	public void cbLoop() {
		int cbCursorLocation = ACTION_NO_SELECT;
		int changeOrderDestCursor = MONSTER_NO_SELECT;
		
		HasMonsterDetailPanel hmdp;
		
		key_state = 0;
		
		CONTROL_LOOP:
			while(true) {
				if (Main.exitFlag != Main.RUNNING || exitHMPLoopFlag != RUNNING_HMP_LOOP) {
					break;
				}

				cbSelected = cbAction.getSource();
				switch(cbSelected) {
				case ACTION_SHOW_DETAIL:
					this.requestFocus(false);
					controller.showHasMonsterDetailPanel(hasMonstersInfo.get(mSelected));
					hmdp = (HasMonsterDetailPanel )controller.getPanelInstance(PanelController.HAS_MONSTER_DETAIL);
					hmdp.loop();
					this.requestFocus(true);
					break;

				case ACTION_CHANGE_ORDER:
					if (PanelController.state == PanelController.STATE_BATTLE) {
						// バトル中の場合は選択したモンスターを先頭に
						try {
							DataBase.wExecuteUpdate("update testhasmonsters set orderNum = 7 where orderNum = "
									+ Integer.toString(mSelected + 1));
							DataBase.wExecuteUpdate("update testhasmonsters set orderNum = "+ Integer.toString(mSelected + 1) +" where orderNum = 1");
							DataBase.wExecuteUpdate("update testhasmonsters set orderNum = 1 where orderNum = 7");
						} catch(SQLException sqle) {
							sqle.printStackTrace();

						}

						// test code -> バトルへのモンスター情報&モンスター入れ替えエフェクト通知
						showHasMonsters();
						repaint();
						////

						exitHMPLoopFlag = EXIT_HMP_LOOP;
						Battlemain.changeMonsterFlag = true;

						break CONTROL_LOOP;

					} else {
						// マップ画面では、モンスターの順番を任意に入れ替え可能
						changeOrderFlag = true;
						setEnabledControlBox(false);
						setEnabledMonsters(true);
						lockChangeOrderMonsterBtn(mSelected);
						while(true) {
							if (Main.exitFlag != Main.RUNNING || exitHMPLoopFlag != RUNNING_HMP_LOOP) {
								break;
							}

							if (changeOrderMonsterDest != MONSTER_NO_SELECT) {
								try {
									DataBase.wExecuteUpdate("update testhasmonsters set orderNum = 7 where orderNum = "
											+ Integer.toString(mSelected + 1));
									DataBase.wExecuteUpdate("update testhasmonsters set orderNum = "+ Integer.toString(mSelected + 1) +" where orderNum = "
											+ Integer.toString(changeOrderMonsterDest + 1));
									DataBase.wExecuteUpdate("update testhasmonsters set orderNum = " + Integer.toString(changeOrderMonsterDest + 1)
									+ " where orderNum = 7");
								} catch(SQLException sqle) {
									sqle.printStackTrace();

								}
								unlockChangeOrderMonsterBtn(mSelected);
								mSelected = changeOrderMonsterDest;
								changeOrderMonsterDest = MONSTER_NO_SELECT;
								showHasMonsters();
								repaint();
								break;
							}
							
							changeOrderDestCursor = checkCursorOperation(changeOrderDestCursor);
							// カーソル表示 (暫定的に赤枠表示)
							if (changeOrderDestCursor != MONSTER_NO_SELECT) {
								setCursor(changeOrderDestCursor);
							}

							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

						changeOrderFlag = false;
						setEnabledMonsters(false);
						setEnabledControlBox(true);
					}
					break;

				case ACTION_BACK:
					break CONTROL_LOOP;

				default:
				}
				cbSelected = ACTION_NO_SELECT;
				
				// キー入力受付
				switch (key_state) {
				case 1:  // UP
					if (cbCursorLocation > 0) {
						cbCursorLocation--;
					}
					break;
				case 2:  // DOWN
					if (cbCursorLocation < ACTION_CHANGE_ORDER) {
						cbCursorLocation++;
					}
					break;
				case 3:  // LEFT
					// no operation
					break;
				case 4:  // RIGHT
					// no operation
					break;
				case 5:  // Z
					if (cbCursorLocation != ACTION_NO_SELECT || cbCursorLocation != ACTION_BACK) {
						cbAction.emurateClick(cbCursorLocation);
					}
					break;
				case 6:  // X
					// no operation
					break;
				case 7:  // スペースキー
					key_state = 0;
					break CONTROL_LOOP;
				default:
					break;
				}
				key_state = 0;

				// カーソル表示 (暫定的に青枠表示)
				if (cbCursorLocation != ACTION_NO_SELECT || cbCursorLocation != ACTION_BACK) {
					cbAction.setCursor(cbCursorLocation);
				}

				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
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

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		if (hasCb) {
			g2.setColor(Color.white);
			g2.fillRect(0, 0, 300, 540);
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke(3.0f));
			g2.drawRect(0, 0, 300, 540);
		}

		super.paint(g);
	}

}
