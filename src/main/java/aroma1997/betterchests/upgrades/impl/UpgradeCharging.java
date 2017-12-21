package aroma1997.betterchests.upgrades.impl;

import java.util.Collections;
import java.util.function.BiPredicate;

import net.minecraft.item.ItemStack;

import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.upgrades.BasicUpgrade;
import aroma1997.betterchests.upgrades.PowerUpgradeType;

public class UpgradeCharging extends BasicUpgrade {

	/**
	 * Used for IC2 compatibility.
	 */
	public static BiPredicate<ItemStack, IBetterChest> chargeItem = UpgradeCharging::chargeItem;

	public UpgradeCharging() {
		super(true, 1, UpgradableBlockType.NORMAL_INVENTORIES, () -> Collections.singletonList(PowerUpgradeType.CAPACITOR.getStack()));
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (!hasUpgradeOperationCost(chest)) {
			return;
		}
		IBetterChest inv = (IBetterChest) chest;
		int[] availableSlots = inv.getSlotsForFace(null);
		int idx = UpgradeHelper.INSTANCE.getFrequencyTick(chest, stack, availableSlots.length);
		ItemStack itemToCharge = inv.getStackInSlot(availableSlots[idx]);
		if (itemToCharge.getCount() == 1) {
			if (chargeItem.test(itemToCharge, inv)) {
				drawUpgradeOperationCode(chest);
			}
		}
	}

	private static boolean chargeItem(ItemStack stack, IBetterChest chest) {
		IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (storage != null) {
			int maxCharge = chest.getEnergyStorage().getEnergyStored();
			maxCharge = storage.receiveEnergy(maxCharge, false);
			if (maxCharge > 0) {
				chest.getEnergyStorage().extractEnergy(maxCharge, false);
				return true;
			}
		}
		return false;
	}
}
