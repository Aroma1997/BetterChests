/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;


import java.util.ArrayList;

import aroma1997.betterchests.upgrades.BasicUpgrade;
import aroma1997.betterchests.upgrades.CobbleGen;
import aroma1997.betterchests.upgrades.Collector;
import aroma1997.betterchests.upgrades.Furnace;
import aroma1997.betterchests.upgrades.Null;
import aroma1997.betterchests.upgrades.Rain;
import aroma1997.betterchests.upgrades.Ticking;
import aroma1997.core.util.AromaRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.registry.GameRegistry;

public enum Upgrade {
	SLOT(null, 9, true, true, Null.class),
	COBBLEGEN(null, 1, true, true, CobbleGen.class),
	REDSTONE(null, 1, true, false, Null.class),
	LIGHT(null, 1, true, false, Null.class),
	BASIC(null, 0, false, false, Null.class),
	COMPARATOR(null, 1, true, false, Null.class),
	VOID(null, 1, true, true, Null.class),
	UNBREAKABLE(null, 1, true, false, Null.class),
	PLAYER(UNBREAKABLE, 1, true, false, Null.class),
	RAIN(null, 1, true, false, Rain.class),
	ENERGY(null, 1, true, true, Null.class),
	FURNACE(ENERGY, 1, true, true, Furnace.class),
	COLLECTOR(ENERGY, 8, true, true, Collector.class),
	TICKING(ENERGY, 1, true, false, Ticking.class);
	
	private Upgrade requirement;
	
	private int max;
	
	private boolean validItem;
	
	private final boolean canBag;
	
	private BasicUpgrade upgrade;
	
	private Upgrade(Upgrade requirement, int max, boolean validItem, boolean canBag, Class<? extends BasicUpgrade> claSS) {
		this.requirement = requirement;
		this.max = max;
		this.validItem = validItem;
		this.canBag = canBag;
		
		try {
			upgrade = claSS.newInstance();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return StatCollector.translateToLocal("item.betterchests:upgrade." + toString() + ".name");
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
		AromaRegistry.registerShapedOreRecipe(new ItemStack(itemID, 8, Upgrade.BASIC.ordinal()),
			"WIW",
			"ISI", "WIW", 'W', "plankWood", 'I', new ItemStack(
				Block.fenceIron), 'S', new ItemStack(Item.stick));
		// SLOT
		AromaRegistry.registerShapedOreRecipe(new ItemStack(itemID, 1, Upgrade.SLOT.ordinal()),
			" W ",
			"WUW", " W ", 'W', "plankWood", 'U', itemUpgrade);
		// REDSTONE
		AromaRegistry.registerShapedOreRecipe(new ItemStack(itemID, 1, Upgrade.REDSTONE.ordinal()),
			"RRR",
			"RUR", "RRR", 'R', new ItemStack(Item.redstone), 'U', itemUpgrade);
		// LIGHT
		AromaRegistry.registerShapedOreRecipe(new ItemStack(itemID, 1, Upgrade.LIGHT.ordinal()),
			" G ", "GUG", " G ", 'G', new ItemStack(Item.glowstone), 'U', itemUpgrade);
		// COMPARATOR
		AromaRegistry.registerShapedOreRecipe(
			new ItemStack(itemID, 1, Upgrade.COMPARATOR.ordinal()),
			" Q ", "RUR", " Q ", 'Q', new ItemStack(Item.netherQuartz), 'R', new ItemStack(
				Item.redstone), 'U', itemUpgrade);
		AromaRegistry.registerShapedOreRecipe(
			new ItemStack(itemID, 1, Upgrade.COMPARATOR.ordinal()),
			" R ", "QUQ", " R ", 'Q', new ItemStack(Item.netherQuartz), 'R', new ItemStack(
				Item.redstone), 'U', itemUpgrade);
		// PLAYER
		AromaRegistry.registerShapedOreRecipe(new ItemStack(itemID, 1, Upgrade.PLAYER.ordinal()),
			"OQO",
			"QUQ", "OQO", 'Q', new ItemStack(Item.netherQuartz), 'O',
			new ItemStack(Block.obsidian), 'U', itemUpgrade);
		// COBBLEGEN
		AromaRegistry.registerShapedOreRecipe(
			new ItemStack(itemID, 1, Upgrade.COBBLEGEN.ordinal()), "CCC", "BUB",
			"CCC", 'C', "cobblestone", 'U',
			itemUpgrade, 'B', Item.bucketEmpty);
		// VOID
		AromaRegistry.registerShapedOreRecipe(new ItemStack(itemID, 1, Upgrade.VOID.ordinal()),
			" E ", "RUR",
			" E ", 'E', new ItemStack(Item.enderPearl), 'R', new ItemStack(Item.redstone), 'U',
			itemUpgrade);
		// UNBREAKABLE
		AromaRegistry.registerShapedOreRecipe(
			new ItemStack(itemID, 1, Upgrade.UNBREAKABLE.ordinal()), "OOO",
			"OUO", "OOO", 'O', new ItemStack(Block.obsidian), 'U', itemUpgrade);
		// RAIN
		AromaRegistry.registerShapedOreRecipe(new ItemStack(itemID, 1, Upgrade.RAIN.ordinal()),
			" B ", "BUB",
			" B ", 'B', new ItemStack(Item.bucketEmpty), 'U', itemUpgrade);
		// ENERGY
		AromaRegistry.registerShapedOreRecipe(new ItemStack(itemID, 1, Upgrade.ENERGY.ordinal()),
			"BRB", "CUC",
			"BRB", 'B', new ItemStack(Block.coalBlock), 'R', new ItemStack(Item.redstone),
			'C', new ItemStack(Item.redstoneRepeater), 'U', itemUpgrade);
		AromaRegistry.registerShapedOreRecipe(new ItemStack(itemID, 1, Upgrade.ENERGY.ordinal()), "BCB", "RUR",
			"BCB", 'B', new ItemStack(Block.coalBlock), 'R', new ItemStack(Item.redstone),
			'C', new ItemStack(Item.redstoneRepeater), 'U', itemUpgrade);
		// FURNACE
		AromaRegistry.registerShapedOreRecipe(new ItemStack(itemID, 1, Upgrade.FURNACE.ordinal()),
			"RFR", "FUF",
			"RFR", 'R', new ItemStack(Item.redstone), 'F', new ItemStack(Block.furnaceIdle), 'U',
			itemUpgrade);
		// COLLECTOR
		AromaRegistry.registerShapedOreRecipe(
			new ItemStack(itemID, 1, Upgrade.COLLECTOR.ordinal()), " H ", "HUH",
			"RER", 'H', new ItemStack(Block.hopperBlock), 'R', new ItemStack(Item.redstone), 'E',
			new ItemStack(Item.enderPearl), 'U', itemUpgrade);
		// TICKING
		AromaRegistry.registerShapedOreRecipe(new ItemStack(itemID, 1, Upgrade.TICKING.ordinal()),
			"QCQ", "RUR", "QCQ", 'Q', new ItemStack(Item.netherQuartz), 'C', new ItemStack(
				Item.pocketSundial), 'R', new ItemStack(Item.comparator), 'U', itemUpgrade);
		
		for (Upgrade upgr : Upgrade.values()) {
			if (! upgr.isValidUpgrade()) {
				continue;
			}
			GameRegistry.addShapelessRecipe(itemUpgrade, new ItemStack(itemID, 1, upgr.ordinal()));
		}
	}
	
	public int getMaxAmount() {
		return max;
	}
	
	public boolean isValidUpgrade() {
		return validItem;
	}
	
	public boolean canBagTakeUpgrade() {
		return canBag && isValidUpgrade();
	}
	
	public static void addBagBookDescription(ArrayList<String> list) {
		for (Upgrade upgrade : Upgrade.values()) {
			list.add("book.betterchests:upgrade." + upgrade);
		}
	}
	
	public ItemStack getItem() {
		return new ItemStack(BetterChests.upgrade, 1, ordinal());
	}
	
	public BasicUpgrade getUpgrade() {
		return upgrade;
	}
}
