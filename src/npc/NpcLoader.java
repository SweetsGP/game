package npc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gamesystem.MessageBoard;

public class NpcLoader {

	private Class<?> npcClass;
	private Object npcObject;
	
	public NpcLoader(String npcTypeName) throws NpcLoadFailedException {
		try {
			// 指定した名前のハンドラクラスをロード
			npcClass = Class.forName("npc." + npcTypeName);
			
			// 指定した名前のアイテムのハンドラオブジェクト生成
			Constructor<?> npcConstructor
				= npcClass.getDeclaredConstructor(new Class[] {});
			npcObject = npcConstructor.newInstance();
			
		} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			throw new NpcLoadFailedException();
		}
	}
	
	/**
	 * NPCの初期化を行います
	 * @param npcId
	 * @throws NpcLoadFailedException
	 */
	public void create(int npcId) throws NpcLoadFailedException {
		try {
			Method create = npcClass.getDeclaredMethod("create", new Class[] {int.class});
			create.invoke(npcObject, new Object[] {npcId});
		} catch(Exception e) {
			throw new NpcLoadFailedException();
		}
	}
	
	/**
	 * NPCとの会話処理を行います
	 * @param msgBoard
	 * @throws NpcLoadFailedException
	 */
	public void talk(MessageBoard msgBoard) throws NpcLoadFailedException {
		try {
			Method talk = npcClass.getDeclaredMethod("talk", new Class[] {MessageBoard.class});
			talk.invoke(npcObject, new Object[] {msgBoard});
		} catch(Exception e) {
			throw new NpcLoadFailedException();
		}
	}
	
	/**
	 * NPCに対する/からのアクションを処理します
	 * @throws NpcLoadFailedException
	 */
	public void act() throws NpcLoadFailedException {
		try {
			Method act = npcClass.getDeclaredMethod("act", new Class[] {});
			act.invoke(npcObject, new Object[] {});
		} catch(Exception e) {
			throw new NpcLoadFailedException();
		}
	}
}
