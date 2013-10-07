/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests;


import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public enum Upgrade {
	SLOT("Slot Upgrade", "Gives you 9 more Slots.", null, 5),
	COBBLEGEN("Cobblestone Generator", "This lets your chest create Cobblestone with Lava and Water", null, 1),
	REDSTONE("Redstone Upgrade", "Outputs a redstone signal, when somebody opens the chest.", null, 1),
	LIGHT("Light Upgrade", "Makes the Chest emit light.", null, 1),
	BASIC("Upgrade Case", "This is only needed to craft the other upgrades.", null, 0),
	COMPARATOR("Comparator Upgrade", "This Upgrade will enable the usage of the Comparators.", null, 1),
	VOID("Void Upgrade", "This will destroy all Items that enter the Chest.", null, 1),
	UNBREAKABLE("Unbreakable Upgrade", "This will make Entitys no longer able to destroy the chest. (Wither,...)", null, 1),
	PLAYER("Player Upgrade", "This will make the chest accessable only for you.", UNBREAKABLE, 1),
	RAIN("Rain Upgrade", "This will fill buckets in the Chest with Water.", null, 1),
	SOLAR("Solar Upgrade", "This will supply your chest Power.", null, 1),
	FURNACE("Furnace Upgrade", "With this Upgrade, your chest will smelt.", SOLAR, 1),
	COLLECTOR("Collector Upgrade", "Lets the chest collect Items around it. Radius increase by 1.", SOLAR, 8),
	TICKING("Ticking Upgrade", "Ticks the Items as in a Player Inventory.", SOLAR, 1);
	
	private String name;
	
	private String tooltip;
	
	private Upgrade requirement;
	
	private int max;
	
	private Upgrade(String name, String tooltip, Upgrade requirement, int max) {
		this.name = name;
		this.tooltip = tooltip;
		this.requirement = requirement;
		this.max = max;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTooltip() {
		return tooltip;
	}
	
	public String getTexture() {
		return Reference.MOD_ID + ":" + toString().toLowerCase();
	}
	
	public Upgrade getRequirement() {
		return requirement;
	}
	
	public static void generateRecipes() {
		int itemID = BetterChests.upgrade.itemID;
		ItemStack itemUpgrade = new ItemStack(itemID, 1, Upgrade.BASIC.ordinal());
		// BASIC
		GameRegistry.addRecipe(new ItemStack(itemID, 8, Upgrade.BASIC.ordinal()), "WIW",
			"ISI", "WIW", 'W', new ItemStack(Block.planks, 1, 32767), 'I', new ItemStack(
				Block.fenceIron), 'S', new ItemStack(Item.stick));
		// SLOT
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.SLOT.ordinal()), " W ",
			"WUW", " W ", 'W', new ItemStack(Block.planks, 1, 32767), 'U', itemUpgrade);
		// REDSTONE
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.REDSTONE.ordinal()), "RRR",
			"RUR", "RRR", 'R', new ItemStack(Item.redstone), 'U', itemUpgrade);
		//LIGHT
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.LIGHT.ordinal()), " G ", "GUG", " G ", 'G', new ItemStack(Item.glowstone), 'U', itemUpgrade);
		// COMPARATOR
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.COMPARATOR.ordinal()),
			" Q ", "RUR", " Q ", 'Q', new ItemStack(Item.netherQuartz), 'R', new ItemStack(
				Item.redstone), 'U', itemUpgrade);
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.COMPARATOR.ordinal()),
			" R ", "QUQ", " R ", 'Q', new ItemStack(Item.netherQuartz), 'R', new ItemStack(
				Item.redstone), 'U', itemUpgrade);
		// PLAYER
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.PLAYER.ordinal()), "OQO",
			"QUQ", "OQO", 'Q', new ItemStack(Item.netherQuartz), 'O',
			new ItemStack(Block.obsidian), 'U', itemUpgrade);
		// COBBLEGEN
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.COBBLEGEN.ordinal()), "CCC", "LUW",
			"BBB", 'C', new ItemStack(Block.cobblestone), 'L', new ItemStack(Item.bucketLava), 'U',
			itemUpgrade, 'W', new ItemStack(Item.bucketWater), 'B', new ItemStack(Item.bucketEmpty));
		// VOID
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.VOID.ordinal()), " E ", "RUR",
			" E ", 'E', new ItemStack(Item.enderPearl), 'R', new ItemStack(Item.redstone), 'U',
			itemUpgrade);
		// UNBREAKABLE
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.UNBREAKABLE.ordinal()), "OOO",
			"OUO", "OOO", 'O', new ItemStack(Block.obsidian), 'U', itemUpgrade);
		// RAIN
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.RAIN.ordinal()), " B ", "BUB",
			" B ", 'B', new ItemStack(Item.bucketEmpty), 'U', itemUpgrade);
		// SOLAR
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.SOLAR.ordinal()), "QSQ", "RUR",
			"QCQ", 'S', new ItemStack(Block.daylightSensor), 'R', new ItemStack(Item.redstone),
			'C', new ItemStack(Item.redstoneRepeater), 'Q', new ItemStack(Item.netherQuartz), 'R',
			new ItemStack(Item.redstone), 'U', itemUpgrade);
		// FURNACE
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.FURNACE.ordinal()), "RFR", "FUF",
			"RFR", 'R', new ItemStack(Item.redstone), 'F', new ItemStack(Block.furnaceIdle), 'U',
			itemUpgrade);
		// COLLECTOR
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.COLLECTOR.ordinal()), " H ", "HUH",
			"RER", 'H', new ItemStack(Block.hopperBlock), 'R', new ItemStack(Item.redstone), 'E',
			new ItemStack(Item.enderPearl), 'U', itemUpgrade);
		// TICKING
		GameRegistry.addRecipe(new ItemStack(itemID, 1, Upgrade.TICKING.ordinal()), "QCQ", "RUR", "QCQ", 'Q', new ItemStack(Item.netherQuartz), 'C', new ItemStack(Item.pocketSundial), 'R', new ItemStack(Item.comparator), 'U', itemUpgrade);
		
		for (Upgrade upgr : Upgrade.values()) {
			if (upgr == BASIC) {
				continue;
			}
			GameRegistry.addShapelessRecipe(itemUpgrade, new ItemStack(itemID, 1, upgr.ordinal()));
		}
	}
	
	public int getMaxAmount() {
		return max;
	}
}
