package aroma1997.betterchests;


public enum Upgrade {
	SLOT("Slot Upgrade", "Gives you 9 more Slots."),
	STACK("Stack Upgrade", "Gives you one more Item per Slot.");
	
	private String name;
	private String tooltip;
	
	private Upgrade(String name, String tooltip) {
		this.name = name;
		this.tooltip = tooltip;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getTooltip() {
		return this.tooltip;
	}
	
}
