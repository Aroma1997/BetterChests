/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;


import java.io.File;
import java.util.ArrayList;

import aroma1997.betterchests.client.EventListener;
import aroma1997.core.log.AromaLog;
import aroma1997.core.log.LogHelper;
import aroma1997.core.util.AromaRegistry;
import aroma1997.core.util.ItemUtil;
import aroma1997.core.version.VersionCheck;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.Configuration;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:Aroma1997Core")
@NetworkMod(channels = {"BetterChests"}, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class BetterChests {
	
	@Instance(Reference.MOD_ID)
	public static BetterChests instance;
	
	@SidedProxy(clientSide = "aroma1997.betterchests.client.ClientProxy", serverSide = "aroma1997.betterchests.CommonProxy")
	public static CommonProxy proxy;
	
	public static BlockBChest chest;
	
	public static ItemUpgrade upgrade;
	
	public static ItemBag bag;
	
	public static ItemTool tool;
	
	public static CreativeTabs creativeTabBC = new CreativeTabBChest();
	
	public static AromaLog logger;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = LogHelper.genNewLogger(Reference.MOD_ID);
		Configuration config = new Configuration(new File(new File(
			event.getModConfigurationDirectory(), "aroma1997"), Reference.MOD_ID + ".cfg"));
		config.load();
		chest = new BlockBChest(
			config.getBlock("chestID", 2540, "The Block id of the BetterChest").getInt());
		upgrade = new ItemUpgrade(
			config.getItem(Configuration.CATEGORY_ITEM, "upgradeItem", 12458,
				"The Item id of the Upgrades").getInt() - 256);
		bag = new ItemBag(config.getItem("bagItem", 12457, "The item id of the Bag").getInt() - 256);
		tool = new ItemTool(
			config.getItem("itemTool", 12456, "The Item id of the Tool").getInt() - 256);
		if (config.hasChanged()) {
			config.save();
		}
		
		GameRegistry.registerBlock(chest, ItemBlockBChest.class, "betterChest");
		GameRegistry.registerItem(upgrade, "Upgrade");
		GameRegistry.registerItem(bag, "Bag");
		GameRegistry.registerItem(tool, "Tool");
		new EventListener();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();
		GameRegistry.registerTileEntity(TileEntityBChest.class, "adjustableChest");
		AromaRegistry.registerShapedOreRecipe(new ItemStack(chest), "CCC", "CBC", "CCC", 'C',
			"cobblestone", 'B', new ItemStack(Block.chest));
		AromaRegistry.registerShapedOreRecipe(new ItemStack(bag), "SWS", "LCL", "SWS", 'S',
			new ItemStack(Item.silk), 'L', new ItemStack(Item.leather), 'W', new ItemStack(
				Block.cloth), 'C', new ItemStack(chest));
		GameRegistry.addRecipe(new CraftingBag());
		Upgrade.generateRecipes();
		Tool.generateRecipes();
		AromaRegistry.registerShapelessOreRecipe(getHelpBook(), Upgrade.BASIC.getItem(),
			new ItemStack(Item.book));
		GameRegistry.addRecipe(new CraftingBook());
		
		VersionCheck.registerVersionChecker(Reference.MOD_ID, Reference.VERSION);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
	public static ItemStack getHelpBook() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("book.betterchests:introduction");
		list.add("book.betterchests:chapter.general");
		list.add("book.betterchests:general.1");
		list.add("book.betterchests:general.2");
		list.add("book.betterchests:general.3");
		list.add("book.betterchests:chest.1");
		list.add("book.betterchests:bag.1");
		list.add("book.betterchests:bag.2");
		list.add("book.betterchests:chapter.tools");
		Tool.addBookDescription(list);
		list.add("book.betterchests:chapter.upgrades");
		Upgrade.addBagBookDescription(list);
		list.add("book.betterchests:chapter.credits");
		list.add("book.betterchests:credits");
		ItemStack item = ItemUtil.getWrittenBook("book.betterchests:name", "Aroma1997", true,
			list.toArray(new String[list.size()]));
		item.getTagCompound().setString("id", "BetterChests");
		return item;
	}
	
}
