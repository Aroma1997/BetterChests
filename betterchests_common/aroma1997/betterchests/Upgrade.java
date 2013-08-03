package aroma1997.betterchests;


public enum Upgrade {
	SLOT("Slot Upgrade", "Gives you " + Reference.Conf.SLOT_UPGRADE + " more Slots.", "slot"),
	STACK("Stack Upgrade", "Gives you " + Reference.Conf.STACK_UPGRADE + " more Item per Slot.", "stack"),
	REDSTONE("Redstone Upgrade", "Outputs a redstone signal, when somebody opens the chest.", "redstone"),
	LIGHT("Light Upgrade", "Makes the Chest emit light. " + Colors.RED + "Does not work yet.", "light"),
	BASIC("Upgrade Case", "This is only needed to craft the other upgrades.", "basic"),
	COMPARATOR("Comparator Upgrade", "This Upgrade will enable the usage of the Comparators.", "comparator"),
	PLAYER("Player Upgrade", "This will make the chest accessable only for you.", "player"),
	VOID("Void Upgrade", "This will destroy and delete all Items that go into the Chest.", "void"),
	UNBREAKABLE("Unbreakable Upgrade", "This will make Entitys no longer able to destroy the chest. (Wither,...)", "unbreakable"),
	RAIN("Rain Upgrade", "This will fill buckets in the Chest with Water.", "rain");
	
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
