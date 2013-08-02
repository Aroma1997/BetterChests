package aroma1997.betterchests;

import java.io.File;

import net.minecraftforge.common.Configuration;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class BetterChests {
	
	@Instance(Reference.MOD_ID)
	public static BetterChests instance;
	
	@SidedProxy(clientSide = "aroma1997.betterchests.client.ClientProxy", serverSide = "aroma1997.betterchests.CommonProxy")
	public static CommonProxy proxy;
	
	public static BlockBChest chest;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(new File(new File(event.getModConfigurationDirectory(), "aroma1997"), Reference.MOD_ID + ".cfg"));
		config.load();
		chest = new BlockBChest(config.getBlock("chestID", 2540, "The Block id of the BetterChest").getInt());
		config.save();
		GameRegistry.registerBlock(chest, "Adjustable Chest");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();
		GameRegistry.registerTileEntity(TileEntityBChest.class, "adjustableChest");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		return;
	}
	
}
