package itemhandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import main.DataBase;
import main.Main;
import main.PanelController;

/**
 * 要求に応じて、すべてのアイテムハンドラから必要なハンドラを呼び出します
 * @author kitayamahideya
 *
 */
public class ItemHandlerDriver {
	
	private Class<?> handlerClass;
	private Object handler;
	
	private int itemFlag = 0;
	
	/**
	 * コンストラクタ
	 * @param handlerName 要求するアイテムのハンドラクラスの名前
	 * @param ihap アイテム処理の際に使用する画面描画領域のパネル(通常はItemDetailPanelの右半分)
	 * @throws ItemHandlingFailedException
	 */
	public ItemHandlerDriver(String handlerName, ItemHandlingAreaPanel ihap) throws ItemHandlingFailedException {
		try {
			// 指定した名前のハンドラクラスをロード
			handlerClass = Class.forName("itemhandler." + handlerName);
			
			// 指定した名前のアイテムのハンドラオブジェクト生成
			Constructor<?> handlerConstructor
				= handlerClass.getDeclaredConstructor(new Class[] {ItemHandlingAreaPanel.class});
			handler = handlerConstructor.newInstance(ihap);
			
			// itemFlagの取得
			Method getItemFlag = handlerClass.getDeclaredMethod("getItemFlag", new Class[] {});
			itemFlag = (Integer )getItemFlag.invoke(handler, new Object[] {});
//			itemFlag = (int )getItemFlag.invoke(handler, new Object[] {});
			
		} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			throw new ItemHandlingFailedException();
		}
	}
	
	/**
	 * アイテム使用処理
	 * @throws ItemHandlingFailedException
	 */
	public void use() throws ItemHandlingFailedException {
		// アイテム効果のバトル中の発動 -> 無い場合にはその旨を表示して処理終了
		if ((itemFlag != ItemHandler.ITEM_USE_ON_BATTLE_OK)
				&& (PanelController.getBeforeCalledLoopNum() == PanelController.BATTLE_PANEL)) {
			JOptionPane.showMessageDialog(null, "このアイテムをバトル中に使用することはできません。",
					Main.GAME_TITLE, JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		// アイテム効果発動処理
		try {
			Method itemEffect = handlerClass.getDeclaredMethod("itemEffect", new Class[] {});
			itemEffect.invoke(handler, new Object[] {});
		} catch(Exception e) {
			throw new ItemHandlingFailedException();
		}
	}
	
	/**
	 * アイテムの破棄処理
	 * @throws ItemHandlingFailedException
	 */
	public void throwAway() throws ItemHandlingFailedException {
		// アイテム破棄処理
		try {
			Method itemThrowAway = handlerClass.getDeclaredMethod("itemThrowAway", new Class[] {});
			itemThrowAway.invoke(handler, new Object[] {});
		} catch(Exception e) {
			throw new ItemHandlingFailedException();
		}
	}

}
