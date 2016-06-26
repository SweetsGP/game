package npc;

import java.util.ArrayList;
import java.util.HashMap;

import gamesystem.MessageBoard;

abstract class NpcBase {
	
	public ArrayList<HashMap<String, String>> npcInfo;
	
	abstract void create(int npcId) throws NpcLoadFailedException;
	
	abstract void talk(MessageBoard msgBoard) throws NpcLoadFailedException;
	
	abstract void act() throws NpcLoadFailedException;

}
