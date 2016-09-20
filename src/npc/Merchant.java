package npc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import gamesystem.MessageBoard;
import main.DataBase;
import main.PanelController;
import map.MenuPanel;
import map.map_main;

public class Merchant extends NpcBase {
	
	private ArrayList<HashMap<String, String>> productsInfo = null;
	
	Merchant() {}

	@Override
	void create(int npcId) throws NpcLoadFailedException {
		String products = null;
		
		System.out.println(npcId);
		try {
			npcInfo = DataBase.wExecuteQuery("select * from testnpcs where id = " + Integer.toString(npcId));
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		products = npcInfo.get(0).get("productIds");
		try {
			productsInfo = DataBase.wExecuteQuery("select * from testitems where itemId in (" + products + ")");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
	}

	@Override
	void talk(MessageBoard msgBoard) throws NpcLoadFailedException {
		msgBoard.setMessage("いらっしゃいませ");
		act();
		msgBoard.setMessage("ありがとうございました");
	}

	@Override
	void act() throws NpcLoadFailedException {
		map_main map = PanelController.mp;
		
//		map.merchantMenuPanel.setMenuType(MenuPanel.MERCHANT_MENU);
		map.merchantMenuPanel.setMerchantProducts(productsInfo);
		map.merchantMenuPanel.showPanel();
		map.merchantMenuPanel.loop();
//		map.pushSpaceKey();
	}
	
	

}
