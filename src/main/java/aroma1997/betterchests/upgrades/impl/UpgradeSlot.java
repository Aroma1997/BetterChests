package aroma1997.betterchests.upgrades.impl;

import net.minecraft.item.ItemStack;

import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradeSlot extends BasicUpgrade {

	public UpgradeSlot() {
		super(false, 7, UpgradableBlockType.VALUES);
	}

	@Override
	public Number getChestModifier(IUpgradableBlock chest, ChestModifier modifier, ItemStack stack) {
		switch (modifier) {
		case SIZE:
			return 9;
		default:
			return null;
		}
	}
}
