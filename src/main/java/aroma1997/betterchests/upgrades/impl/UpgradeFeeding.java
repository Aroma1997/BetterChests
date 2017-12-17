package aroma1997.betterchests.upgrades.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import aroma1997.core.inventory.InvUtil;
import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IMobileUpgradableBlock;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradeFeeding extends BasicUpgrade {
	public UpgradeFeeding() {
		super(false, 1, new UpgradableBlockType[]{UpgradableBlockType.BAG, UpgradableBlockType.PORTABLE_BARREL});
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (UpgradeHelper.INSTANCE.getFrequencyTick(chest, stack, 100) != 0) {
			return;
		}

		Entity e = ((IMobileUpgradableBlock)chest).getEntity();
		if (e instanceof EntityPlayer && hasUpgradeOperationCost(chest)) {
			EntityPlayer player = (EntityPlayer) e;
			if (!player.getFoodStats().needFood()) {
				return;
			}
			IBetterChest inv = (IBetterChest) chest;
			int idx = InvUtil.findInInvInternal(inv, null, test -> test.getItem() instanceof ItemFood);
			if (idx != -1) {
				ItemStack food = inv.getStackInSlot(idx);

				if (20 - player.getFoodStats().getFoodLevel() >= ((ItemFood)food.getItem()).getHealAmount(food)
						|| player.getFoodStats().getFoodLevel() <= 17 && player.getHealth() <= player.getMaxHealth() - 1.0F
						|| player.getFoodStats().getFoodLevel() <= 6) {
					inv.setInventorySlotContents(idx, ((ItemFood)food.getItem()).onItemUseFinish(food, chest.getWorldObj(), player));
					drawUpgradeOperationCode(chest);
					inv.markDirty();
				}
			}
		}
	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energyFeeding;
	}
}
