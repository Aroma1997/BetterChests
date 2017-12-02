package aroma1997.betterchests.integration.ic2;

import aroma1997.betterchests.api.planter.PlantHarvestRegistry;

import ic2.api.item.ElectricItem;

public class Ic2Integration {

	static final double EU_PER_FE = 1/8F; //0.25;

	static {
		ElectricItem.registerBackupManager(new ItemHandler());
		new BlockHandler();
		//TODO: add cropstick placing
		PlantHarvestRegistry.INSTANCE.register(new Ic2CropHandler());
	}
}
