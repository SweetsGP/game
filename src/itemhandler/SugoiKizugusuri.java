package itemhandler;

class SugoiKizugusuri extends Kizugusuri {
	
	SugoiKizugusuri(ItemHandlingAreaPanel p) {
		super(p);
		
		recovery = 150;
		itemName = "すごいキズぐすり";
		itemId = 3;
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
