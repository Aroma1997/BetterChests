package aroma1997.betterchests.upgrades.power;

import net.minecraft.item.ItemStack;

import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;

public class UpgradeCapacitor extends PowerBaseUpgrade {
	public UpgradeCapacitor() {
		super(false, Integer.MAX_VALUE, UpgradableBlockType.VALUES);
	}

	@Override
	public Number getChestModifier(IUpgradableBlock chest, ChestModifier modifier, ItemStack stack) {
		switch (modifier) {
		case ENERGY_CAPACITY:
			return 100000;
		}
		return null;
	}
}
