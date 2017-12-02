package aroma1997.betterchests.upgrades.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import aroma1997.core.inventory.InvUtil;
import aroma1997.core.util.ItemUtil;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.IMobileUpgradableBlock;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradeResupply extends BasicUpgrade {
	public UpgradeResupply() {
		super(true, 1, UpgradableBlockType.BAG.array);
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		IBetterChest inv = (IBetterChest)chest;
		IMobileUpgradableBlock bag = (IMobileUpgradableBlock) inv;
		Entity entity = bag.getEntity();
		if (entity instanceof EntityPlayer) {
			InventoryPlayer playerInv = ((EntityPlayer)entity).inventory;
			int currentSlot = UpgradeHelper.INSTANCE.getFrequencyTick(chest, stack, playerInv.getSizeInventory());
			IFilter filter = inv.getFilterFor(stack);
			ItemStack currentStack = playerInv.getStackInSlot(currentSlot);
			if (!currentStack.isEmpty() && filter.matchesStack(currentStack)) {
				int needed = (int) (currentStack.getMaxStackSize() / 2F + .5F) - currentStack.getCount();
				if (needed > 0) {
					int match = InvUtil.findInInvInternal(inv, null, test -> ItemUtil.areItemsSameMatchingIdDamageNbt(test, currentStack));
					if (match != -1) {
						ItemStack fromStack = inv.getStackInSlot(match);
						int toAdd = Math.min(needed, fromStack.getCount());
						currentStack.setCount(currentStack.getCount() + toAdd);
						fromStack.setCount(fromStack.getCount() - toAdd);
						inv.markDirty();
					}
				}
			}
		}
	}
}
