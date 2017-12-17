package aroma1997.betterchests.upgrades.impl;

import java.util.Collections;

import net.minecraft.item.ItemStack;

import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.upgrades.BasicUpgrade;
import aroma1997.betterchests.upgrades.DummyUpgradeType;

public class UpgradeTicking extends BasicUpgrade {
	public UpgradeTicking() {
		super(true, 1, UpgradableBlockType.NORMAL_INVENTORIES,
				() -> Collections.singletonList(DummyUpgradeType.AI.getStack()));
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		IBetterChest inv = (IBetterChest) chest;

		int[] availableSlots = inv.getSlotsForFace(null);

		ItemStack toTick = inv.getStackInSlot(availableSlots[(int) (chest.getWorldObj().getWorldTime() % availableSlots.length)]);

		if (!hasUpgradeOperationCost(chest)) {
			return;
		}

		if (!toTick.isEmpty()) {
			drawUpgradeOperationCode(chest);
		}
		try {
			toTick.updateAnimation(chest.getWorldObj(), null, 0, true);
			inv.markDirty();
		} catch (Exception e) {
			e.printStackTrace();
			chest.setUpgradeDisabled(stack, true);
		}
	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energyTicking;
	}
}