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

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IUpgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
			if (isUpgrade(item)) {
				((IUpgrade) item.getItem()).update(inv, tick, world, item);
			}
		}
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
	
}
