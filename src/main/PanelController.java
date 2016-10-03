package main;

import java.awt.Container;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JPanel;

import map.map_main;

import Battle.Battlemain;
import gamesystem.CharacterCreatingPanel;
import gamesystem.HasMonsterDetailPanel;
import gamesystem.ItemDetailPanel;
import gamesystem.MainFrame;
import gamesystem.SettingsPanel;
import gamesystem.SplashPanel;
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
	public static final int NONE_PANEL = -1;  // 初回起動時のみ
	public static final int TITLE_PANEL = 0;  // タイトル画面
	public static final int SETTINGS_PANEL = 1;  // 設定画面
	public static final int BATTLE_PANEL = 2;  // バトル画面
	public static final int MAP_PANEL = 3;  // マップ画面
	public static final int CHARACTER_CREATING_PANEL = 4;  // キャラメイク画面
	public static final int HAS_MONSTER_DETAIL_PANEL = 5; // てもちモンスターの詳細情報画面
	public static final int ITEM_DETAIL_PANEL = 6; // アイテムの詳細情報画面
	
	// コンテナ
	private static Container container;
	
	// 管理するパネル
	private static SplashPanel spp;
	private static TitlePanel tp;
	private static SettingsPanel sp;
	public static map_main mp;  // 暫定
	private static Battlemain bp;
	private static CharacterCreatingPanel ccp;
	private static HasMonsterDetailPanel hmdp;
	private static ItemDetailPanel idp;
	
	// パネル表示情報
	private static int nowDisplayed;
	
	// ループコール情報
	private static int beforeCalledLoop;
	private static int nowCalledLoop;
	private static int nextCallLoop;
	
	// サウンド
	private static Sound sound;
	
	/**
	 * コンストラクタ
	 * 管理するパネルのインスタンスを生成
	 * @param mf メインフレーム
	 */
	public PanelController(MainFrame mf, SplashPanel splashp, Sound s) {
		container = mf.getContentPane();
		
		// パネルのインスタンス化
		spp = splashp;
		tp = new TitlePanel(mf, this);
		sp = new SettingsPanel(this, s);
		bp = new Battlemain(this);
		mp = new map_main(this);
		ccp = new CharacterCreatingPanel(this);
		hmdp = new HasMonsterDetailPanel(this);
		idp = new ItemDetailPanel(this);
		
		// パネル表示情報
		nowDisplayed = NONE_PANEL;
		
		// ループコール情報初期化
		beforeCalledLoop = NONE_PANEL;
		nowCalledLoop = NONE_PANEL;
		nextCallLoop = NONE_PANEL;
		
		sound = s;
	}
	
	/**
	 * 画面遷移管理初期化
	 * @param mf メインフレーム
	 */
	public static void init(MainFrame mf, SplashPanel spp, Sound s) {
		PanelController controller = new PanelController(mf, spp, s);
		controller.screenTrans(TITLE_PANEL);
		reserveCallLoop(TITLE_PANEL);
	}
	
	// ==================================================
	// 画面表示/遷移管理
	// ==================================================
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
	 * スプラッシュ画面の表示
	 */
	public static void showSplash() {
		showPanel(spp);
		spp.showPanel();
	}
	
	/**
	 * 各画面への切り替え(可視化も行う)
	 * @param panelNum パネル番号
	 */
	public void screenTrans(int panelNum) {
		switch(panelNum) {
		case TITLE_PANEL:
			nowDisplayed = TITLE_PANEL;
			showPanel(tp);
			tp.showPanel();
			break;
			
		case SETTINGS_PANEL:
			nowDisplayed = SETTINGS_PANEL;
			showPanel(sp);
			sp.showPanel();
			break;
			
		case BATTLE_PANEL:
			nowDisplayed = BATTLE_PANEL;
			showPanel(bp);
			bp.showBattlemain();
			break;
			
		case MAP_PANEL:
			nowDisplayed = MAP_PANEL;
			showPanel(mp);
			break;
			
		case CHARACTER_CREATING_PANEL:
			nowDisplayed = CHARACTER_CREATING_PANEL;
			showPanel(ccp);
			ccp.showPanel();
			break;
			
//		case HAS_MONSTER_DETAIL_PANEL:
//			nowDisplayed = HAS_MONSTER_DETAIL_PANEL;
//			showPanel(hmdp);
//			hmdp.showDetailOfHasMonster(hasMonsterInfo);
//			break;
//		case ITEM_DETAIL_PANEL:
//			nowDisplayed = ITEM_DETAIL_PANEL;
//			showPanel(idp);
//			idp.showDetailOfItem(itemInfo);
//			break;
			
		default:
			break;
		}
	}
	
	/**
	 * てもちモンスターの詳細情報画面へ遷移
	 */
	public void showHasMonsterDetailPanel(HashMap<String, String> hasMonsterInfo) {
		nowDisplayed = HAS_MONSTER_DETAIL_PANEL;
		showPanel(hmdp);
		hmdp.showDetailOfHasMonster(hasMonsterInfo);
	}
	
	/**
	 * アイテムの詳細情報画面へ遷移
	 */
	public void showItemDetailPanel(HashMap<String, String> itemInfo) {
		nowDisplayed = ITEM_DETAIL_PANEL;
		showPanel(idp);
		idp.showDetailOfItem(itemInfo);
	}
	
	// 暫定
	/**
	 * タイトル画面へ遷移
	 */
	public void showTitlePanel() {
		nowDisplayed = TITLE_PANEL;
		showPanel(tp);
		tp.showPanel();
	}
	
	/**
	 * 設定画面へ遷移
	 */
	public void showSettingsPanel() {
		nowDisplayed = SETTINGS_PANEL;
		showPanel(sp);
		sp.showPanel();
	}
	
	/**
	 * バトル画面へ遷移
	 */
	public void showBattlemainPanel() {
		nowDisplayed = BATTLE_PANEL;
		showPanel(bp);
		bp.showBattlemain();
	}
	
	/**
	 * マップ画面へ遷移
	 */
	public void showMapPanel() {
		nowDisplayed = MAP_PANEL;
		showPanel(mp);
	}
	
	/**
	 * 主人公作成画面へ遷移
	 */
	public void showCharacterMakingPanel() {
		nowDisplayed = CHARACTER_CREATING_PANEL;
		showPanel(ccp);
		ccp.showPanel();
	}
	// =====
	
	// ==================================================
	// 各画面ループ処理呼び出し
	// ==================================================
	/**
	 * 次のループ処理呼び出しを指定します
	 * @param panelNum 次に呼び出すループ処理を有するパネルの番号
	 */
	public static void reserveCallLoop(int panelNum) {
		if (panelNum <= NONE_PANEL || ITEM_DETAIL_PANEL < panelNum) {
			System.out.println("Invalid panelNum.");
			return;
		}
		
		nextCallLoop = panelNum;
	}
	
	/**
	 * 呼び出すべきループ処理があるかどうかチェックし，あれば呼び出す
	 */
	public static void checkCallLoop() {
		if (nowCalledLoop != nextCallLoop) {
			callLoop(nextCallLoop);
		}
	}
	
	/**
	 * 引数で指定したパネルの有するループ処理を呼び出します
	 * @param panelNum 呼び出したいループ処理を有するパネルの番号
	 */
	public static void callLoop(int panelNum) {
		if (panelNum <= NONE_PANEL || ITEM_DETAIL_PANEL < panelNum) {
			System.out.println("Invalid panelNum.");
			return;
		}
		
		// コールループ情報更新
		beforeCalledLoop = nowCalledLoop;
		nowCalledLoop = panelNum;
		nextCallLoop = panelNum;
		
		// ループ処理呼び出し
		switch(nowCalledLoop) {
		case TITLE_PANEL:
			tp.loop();
			break;
			
		case SETTINGS_PANEL:
			sp.loop();
			break;
			
		case BATTLE_PANEL:
			bp.loop(sound);
			break;
			
		case MAP_PANEL:
			mp.loop(sound);
			break;
			
		case CHARACTER_CREATING_PANEL:
			ccp.loop();
			break;
			
		case HAS_MONSTER_DETAIL_PANEL:
			hmdp.loop();
			break;
			
		case ITEM_DETAIL_PANEL:
			idp.loop();
			break;
		}
	}
	
	/**
	 * 現在呼び出されているループ処理の属するパネルの番号を返します
	 * @return
	 */
	public static int getNowCalledLoopNum() {
		return nowCalledLoop;
	}
	
	/**
	 * 前回呼び出されたループ処理の属するパネルの番号を返します
	 * @return
	 */
	public static int getBeforeCalledLoopNum() {
		return beforeCalledLoop;
	}
	
	/**
	 * 前回呼び出されていたループ処理に戻る(管理情報調整用 -> 実際にループ処理の呼び出しは行わない)
	 */
	public static void callBack() {
		nowCalledLoop = beforeCalledLoop;
	}
	
	// ==================================================
	// その他
	// ==================================================
	// 暫定
	/**
	 * 指定された番号のパネルのインスタンスを返す
	 * @param panelNum パネル番号
	 * @return パネルのインスタンス(キャストの必要あり)
	 */
	public Object getPanelInstance(int panelNum) {
		switch(panelNum) {
		case TITLE_PANEL:
			return tp;
		case SETTINGS_PANEL:
			return sp;
		case BATTLE_PANEL:
			return bp;
		case MAP_PANEL:
			return mp;
		case CHARACTER_CREATING_PANEL:
			return ccp;
		case HAS_MONSTER_DETAIL_PANEL:
			return hmdp;
		case ITEM_DETAIL_PANEL:
			return idp;
			default:
				// never reached
				return null;
		}
	}
	// =====
}
