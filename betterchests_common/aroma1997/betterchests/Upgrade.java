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
import aroma1997.betterchests.upgrades.Feeding;
import aroma1997.betterchests.upgrades.Furnace;
import aroma1997.betterchests.upgrades.Null;
import aroma1997.betterchests.upgrades.PlayerFeeding;
import aroma1997.betterchests.upgrades.Rain;
import aroma1997.betterchests.upgrades.Resupply;
import aroma1997.betterchests.upgrades.Ticking;
import aroma1997.core.util.AromaRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import net.minecraftforge.oredict.OreDictionary;

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
	TICKING(ENERGY, 1, true, false, Ticking.class),
	FEEDING(null, 1, true, false, Feeding.class),
	PLAYERFOOD(null, 1, false, true, PlayerFeeding.class),
	RESUPPLY(null, 1, false, true, Resupply.class);
	
	private final Upgrade requirement;
	
	private final int max;
	
	private final boolean canChest;
	
	private final boolean canBag;
	
	private BasicUpgrade upgrade;
	
	private Upgrade(Upgrade requirement, int max, boolean canChest, boolean canBag,
		Class<? extends BasicUpgrade> claSS) {
		this.requirement = requirement;
		this.max = max;
		this.canChest = canChest;
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
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 8, Upgrade.BASIC.ordinal()), false,
			"WIW",
			"ISI", "WIW", 'W', "plankWood", 'I', new ItemStack(
				Block.fenceIron), 'S', new ItemStack(Item.stick));
		// SLOT
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 1, Upgrade.SLOT.ordinal()), false,
			" W ",
			"WUW", " W ", 'W', "plankWood", 'U', itemUpgrade);
		// REDSTONE
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 1, Upgrade.REDSTONE.ordinal()), false,
			"RRR",
			"RUR", "RRR", 'R', new ItemStack(Item.redstone), 'U', itemUpgrade);
		// LIGHT
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 1, Upgrade.LIGHT.ordinal()), false,
			" G ", "GUG", " G ", 'G', new ItemStack(Item.glowstone), 'U', itemUpgrade);
		// COMPARATOR
		AromaRegistry.registerShapedAromicRecipe(
			new ItemStack(itemID, 1, Upgrade.COMPARATOR.ordinal()), false,
			" Q ", "RUR", " Q ", 'Q', new ItemStack(Item.netherQuartz), 'R', new ItemStack(
				Item.redstone), 'U', itemUpgrade);
		AromaRegistry.registerShapedAromicRecipe(
			new ItemStack(itemID, 1, Upgrade.COMPARATOR.ordinal()), false,
			" R ", "QUQ", " R ", 'Q', new ItemStack(Item.netherQuartz), 'R', new ItemStack(
				Item.redstone), 'U', itemUpgrade);
		// PLAYER
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 1, Upgrade.PLAYER.ordinal()), false,
			"OQO",
			"QUQ", "OQO", 'Q', new ItemStack(Item.netherQuartz), 'O',
			new ItemStack(Block.obsidian), 'U', itemUpgrade);
		// COBBLEGEN
		AromaRegistry.registerShapedAromicRecipe(
			new ItemStack(itemID, 1, Upgrade.COBBLEGEN.ordinal()), false, "CCC", "BUB",
			"CCC", 'C', "cobblestone", 'U',
			itemUpgrade, 'B', Item.bucketEmpty);
		// VOID
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 1, Upgrade.VOID.ordinal()), false,
			" E ", "RUR",
			" E ", 'E', new ItemStack(Item.enderPearl), 'R', new ItemStack(Item.redstone), 'U',
			itemUpgrade);
		// UNBREAKABLE
		AromaRegistry.registerShapedAromicRecipe(
			new ItemStack(itemID, 1, Upgrade.UNBREAKABLE.ordinal()), false, "OOO",
			"OUO", "OOO", 'O', new ItemStack(Block.obsidian), 'U', itemUpgrade);
		// RAIN
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 1, Upgrade.RAIN.ordinal()), false,
			" B ", "BUB",
			" B ", 'B', new ItemStack(Item.bucketEmpty), 'U', itemUpgrade);
		// ENERGY
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 1, Upgrade.ENERGY.ordinal()), false,
			"BRB", "CUC",
			"BRB", 'B', new ItemStack(Block.coalBlock), 'R', new ItemStack(Item.redstone),
			'C', new ItemStack(Item.redstoneRepeater), 'U', itemUpgrade);
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 1, Upgrade.ENERGY.ordinal()), false,
			"BCB", "RUR",
			"BCB", 'B', new ItemStack(Block.coalBlock), 'R', new ItemStack(Item.redstone),
			'C', new ItemStack(Item.redstoneRepeater), 'U', itemUpgrade);
		// FURNACE
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 1, Upgrade.FURNACE.ordinal()), false,
			"RFR", "FUF",
			"RFR", 'R', new ItemStack(Item.redstone), 'F', new ItemStack(Block.furnaceIdle), 'U',
			itemUpgrade);
		// COLLECTOR
		AromaRegistry.registerShapedAromicRecipe(
			new ItemStack(itemID, 1, Upgrade.COLLECTOR.ordinal()), false, " H ", "HUH",
			"RER", 'H', new ItemStack(Block.hopperBlock), 'R', new ItemStack(Item.redstone), 'E',
			new ItemStack(Item.enderPearl), 'U', itemUpgrade);
		// TICKING
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 1, Upgrade.TICKING.ordinal()), false,
			"QCQ", "RUR", "QCQ", 'Q', new ItemStack(Item.netherQuartz), 'C', new ItemStack(
				Item.pocketSundial), 'R', new ItemStack(Item.comparator), 'U', itemUpgrade);
		//FEEDING
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 1, FEEDING.ordinal()), false, " W ", "WUW", " W ", 'W', new ItemStack(Item.wheat), 'U', itemUpgrade);
		//PLAYERFEEDING
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 1, PLAYERFOOD.ordinal()), false, "FFF", "FUF", "FFF", 'F', ItemFood.class, 'U', itemUpgrade);
		//RESUPPLY
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(itemID, 1, RESUPPLY.ordinal()), false, "CUC", " H ", 'C', new ItemStack(Block.chest), 'H', new ItemStack(Block.hopperBlock), 'U', itemUpgrade);
		
		AromaRegistry.registerShapelessAromicRecipe(BASIC.getItem(), true, new ItemStack(itemID, 1, OreDictionary.WILDCARD_VALUE));
	}
	
	public int getMaxAmount() {
		return max;
	}
	
	public boolean canChestTakeUpgrade() {
		return canChest;
	}
	
	public boolean canBagTakeUpgrade() {
		return canBag;
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
