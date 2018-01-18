package aroma1997.betterchests.upgrades.impl;

import net.minecraft.item.ItemStack;

import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradeVoid extends BasicUpgrade {
	public UpgradeVoid() {
		super(true, 1, UpgradableBlockType.VALUES);
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (!hasUpgradeOperationCost(chest)) {
			return;
		}

		if (chest instanceof IBetterChest) {
			IBetterChest inv = (IBetterChest) chest;
			int[] availableSlots = inv.getSlotsForFace(null);
			if (inv.getUpgradableBlockType() == UpgradableBlockType.BARREL || inv.getUpgradableBlockType() == UpgradableBlockType.PORTABLE_BARREL) {
				if (inv.decrStackSize(availableSlots[availableSlots.length - 1], Integer.MAX_VALUE).isEmpty()) {
					drawUpgradeOperationCode(chest);
					inv.markDirty();
				}
			} else {
				IFilter filter = inv.getFilterFor(stack);
				int idx = availableSlots[UpgradeHelper.INSTANCE.getFrequencyTick(chest, stack, availableSlots.length)];

				if (filter.matchesStack(inv.getStackInSlot(idx))) {
					inv.setInventorySlotContents(idx, ItemStack.EMPTY);
					drawUpgradeOperationCode(chest);
					inv.markDirty();
				}
			}
		}
	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energyVoid;
	}

}
