
package aroma1997.betterchests;


public enum Upgrade {
	SLOT("Slot Upgrade", "Gives you " + Reference.Conf.SLOT_UPGRADE + " more Slots.", "slot", null),
	COBBLEGEN("Cobblestone Generator", "This lets your chest create Cobblestone with Lava and Water", "cobble", null),
	REDSTONE("Redstone Upgrade", "Outputs a redstone signal, when somebody opens the chest.", "redstone", null),
	LIGHT("Light Upgrade", "Makes the Chest emit light. " + Colors.RED + "Does not work yet.", "light", null),
	BASIC("Upgrade Case", "This is only needed to craft the other upgrades.", "basic", null),
	COMPARATOR("Comparator Upgrade", "This Upgrade will enable the usage of the Comparators.", "comparator", null),
	VOID("Void Upgrade", "This will destroy and delete all Items that go into the Chest.", "void", null),
	UNBREAKABLE("Unbreakable Upgrade", "This will make Entitys no longer able to destroy the chest. (Wither,...)", "unbreakable", null),
	PLAYER("Player Upgrade", "This will make the chest accessable only for you.", "player", UNBREAKABLE),
	RAIN("Rain Upgrade", "This will fill buckets in the Chest with Water.", "rain", null);
	
	private String name;
	
	private String tooltip;
	
	private String texture;
	
	private Upgrade requirement;
	
	private Upgrade(String name, String tooltip, String texture, Upgrade requirement) {
		this.name = name;
		this.tooltip = tooltip;
		this.texture = Reference.MOD_ID + ":" + texture;
		this.requirement = requirement;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTooltip() {
		return tooltip;
	}
	
	public String getTexture() {
		return texture;
	}
	
	public Upgrade getRequirement() {
		return this.requirement;
	}
	
}
