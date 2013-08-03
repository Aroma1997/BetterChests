package aroma1997.betterchests;


public enum Upgrade {
	SLOT("Slot Upgrade", "Gives you 9 more Slots.", "slot"),
	STACK("Stack Upgrade", "Gives you one more Item per Slot.", "stack"),
	REDSTONE("Redstone Upgrade", "Outputs a redstone signal, when somebody opens the chest.", "redstone"),
	LIGHT("Light Upgrade", "Makes the Chest emit light. " + Colors.RED + "Does not work yet.", "light");
	
	private String name;
	private String tooltip;
	private String texture;
	
	private Upgrade(String name, String tooltip, String texture) {
		this.name = name;
		this.tooltip = tooltip;
		this.texture =  Reference.MOD_ID + ":" + texture;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getTooltip() {
		return this.tooltip;
	}
	
	public String getTexture() {
		return texture;
	}
	
}
