package npc;

import java.sql.SQLException;

import gamesystem.MessageBoard;
import main.DataBase;

public class Inn extends NpcBase {

	Inn() {}
	
	@Override
	void create(int npcId) throws NpcLoadFailedException {
		/* no operation */
	}

	@Override
	void talk(MessageBoard msgBoard) throws NpcLoadFailedException {
		msgBoard.setMessage("<html>ながたび、おつかれさまです <br /> ゆっくりおやすみになってください</html>");
		act();
		msgBoard.setMessage("いってらっしゃいませ");
	}

	@Override
	void act() throws NpcLoadFailedException {
		try {
			DataBase.wExecuteUpdate("update testhasmonsters set hp = maxhp, state = 0 "
					+ "where orderNum is not null");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
