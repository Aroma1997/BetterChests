/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IInventoryFilter;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.core.log.LogHelper;
import aroma1997.core.util.ItemUtil;
import aroma1997.core.util.ItemUtil.ItemMatchCriteria;
import aroma1997.core.util.ItemUtil.ItemMatchCriteria.ICompareItems;

public class UpgradeHelper {

	static void updateChest(IBetterChest inv, int tick, World world) {
		if (world.isRemote) {
			return;
		}
		ArrayList<ItemStack> upgrades = inv.getUpgrades();
		if (upgrades == null) {
			return;
		}

		for (ItemStack item : upgrades) {
			if (isUpgrade(item) && !inv.isUpgradeDisabled(item)) {
				try {
					((IUpgrade) item.getItem()).update(inv, tick, world, item);
				} catch (Throwable t) {
					LogHelper.logException("Failed to do Upgrade ticking: "
							+ item.toString() + " at " + inv.toString(), t);
					inv.setUpgradeDisabled(item, true);
				}
			}
		}
	}

	public static boolean canBeDisabled(ItemStack stack) {
		return UpgradeHelper.isUpgrade(stack)
				&& ((IUpgrade) stack.getItem()).canBeDisabled(stack);
	}

	public static boolean isRequirement(ItemStack req, ItemStack up) {
		if (!isUpgrade(req) || !isUpgrade(up)) {
			return false;
		}
		IUpgrade iup = (IUpgrade) up.getItem();
		List<ItemStack> reqlist = iup.getRequiredUpgrade(up);
		if (reqlist == null) {
			return false;
		}
		for (ItemStack item : reqlist) {
			if (!isUpgrade(item)) {
				continue;
			}
			if (ItemUtil.areItemsSameMatching(item, req, ItemMatchCriteria.ID,
					ItemMatchCriteria.DAMAGE)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isUpgrade(ItemStack item) {
		return item != null && item.getItem() instanceof IUpgrade;
	}

	public static ItemStack getDefaultItem(ItemStack item) {
		if (!isUpgrade(item)) {
			return null;
		}
		item = item.copy();
		enableUpgrade(item);
		item.stackSize = 1;
		return item;
	}

	public static boolean areRequirementsInstalled(IBetterChest chest,
			ItemStack item) {
		if (chest == null || item == null || !isUpgrade(item)) {
			return false;
		}

		IUpgrade upgrade = (IUpgrade) item.getItem();
		if (upgrade == null) {
			return false;
		}
		if (upgrade.getRequiredUpgrade(item) == null) {
			return true;
		}
		for (ItemStack req : upgrade.getRequiredUpgrade(item)) {
			if (!chest.isUpgradeInstalled(req)) {
				return false;
			}
		}
		return true;

	}

	public static boolean isRequirement(ItemStack item, IBetterChest inv) {
		if (item == null || !isUpgrade(item)) {
			return false;
		}

		for (int i = 0; i < inv.getUpgrades().size(); i++) {
			ItemStack itemtmp = inv.getUpgrades().get(i);
			if (itemtmp == null || !isUpgrade(itemtmp)) {
				continue;
			}
			if (isRequirement(item, itemtmp)) {
				return true;
			}
		}

		return false;
	}

	public static void enableUpgrade(ItemStack upgrade) {
		if (isUpgrade(upgrade)) {
			if (upgrade.hasTagCompound()
					&& upgrade.getTagCompound().getBoolean("disabled")) {
				upgrade.getTagCompound().removeTag("disabled");
				if (upgrade.getTagCompound().hasNoTags()) {
					upgrade.setTagCompound(null);
				}
			}
		}
	}

	public static boolean canUpgradeGoInChest(IBetterChest chest, ItemStack item) {
		if (item == null || chest == null || !isUpgrade(item))
			return false;
		IUpgrade upgrade = (IUpgrade) item.getItem();
		return ((chest instanceof TileEntityBChest && upgrade
				.canChestTakeUpgrade(item)) || (chest instanceof BagInventory && upgrade
				.canBagTakeUpgrade(item)))
				&& areRequirementsInstalled(chest, item)
				&& (upgrade.getMaxUpgrades(item) > chest.getAmountUpgrade(item) || (upgrade
						.getMaxUpgrades(item) == -1 && chest
						.getAmountUpgradeExact(item) == 0));
	}

	public static ItemMatchCriteria BC_NBT = new ItemMatchCriteria(
			BetterChestsNBT.class);

	public static class BetterChestsNBT implements ICompareItems {

		@Override
		public boolean checkFor(ItemStack item1, ItemStack item2) {
			if (item1 == null || item2 == null)
				return false;
			ItemStack item1c = item1.copy();
			ItemStack item2c = item2.copy();
			enableUpgrade(item1c);
			enableUpgrade(item2c);

			return ItemUtil.areItemsSameMatching(item1c, item2c,
					ItemMatchCriteria.NBT);
		}

	}

	public static boolean isItemAllowed(ItemStack item,
			List<IInventoryFilter> list) {
		if (list == null || list.isEmpty()) {
			return true;
		}
		boolean hasWhitelist = false;
		boolean isOnWhitelist = false;

		for (IInventoryFilter filter : list) {
			if (filter.isWhitelist()) {
				hasWhitelist = true;
			}
			for (int i = 0; i < filter.getSizeInventory(); i++) {
				if (i == InventoryFilter.SLOT_UPGRADE)
					continue;
				if (ItemUtil.areItemsSameMatching(item,
						filter.getStackInSlot(i), ItemMatchCriteria.ID,
						ItemMatchCriteria.DAMAGE)) {
					if (filter.isWhitelist()) {
						isOnWhitelist = true;
					} else {
						return false;
					}
				}
			}
		}
		if (hasWhitelist) {
			return isOnWhitelist;
		}
		return true;

	}

}
