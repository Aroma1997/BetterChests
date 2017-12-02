package aroma1997.betterchests.upgrades.impl;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import aroma1997.core.inventory.InvUtil;
import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradeFurnace extends BasicUpgrade {
	public UpgradeFurnace() {
		super(true, 1, UpgradableBlockType.INVENTORIES);
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (UpgradeHelper.INSTANCE.getFrequencyTick(chest, stack, 16) != 0 || !hasUpgradeOperationCost(chest) || chest.getWorldObj().isRemote) {
			return;
		}

		IBetterChest inv = (IBetterChest) chest;
		IFilter filter = inv.getFilterFor(stack);
		int toSmeltSlot = InvUtil.findInInv(inv, true, false, null, test -> !FurnaceRecipes.instance().getSmeltingResult(test).isEmpty() && filter.matchesStack(test));
		if (toSmeltSlot != -1) {
			ItemStack target = inv.getStackInSlot(toSmeltSlot);
			ItemStack result = FurnaceRecipes.instance().getSmeltingResult(target);
			if (InvUtil.putStackInInventory(result, inv, true, false, true, null).isEmpty()) {
				InvUtil.putStackInInventory(result, inv, true, false, false, null);
				inv.decrStackSize(toSmeltSlot, 1);
				drawUpgradeOperationCode(chest);
				inv.markDirty();
			}
		}
	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energySmelting;
	}
}
