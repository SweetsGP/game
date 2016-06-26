package itemhandler;

public class ItemHandlingFailedException extends Exception {
	
	public ItemHandlingFailedException() {
		super();
	}
	
	public ItemHandlingFailedException(String errmsg) {
		super(errmsg);
	}

}
