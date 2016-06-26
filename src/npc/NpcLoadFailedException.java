package npc;

public class NpcLoadFailedException extends Exception {
	
	public NpcLoadFailedException() {
		super();
	}
	
	public NpcLoadFailedException(String errmsg) {
		super(errmsg);
	}

}
