package aroma1997.betterchests.integration.ic2;

import net.minecraft.item.ItemStack;

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.planter.PlantHarvestRegistry;
import aroma1997.betterchests.upgrades.impl.UpgradeCharging;

import ic2.api.energy.EnergyNet;
import ic2.api.item.ElectricItem;

public class Ic2Integration {

	static final double EU_PER_FE = 1/8F;

	static {
		ElectricItem.registerBackupManager(new ItemHandler());
		new BlockHandler();
		//TODO: add cropstick placing
		PlantHarvestRegistry.INSTANCE.register(new Ic2CropHandler());

		UpgradeCharging.chargeItem = UpgradeCharging.chargeItem.or(Ic2Integration::handleCharge);
	}

	public static boolean handleCharge(ItemStack stack, IBetterChest chest) {
		double maxCharge = chest.getEnergyStorage().getEnergyStored() * EU_PER_FE;
		maxCharge = Math.min(EnergyNet.instance.getPowerFromTier(ElectricItem.manager.getTier(stack)) * 100, maxCharge);

		maxCharge = ElectricItem.manager.charge(stack, maxCharge, Integer.MAX_VALUE, true, false);
		if (maxCharge > 0) {
			chest.getEnergyStorage().extractEnergy((int)Math.ceil(maxCharge / EU_PER_FE), false);
			return true;
		}
		return false;
	}
}
