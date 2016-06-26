package itemhandler;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Battle.Battlemain;
import gamesystem.HasItemsPanel;
import gamesystem.ItemDetailPanel;
import gamesystem.MenuEvent;
import main.DataBase;
import main.Main;
import main.PanelController;

class Kizugusuri extends ItemHandler {
	// キズぐすり
	protected String itemName = "キズぐすり";
	// ループ終了判定定数
	protected final int RUNNING_EFFECT_LOOP = 0;
	protected final int EXIT_EFFECT_LOOP = 1;
	
	// ループ終了判定フラグ
	protected int effectLoopFlag = RUNNING_EFFECT_LOOP;
	// 回復量
	protected int recovery = 50;
	// アイテムID
	protected int itemId = 1;
	
	// アイテム破棄許可フラグ
	protected boolean isItemThrowOk = false;
	
	protected ItemHandlingAreaPanel ihap;
	protected TargetMonstersPanel targetmp;
	
	protected JLabel lInst, lConfirm;
	protected JButton btnBack, btnThrowOk, btnThrowNg;
	
	Kizugusuri(ItemHandlingAreaPanel p) {
		ihap = p;
		
		// アイテム処理発動時
		lInst = new JLabel("使用するモンスターを選択してください。");
		lInst.setBounds(10, 10, 380, 30);
		lInst.setVisible(false);
		ihap.add(lInst);
		
		targetmp = new TargetMonstersPanel(ihap.controller);
		targetmp.setLocation(10, 40);
		targetmp.setVisible(false);
		ihap.add(targetmp);
		
		btnBack = new JButton("もどる");
		btnBack.setBounds(200, 530, 140, 60);
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				targetmp.onClickBtnBack();
				effectLoopFlag = EXIT_EFFECT_LOOP;
			}
		});
		btnBack.setVisible(false);
		ihap.add(btnBack);
		
		// アイテム破棄処理時
		lConfirm = new JLabel(itemName + "を1つ捨てます。よろしいですか。");
		lConfirm.setBounds(10, 150, 380, 30);
		lConfirm.setVisible(false);
		ihap.add(lConfirm);
		
		btnThrowOk = new JButton("はい");
		btnThrowOk.setBounds(50, 200, 140, 60);
		btnThrowOk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				isItemThrowOk = true;
			}
		});
		btnThrowOk.setVisible(false);
		ihap.add(btnThrowOk);
		
		btnThrowNg = new JButton("いいえ");
		btnThrowNg.setBounds(210, 200, 140, 60);
		btnThrowNg.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				effectLoopFlag = EXIT_EFFECT_LOOP;
			}
		});
		btnThrowNg.setVisible(false);
		ihap.add(btnThrowNg);
		
		// バトル中の使用OK
		itemFlag = ITEM_USE_ON_BATTLE_OK;
	}
	
	@Override
	int getItemFlag() {
		return itemFlag;
	}
	
	@Override
	void itemEffect() throws ItemHandlingFailedException {
		int selected = 0;
		MenuEvent menuEvent = new MenuEvent();
		ArrayList<HashMap<String, String>> hasMonstersInfo = menuEvent.getHasMonstersInfo();
		
		// アイテムの個数を取得
		int itemCount = 0;
		try {
			itemCount = Integer.parseInt(
					DataBase.wExecuteQuery("select count from testitems where itemId = "
							+ Integer.toString(itemId)).get(0).get("count"));
		} catch (SQLException sqle) {
			throw new ItemHandlingFailedException();
		}
		
		int maxHp, beforeHp, afterHp;
		
		effectLoopFlag = RUNNING_EFFECT_LOOP;
		
		targetmp.showHasMonsters(hasMonstersInfo);
		setEnabledOnUse(true);
		ihap.setVisible(true);
		ihap.requestFocus(true);
		ihap.repaint();
		
		// 操作受付/待機ループ
		while(true) {
			if (effectLoopFlag != RUNNING_EFFECT_LOOP || Main.exitFlag != Main.RUNNING) {
				break;
			}
			
			selected = targetmp.getSelectedMonster();
			if (selected != 0) {  // アイテムの使用対象が選択された場合
				// モンスターの最大HP取得
				maxHp = Integer.parseInt(hasMonstersInfo.get(selected-1).get("maxhp"));
				// モンスターの現在HP取得
				beforeHp = Integer.parseInt(hasMonstersInfo.get(selected-1).get("hp"));
				
				// HP回復計算
				if ((maxHp - beforeHp) < recovery) {
					afterHp = maxHp;
				} else {
					afterHp = beforeHp + recovery;
				}
				// モンスターのHP回復をDBへ反映
				try {
					DataBase.wExecuteUpdate(
							"update testhasmonsters set hp = " + Integer.toString(afterHp)
							+ " where orderNum = " + Integer.toString(selected));
				} catch (SQLException sqle) {
					throw new ItemHandlingFailedException();
				}
				
				// アイテムの個数カウント
				itemCount = itemCount - 1;
				
				// リロード
				targetmp.setVisible(false);
				hasMonstersInfo = menuEvent.getHasMonstersInfo();
				targetmp.showHasMonsters(hasMonstersInfo);
				ihap.repaint();
				JOptionPane.showMessageDialog(null, itemName + "を使用しました。",
						Main.GAME_TITLE, JOptionPane.INFORMATION_MESSAGE);
				
				if (PanelController.state == PanelController.STATE_BATTLE) {
					// バトル中 -> 1度の使用で相手ターンに(ループ離脱 -> バトル画面に戻る)
					ihap.controller.showBattlemainPanel();
					targetmp.onClickBtnBack();
					ItemDetailPanel.exitIDPLoopFlag = ItemDetailPanel.EXIT_IDP_LOOP;
					HasItemsPanel.exitHIPLoopFlag = HasItemsPanel.EXIT_HIP_LOOP;
					Battlemain.itemUsedFlag = true;
					break;
					
				} else {
					if (itemCount == 0) {
						// アイテムの個数が0個になった場合は、強制的に終了
						targetmp.onClickBtnBack();
						break;
					}
				}
			}
			
			selected = 0;
			targetmp.setSelectedMonster(selected);
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// アイテムの残り個数をDBに反映
		try {
			DataBase.wExecuteUpdate("update testitems set count = " + Integer.toString(itemCount)
					+ " where itemId = " + Integer.toString(itemId));
		} catch (SQLException sqle) {
			throw new ItemHandlingFailedException();
		}
		
		setEnabledOnUse(false);
		ihap.requestFocus(false);
		ihap.setVisible(false);
		
	}
	
	@Override
	void itemThrowAway() throws ItemHandlingFailedException {
		effectLoopFlag = RUNNING_EFFECT_LOOP;
		isItemThrowOk = false;
		
		// アイテムの個数を取得
		int itemCount = 0;
		try {
			itemCount = Integer.parseInt(
					DataBase.wExecuteQuery("select count from testitems where itemId = "
							+ Integer.toString(itemId)).get(0).get("count"));
		} catch (SQLException sqle) {
			throw new ItemHandlingFailedException();
		}
		
		setEnabledOnThrowAway(true);
		ihap.setVisible(true);
		ihap.requestFocus(true);
		ihap.repaint();
		
		while(true) {
			if(effectLoopFlag != RUNNING_EFFECT_LOOP || Main.exitFlag != Main.RUNNING) {
				break;
			}
			
			if (isItemThrowOk) {
				// アイテムの個数を-1する
				itemCount = itemCount - 1;
				
				JOptionPane.showMessageDialog(null, itemName + "を捨てました。",
						Main.GAME_TITLE, JOptionPane.INFORMATION_MESSAGE);
				
				isItemThrowOk = false;
			}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// DBへ反映
		try {
			DataBase.wExecuteUpdate("update testitems set count = " + Integer.toString(itemCount)
			+ " where itemId = " + Integer.toString(itemId));
		} catch (SQLException sqle) {
			throw new ItemHandlingFailedException();
		}
		
		setEnabledOnThrowAway(false);
		ihap.requestFocus(false);
		ihap.setVisible(false);
	}
	
	void setEnabledOnUse(boolean b) {
		lInst.setVisible(b);
		targetmp.setVisible(b);
		btnBack.setVisible(b);
	}
	
	void setEnabledOnThrowAway(boolean b) {
		lConfirm.setVisible(b);
		btnThrowOk.setVisible(b);
		btnThrowNg.setVisible(b);
	}

}

