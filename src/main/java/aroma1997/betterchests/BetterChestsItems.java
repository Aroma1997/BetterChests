package aroma1997.betterchests;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import aroma1997.core.util.AromaRegistry;
import aroma1997.core.util.registry.SpecialItemBlock;
import aroma1997.core.util.registry.SpecialName;
import cpw.mods.fml.common.registry.GameRegistry;

public class BetterChestsItems {

	@SpecialName("betterChest")
	@SpecialItemBlock(ItemBlockBChest.class)
	public static BlockBChest chest = new BlockBChest();

	@SpecialName("Upgrade")
	public static ItemUpgrade upgrade = new ItemUpgrade();
	
	public static ItemFilter filter = new ItemFilter();

	@SpecialName("Bag")
	public static ItemBag bag = new ItemBag();

	public static ItemBetterChestUpgrade upgradeChest = new ItemBetterChestUpgrade();

	public static void init() {
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
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(filter), false, " H ", "PUP", " P ", 'H', new ItemStack(Blocks.hopper), 'P', new ItemStack(Items.paper), 'U', new ItemStack(upgrade, 1, Upgrade.BASIC.ordinal()));
		GameRegistry.addRecipe(new CraftingFilter());
		AromaRegistry.registerShapelessAromicRecipe(BetterChests.getHelpBook(),
				false, Upgrade.BASIC.getItem(), new ItemStack(Items.book));
		GameRegistry.addRecipe(new CraftingBook());
	}

}
