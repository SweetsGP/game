package main;

import java.awt.Container;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JPanel;

import map.map_main;

import Battle.Battlemain;
import gamesystem.CharacterCreatingPanel;
import gamesystem.CharacterMakingPanel;
import gamesystem.HasMonsterDetailPanel;
import gamesystem.ItemDetailPanel;
import gamesystem.MainFrame;
import gamesystem.SettingsPanel;
import gamesystem.TitlePanel;

/**
 * 画面遷移管理クラス
 * @author kitayamahideya
 *
 */
public class PanelController {
	// パネルサイズ
	public static final int PANEL_WIDTH = 800;
	public static final int PANEL_HEIGHT = 600;
	
	// パネル番号
	public static final int TITLE = 0;  // タイトル画面
	public static final int SETTINGS = 1;  // 設定画面
	public static final int BATTLE = 2;  // バトル画面
	public static final int MAP = 3;  // マップ画面
	public static final int CHARACTER_MAKING = 4;  // キャラメイク画面
	public static final int HAS_MONSTER_DETAIL = 5; // てもちモンスターの詳細情報画面
	public static final int ITEM_DETAIL = 6; // アイテムの詳細情報画面
	
	// 遷移状態定数
	public static final int STATE_TITLE = 0;  // タイトル
	public static final int STATE_SETTINGS = 1;  // 設定
	public static final int STATE_BATTLE = 2;  // バトル
	public static final int STATE_MAP = 3;  // マップ
	public static final int STATE_CHARACTER_MAKING = 4;  // キャラメイク
	
	// 遷移状態
	public static int state = STATE_TITLE;
	
	// コンテナ
	private static Container container;
	
	// 管理するパネル
	private static TitlePanel tp;
	private SettingsPanel sp;
	private static Battlemain bp;
	public static map_main mp;  // 特例措置
//	private static CharacterMakingPanel cmp;
	private static CharacterCreatingPanel cmp;
	private HasMonsterDetailPanel hmdp;
	private ItemDetailPanel idp;
	
	/**
	 * コンストラクタ
	 * 管理するパネルのインスタンスを生成
	 * @param mf メインフレーム
	 */
	public PanelController(MainFrame mf, Sound s) {
		container = mf.getContentPane();
		
		// パネルのインスタンス化
		this.tp = new TitlePanel(mf, this);
		this.sp = new SettingsPanel(this, s);
		this.bp = new Battlemain(this);
		this.mp = new map_main(this);
//		this.cmp = new CharacterMakingPanel(this);
		this.cmp = new CharacterCreatingPanel(this);
		this.hmdp = new HasMonsterDetailPanel(this);
		this.idp = new ItemDetailPanel(this);
		
	}
	
	/**
	 * 指定された番号のパネルのインスタンスを返す
	 * @param panelNum パネル番号
	 * @return パネルのインスタンス(キャストの必要あり)
	 */
	public Object getPanelInstance(int panelNum) {
		switch(panelNum) {
		case TITLE:
			return tp;
		case SETTINGS:
			return sp;
		case BATTLE:
			return bp;
		case MAP:
			return mp;
		case CHARACTER_MAKING:
			return cmp;
		case HAS_MONSTER_DETAIL:
			return hmdp;
		case ITEM_DETAIL:
			return idp;
			default:
				// never reached
				return null;
		}
	}
	
	/**
	 * 画面遷移管理初期化
	 * @param mf メインフレーム
	 * @throws IOException 
	 */
	public static void init(MainFrame mf, Sound s) {
		PanelController controller = new PanelController(mf, s);
		controller.showTitlePanel();
	}
	
	/**
	 * 画面/状態遷移管理ループ
	 */
	public static void loop(Sound s) {
		if (state == STATE_TITLE) {
			tp.loop(s);
		} else if (state == STATE_MAP) {
			mp.loop(s);
		} else if (state == STATE_BATTLE) {
			bp.loop(s);
		} else if (state == STATE_CHARACTER_MAKING) {
			cmp.loop();
		}
	}
	
	/**
	 * パネル切り替えにより画面遷移を実現(可視化は行わない)
	 * @param panel 遷移先となる画面のパネル
	 */
	private static void showPanel(JPanel panel) {
		// 遷移前の画面を無効化し、パネルを除去する
		container.invalidate();
		container.removeAll();
		
		// 遷移後の画面のパネルを貼り付け、再描画する
		container.add(panel);
		container.repaint();
		
		// 画面を有効化する
		container.validate();
		
		// フォーカスを遷移後の画面に移す
		panel.requestFocus(true);
	}
	
	/**
	 * タイトル画面へ遷移
	 */
	public void showTitlePanel() {
		state = STATE_TITLE;
		showPanel(tp);
	}
	
	/**
	 * 設定画面へ遷移
	 */
	public void showSettingsPanel() {
		state = STATE_SETTINGS;
		showPanel(sp);
		sp.showSettings();
	}
	
	/**
	 * バトル画面へ遷移
	 */
	public void showBattlemainPanel() {
		state = STATE_BATTLE;
		showPanel(bp);
		bp.showBattlemain();
//		bp.loop();
	}
	
	/**
	 * マップ画面へ遷移
	 */
	public void showMapPanel() {
		state = STATE_MAP;
		showPanel(mp);
	}
	
	/**
	 * 主人公作成画面へ遷移
	 */
	public void showCharacterMakingPanel() {
		state = STATE_CHARACTER_MAKING;
		showPanel(cmp);
//		cmp.showCharacterMakingPanel();
		cmp.showPanel();
	}
	
	/**
	 * てもちモンスターの詳細情報画面へ遷移
	 */
	public void showHasMonsterDetailPanel(HashMap<String, String> hasMonsterInfo) {
		showPanel(hmdp);
		hmdp.showDetailOfHasMonster(hasMonsterInfo);
	}
	
	/**
	 * アイテムの詳細情報画面へ遷移
	 */
	public void showItemDetailPanel(HashMap<String, String> itemInfo) {
		showPanel(idp);
		idp.showDetailOfItem(itemInfo);
	}

}
