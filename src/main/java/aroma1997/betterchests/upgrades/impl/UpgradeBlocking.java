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
		super(false, Integer.MAX_VALUE, new UpgradableBlockType[]{UpgradableBlockType.CHEST, UpgradableBlockType.BARREL, UpgradableBlockType.PORTABLE_BARREL});
	}

	@Override
	public Number getChestModifier(IUpgradableBlock chest, ChestModifier modifier, ItemStack stack) {
		switch (modifier) {
		case SIZE_BEGIN:
			return AMOUNT_BLOCKED_SLOTS;
		case ACCEPTANCE_LOCK:
			return 1;
		default:
			return null;
		}
	}

	@Override
	public boolean canBePutInChest(IUpgradableBlock chest, ItemStack stack) {
		return super.canBePutInChest(chest, stack) &&
				(chest.getUpgradableBlockType() != UpgradableBlockType.BARREL && isOk(chest, stack, chest.getAmountUpgrades(stack)) ||
				chest.getUpgradableBlockType() == UpgradableBlockType.BARREL && chest.getAmountUpgrades(stack) == 0);
	}

	@Override
	public boolean areRequirementsMet(IUpgradableBlock chest, ItemStack stack) {
		return super.areRequirementsMet(chest, stack) &&
				(chest.getUpgradableBlockType() == UpgradableBlockType.BARREL ||
				isOk(chest, stack, chest.getAmountUpgrades(stack) - 1));
	}

	private boolean isOk(IUpgradableBlock block, ItemStack upgrade, int amount) {
		return amount * AMOUNT_BLOCKED_SLOTS < ((IBetterChest)block).getSlotsForFace(null).length;
	}
}
