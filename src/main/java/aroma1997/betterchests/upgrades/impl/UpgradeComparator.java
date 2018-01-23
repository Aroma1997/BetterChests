package aroma1997.betterchests.upgrades.impl;

import net.minecraft.item.ItemStack;

import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IBetterTank;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradeComparator extends BasicUpgrade {
	public UpgradeComparator() {
		super(false, 1, UpgradableBlockType.BLOCKS);
	}

	@Override
	public Number getChestModifier(IUpgradableBlock chest, ChestModifier modifier, ItemStack stack) {
		switch (modifier) {
		case COMPARATOR:
			if (chest instanceof IBetterChest) {
				IBetterChest inv = (IBetterChest) chest;
				float ratio = 0;
				int[] availableSlots = ((IBetterChest) chest).getSlotsForFace(null);
				for (int slot : availableSlots) {
					ItemStack currentStack = inv.getStackInSlot(slot);
					ratio += ((float)currentStack.getCount()) / (Math.min(currentStack.getMaxStackSize(), inv.getInventoryStackLimit()));
				}
				return Math.ceil(ratio / availableSlots.length * 15);
			} else if (chest instanceof IBetterTank) {
				IBetterTank inv = (IBetterTank) chest;
				return 15F * (float)inv.getTank().getFluidAmount() / inv.getTank().getCapacity() * 16;
			}
		default:
			return null;
		}
	}
}
