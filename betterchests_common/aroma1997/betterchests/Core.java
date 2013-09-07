package aroma1997.betterchests;

import aroma1997.core.version.VersionCheck;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;


public class Core {
	
	public static void init(FMLInitializationEvent event) {
		VersionCheck.registerVersionChecker(Reference.MOD_ID, Reference.VERSION);
	}
	
	public static void postInit(FMLPostInitializationEvent event) {
		
	}
	
}
