package aroma1997.betterchests.upgrades.impl;

import net.minecraft.item.ItemStack;

import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradeBlocking extends BasicUpgrade {

	private static final int AMOUNT_BLOCKED_SLOTS = 9;

	public UpgradeBlocking() {
		super(false, Integer.MAX_VALUE, UpgradableBlockType.CHEST.array);
	}

	@Override
	public Number getChestModifier(IUpgradableBlock chest, ChestModifier modifier, ItemStack stack) {
		switch (modifier) {
		case SIZE_BEGIN:
			return AMOUNT_BLOCKED_SLOTS;
		default:
			return null;
		}
	}

	@Override
	public boolean canBePutInChest(IUpgradableBlock chest, ItemStack stack) {
		return super.canBePutInChest(chest, stack) && isOk(chest, stack, chest.getAmountUpgrades(stack));
	}

	@Override
	public boolean areRequirementsMet(IUpgradableBlock chest, ItemStack stack) {
		return super.areRequirementsMet(chest, stack) && isOk(chest, stack, chest.getAmountUpgrades(stack) - 1);
	}

	private boolean isOk(IUpgradableBlock block, ItemStack upgrade, int amount) {
		return amount * AMOUNT_BLOCKED_SLOTS < ((IBetterChest)block).getSlotsForFace(null).length;
	}
}
