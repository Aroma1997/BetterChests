package aroma1997.betterchests.upgrades.power;

import net.minecraft.item.ItemStack;

import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;

public class UpgradeEnergyCreative extends PowerBaseUpgrade {

	private static final int energy = 100000;

	public UpgradeEnergyCreative() {
		super(false, 1, UpgradableBlockType.VALUES);
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		chest.getEnergyStorage().receiveEnergy(getPowerProvided(), false);
	}

	@Override
	public Number getChestModifier(IUpgradableBlock chest, ChestModifier modifier, ItemStack stack) {
		switch (modifier) {
		case ENERGY_CAPACITY:
			return energy;
		}
		return null;
	}

	@Override
	public int getPowerProvided() {
		return energy;
	}
}
