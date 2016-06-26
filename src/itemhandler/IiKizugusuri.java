package itemhandler;

class IiKizugusuri extends Kizugusuri {
	
	IiKizugusuri(ItemHandlingAreaPanel p) {
		super(p);
		
		recovery = 100;
		itemName = "いいキズぐすり";
		itemId = 2;
	}
	
	@Override
	int getItemFlag() {
		return super.getItemFlag();
	}
	
	@Override
	void itemEffect() throws ItemHandlingFailedException {
		super.itemEffect();
	}
	
	@Override
	void itemThrowAway() throws ItemHandlingFailedException {
		super.itemThrowAway();
	}

}

