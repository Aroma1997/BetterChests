
package aroma1997.betterchests;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;


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
	
	public static void generateRecipes() {
		int itemID = BetterChests.upgrade.itemID;
		ItemStack itemUpgrade = new ItemStack(itemID, 1, Upgrade.BASIC.ordinal());
		//BASIC
		GameRegistry.addRecipe(new ItemStack(itemID, 8, Upgrade.BASIC.ordinal()), "WIW",
			"ISI", "WIW", 'W', new ItemStack(Block.planks, 1, 32767), 'I', new ItemStack(
				Block.fenceIron), 'S', new ItemStack(Item.stick));
		//SLOT
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.SLOT.ordinal()), " W ",
			"WUW", " W ", 'W', new ItemStack(Block.planks, 1, 32767), 'U', itemUpgrade);
		//
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.REDSTONE.ordinal()), "RRR",
			"RUR", "RRR", 'R', new ItemStack(Item.redstone), 'U', itemUpgrade);
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.COMPARATOR.ordinal()),
			" Q ", "RUR", " Q ", 'Q', new ItemStack(Item.netherQuartz), 'R', new ItemStack(
				Item.redstone), 'U', itemUpgrade);
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.COMPARATOR.ordinal()),
			" R ", "QUQ", " R ", 'Q', new ItemStack(Item.netherQuartz), 'R', new ItemStack(
				Item.redstone), 'U', itemUpgrade);
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.PLAYER.ordinal()), "OQO",
			"QUQ", "OQO", 'Q', new ItemStack(Item.netherQuartz), 'O',
			new ItemStack(Block.obsidian), 'U', itemUpgrade);
		
		
		
		for (Upgrade upgr : Upgrade.values()) {
			if (upgr == BASIC) continue;
			GameRegistry.addShapelessRecipe(itemUpgrade, new ItemStack(itemID, 1, upgr.ordinal()));
		}
	}
	
}
