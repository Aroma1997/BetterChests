package aroma1997.betterchests;

import org.apache.logging.log4j.Level;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import aroma1997.core.item.AromicCreativeTab;
import aroma1997.core.log.LogHelper;
import aroma1997.core.network.NetworkHelper;
import aroma1997.core.network.PacketHandler;
import aroma1997.core.util.Util;
import aroma1997.core.util.registry.AromaAutoRegistry;
import aroma1997.betterchests.network.PacketOpenBag;
import aroma1997.betterchests.upgrades.impl.plant.PlantHarvestHelper;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_ID, dependencies = "required-after:aroma1997core", certificateFingerprint = "dfbfe4c473253d8c5652417689848f650b2cbe32")
public class BetterChests {

	public static final AromicCreativeTab creativeTab = new AromicCreativeTab("betterchests:creativetab", () -> new ItemStack(BlocksItemsBetterChests.betterchest));

	@Instance
	public static BetterChests instance;

	public PacketHandler ph;

	@SidedProxy(serverSide = "aroma1997.betterchests.CommonProxy", clientSide = "aroma1997.betterchests.client.ClientProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		AromaAutoRegistry.INSTANCE.registerClassWrapper(BlocksItemsBetterChests.class, null);
		proxy.preInit();
		Config.INSTANCE.load();
		PlantHarvestHelper.INSTANCE.prepareLoading();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
		ph = NetworkHelper.getPacketHandler(Reference.MOD_ID);
		ph.registerMessage(PacketOpenBag.class, PacketOpenBag.class, 0, Side.SERVER);
		aroma1997.betterchests.api.UpgradeHelper.INSTANCE = UpgradeHelper.INSTANCE;

		if (Loader.isModLoaded("ic2")) {
			Util.forceLoadClass("aroma1997.betterchests.integration.ic2.Ic2Integration");
		}
		if (Loader.isModLoaded("storagedrawers")) {
			Util.forceLoadClass("aroma1997.betterchests.integration.storagedrawers.StorageDrawersIntegration");
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PlantHarvestHelper.INSTANCE.finishLoading();
		OreDictionary.registerOre("chest", BlocksItemsBetterChests.betterchest);
		OreDictionary.registerOre("drawerBasic", BlocksItemsBetterChests.betterbarrel);
	}

	@EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
		LogHelper.log(Level.WARN, "Invalid fingerprint detected! The version of the mod is most likely modified and an inofficial release.");
		LogHelper.log(Level.WARN, "Use with caution.");

	}
}
