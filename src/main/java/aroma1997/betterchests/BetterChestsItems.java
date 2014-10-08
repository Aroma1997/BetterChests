package aroma1997.betterchests;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import aroma1997.core.util.AromaRegistry;
import aroma1997.core.util.registry.SpecialItemBlock;
import cpw.mods.fml.common.registry.GameRegistry;

public class BetterChestsItems {

	@SpecialItemBlock(ItemBlockBChest.class)
	public static BlockBChest chest = new BlockBChest();

	public static ItemUpgrade upgrade = new ItemUpgrade();

	public static ItemBag bag = new ItemBag();

	public static ItemBetterChestUpgrade upgradeChest = new ItemBetterChestUpgrade();

	public void init() {
		GameRegistry.registerTileEntity(TileEntityBChest.class,
				"adjustableChest");
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(chest), false,
				"CCC", "CBC", "CCC", 'C', "cobblestone", 'B', new ItemStack(
						Blocks.chest));
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(bag), false,
				"SWS", "LCL", "SWS", 'S', new ItemStack(Items.string), 'L',
				new ItemStack(Items.leather), 'W', new ItemStack(Blocks.wool,
						1, OreDictionary.WILDCARD_VALUE), 'C', new ItemStack(
						chest));
		GameRegistry.addRecipe(new CraftingBag());
		Upgrade.generateRecipes();
		AromaRegistry.registerShapelessAromicRecipe(BetterChests.getHelpBook(),
				false, Upgrade.BASIC.getItem(), new ItemStack(Items.book));
		GameRegistry.addRecipe(new CraftingBook());
	}

}
