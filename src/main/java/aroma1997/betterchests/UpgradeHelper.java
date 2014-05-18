/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.core.util.ItemUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

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
				((IUpgrade) item.getItem()).update(inv, tick, world, item);
			}
		}
	}
	
	public static boolean isRequirement(ItemStack req, ItemStack up) {
		if (! isUpgrade(req) || ! isUpgrade(up)) {
			return false;
		}
		IUpgrade iup = (IUpgrade) up.getItem();
		List<ItemStack> reqlist = iup.getRequiredUpgrade(up);
		if (reqlist == null) {
			return false;
		}
		for (ItemStack item : reqlist) {
			if (! isUpgrade(item)) {
				continue;
			}
			if (ItemUtil.areItemsSame(item, req)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isUpgrade(ItemStack item) {
		return item != null && item.getItem() instanceof IUpgrade;
	}
	
	public static ItemStack getDefaultItem(ItemStack item) {
		if (! isUpgrade(item)) {
			return null;
		}
		item = item.copy();
		item.stackSize = 1;
		return item;
	}
	
	public static boolean areRequirementsInstalled(IBetterChest chest, ItemStack item) {
		if (chest == null || item == null || ! isUpgrade(item)) {
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
			if (! chest.isUpgradeInstalled(req)) {
				return false;
			}
		}
		return true;
		
	}
	
	public static boolean isRequirement(ItemStack item, IBetterChest inv) {
		if (item == null || ! isUpgrade(item)) {
			return false;
		}
		
		for (int i = 0; i < inv.getUpgrades().size(); i++ ) {
			ItemStack itemtmp = inv.getUpgrades().get(i);
			if (itemtmp == null || ! isUpgrade(itemtmp)) {
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
			if (upgrade.hasTagCompound() && upgrade.stackTagCompound.getBoolean("disabled")) {
				upgrade.stackTagCompound.removeTag("disabled");
				if (upgrade.stackTagCompound.hasNoTags()) {
					upgrade.stackTagCompound = null;
				}
			}
		}
	}
	
}
