package aroma1997.betterchests.upgrades.power;

import net.minecraft.item.ItemStack;

import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;

public class UpgradeSolar extends PowerBaseUpgrade {
	public UpgradeSolar() {
		super(false, 8, UpgradableBlockType.VALUES);
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (chest.getWorldObj().canBlockSeeSky(chest.getPosition()) && chest.getWorldObj().isDaytime()) {
			chest.getEnergyStorage().receiveEnergy(getPowerProvided(), false);
		}
	}

	@Override
	public int getPowerProvided() {
		return Config.INSTANCE.energySolar;
	}
}
