package aroma1997.betterchests.upgrades.impl;

import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import aroma1997.core.inventory.InvUtil;
import aroma1997.betterchests.BlocksItemsBetterChests;
import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.upgrades.BasicUpgrade;
import aroma1997.betterchests.upgrades.UpgradeType;

public class UpgradeCollector extends BasicUpgrade {
	public UpgradeCollector() {
		super(true, 5, UpgradableBlockType.INVENTORIES);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (!UpgradeHelper.INSTANCE.isFirstUpgrade(chest, stack) || UpgradeHelper.INSTANCE.getFrequencyTick(chest, stack, 5) != 0
				|| chest.getWorldObj().isRemote) {
			return;
		}

		IBetterChest inv = (IBetterChest) chest;
		IFilter filter = inv.getFilterFor(stack);

		int radius = chest.getAmountUpgrades(stack);
		double pickupRadius = 0.5;
		Vec3d pos = chest.getPositionPrecise();

		AxisAlignedBB bb = new AxisAlignedBB(chest.getPosition()).grow(pickupRadius);

		Predicate<EntityItem> predicate = entity -> !entity.isDead && !entity.cannotPickup() && filter.matchesStack(entity.getItem());

		List<EntityItem> entities = chest.getWorldObj().getEntitiesWithinAABB(EntityItem.class, bb, predicate);

		for (EntityItem entity : entities) {
			ItemStack entityStack = entity.getItem();
			if (UpgradeHelper.INSTANCE.getInventory(entityStack, entity) == null && hasUpgradeOperationCost(chest)) {
				entity.setItem(InvUtil.putStackInInventoryInternal(entityStack, inv, false));
				drawUpgradeOperationCode(chest);
				inv.markDirty();
			}
		}

		bb = new AxisAlignedBB(chest.getPosition()).grow(radius);

		entities = chest.getWorldObj().getEntitiesWithinAABB(EntityItem.class, bb, predicate);

		double multiplier = 0.2D;

		for (EntityItem entity : entities) {
			Vec3d diff = pos.subtract(entity.getPositionVector()).scale(multiplier);
			entity.addVelocity(diff.x, diff.y, diff.z);
		}
	}

	@SubscribeEvent
	public void pickupItem(EntityItemPickupEvent event) {
		for (int i = 0; i < event.getEntityPlayer().inventory.getSizeInventory() && !event.getItem().getItem().isEmpty(); i++) {
			IUpgradableBlock chest = UpgradeHelper.INSTANCE.getInventory(event.getEntityPlayer().inventory.getStackInSlot(i), event.getEntity());
			if (chest != null) {
				if (!hasUpgradeOperationCost(chest)) {
					continue;
				}

				for (ItemStack upgrade : chest.getActiveUpgrades()) {
					if (upgrade.getItem() == BlocksItemsBetterChests.upgrade && BlocksItemsBetterChests.upgrade.getType(upgrade) == UpgradeType.COLLECTOR) {
						IBetterChest inv = (IBetterChest) chest;
						IFilter filter = ((IBetterChest) chest).getFilterFor(upgrade);
						if (filter.matchesStack(event.getItem().getItem())) {
							ItemStack prev = event.getItem().getItem();
							event.getItem().setItem(InvUtil.putStackInInventoryInternal(event.getItem().getItem(), inv, false));

							if (prev.getCount() != event.getItem().getItem().getCount()) {
								event.setCanceled(true);
								drawUpgradeOperationCode(chest);
								((IBetterChest) chest).markDirty();
							}
						}
						break;
					}
				}
			}
		}
	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energyCollector;
	}
}
