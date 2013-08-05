
package aroma1997.betterchests;


import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.Configuration;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class BetterChests {
	
	@Instance(Reference.MOD_ID)
	public static BetterChests instance;
	
	@SidedProxy(clientSide = "aroma1997.betterchests.client.ClientProxy", serverSide = "aroma1997.betterchests.CommonProxy")
	public static CommonProxy proxy;
	
	public static BlockBChest chest;
	
	public static ItemUpgrade upgrade;
	
	public static CreativeTabs creativeTabBC = new CreativeTabBChest();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(new File(new File(
			event.getModConfigurationDirectory(), "aroma1997"), Reference.MOD_ID + ".cfg"));
		config.load();
		chest = new BlockBChest(
			config.getBlock("chestID", 2540, "The Block id of the BetterChest").getInt());
		upgrade = new ItemUpgrade(
			config.getItem(Configuration.CATEGORY_ITEM, "upgradeItem", 12458, "The Item id of the Upgrades").getInt() - 256);
		config.save();
		GameRegistry.registerBlock(chest, ItemBlockBChest.class, "betterChest");
		GameRegistry.registerItem(upgrade, "Upgrade");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();
		GameRegistry.registerTileEntity(TileEntityBChest.class, "adjustableChest");
		LanguageRegistry.instance().addStringLocalization("itemGroup.creativeTabBC", "en_US",
			"BetterChests");
		GameRegistry.addRecipe(new ItemStack(chest), "CCC", "CBC", "CCC", 'C', new ItemStack(
			Block.cobblestone), 'B', new ItemStack(Block.chest));
		Upgrade.generateRecipes();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		return;
	}
	
}
