package aroma1997.betterchests.upgrades.impl;

import java.util.List;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;

import net.minecraftforge.common.IShearable;

import aroma1997.core.inventory.InvUtil;
import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradeAnimal extends BasicUpgrade {

	public UpgradeAnimal() {
		super(true, 1, UpgradableBlockType.CHEST.array);
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (chest.getWorldObj().isRemote || UpgradeHelper.INSTANCE.getFrequencyTick(chest, stack, 128) != 0) {
			return;
		}

		if (!hasUpgradeOperationCost(chest)) {
			return;
		}

		IBetterChest inv = (IBetterChest)chest;
		IFilter filter = inv.getFilterFor(stack);
		for (EntityAnimal animal : chest.getWorldObj().getEntitiesWithinAABB(EntityAnimal.class,
				new AxisAlignedBB(chest.getPosition()).grow(UpgradeBreeding.RADIUS))) {
			if (animal.isDead) {
				continue;
			}

			if (!hasUpgradeOperationCost(chest)) {
				break;
			}

			if (animal instanceof IShearable  && !(animal instanceof EntityMooshroom)&& InvUtil.findInInvInternal(inv, null, ItemStack::isEmpty) != -1) {
				IShearable shearable = (IShearable) animal;
				//We need to make sure we actually have a empty slot abvailable
				int idx = InvUtil.findInInvInternal(inv, null, test -> shearable.isShearable(stack, chest.getWorldObj(), chest.getPosition()));
				if (idx != -1) {
					ItemStack current = inv.getStackInSlot(idx);
					List<ItemStack> drop = shearable.onSheared(current, chest.getWorldObj(), chest.getPosition(), 0);
					for (ItemStack toInsert : drop) {
						InvUtil.putStackInInventoryFirst(toInsert, inv, true, false, false, null);
					}
					drawUpgradeOperationCode(chest);
				}
			} else if (animal instanceof EntityCow) {
				if (filter.matchesStack(new ItemStack(Items.BUCKET))) {
					int idx = InvUtil.findInInvInternal(inv, null, test -> test.getItem() == Items.BUCKET);
					if (idx != -1) {
						ItemStack current = inv.getStackInSlot(idx);
						if (InvUtil.putStackInInventoryFirst(new ItemStack(Items.MILK_BUCKET), inv, true, false, false, null).isEmpty()) {
							current.setCount(current.getCount() - 1);
							drawUpgradeOperationCode(chest);
						}
					}
				}
				if (animal instanceof EntityMooshroom && filter.matchesStack(new ItemStack(Items.BOWL))) {
					int idx = InvUtil.findInInvInternal(inv, null, test -> test.getItem() == Items.BOWL);
					if (idx != -1) {
						ItemStack current = inv.getStackInSlot(idx);
						if (InvUtil.putStackInInventoryFirst(new ItemStack(Items.MUSHROOM_STEW), inv, true, false, false, null).isEmpty()) {
							current.setCount(current.getCount() - 1);
							drawUpgradeOperationCode(chest);
						}
					}
				}
			}
		}
	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energyAnimal;
	}
}
