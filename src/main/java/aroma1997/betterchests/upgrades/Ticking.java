/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.upgrades;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;

import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.Reference;
import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IInventoryFilter;
import aroma1997.core.log.LogHelper;

public class Ticking extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		if (tick % 8 == 2) {
			return;
		}
		if (chest.getEnergyObject().getCurrent() < Reference.Conf.ENERGY_TICKING)
			return;
		List<IInventoryFilter> list = chest.getFiltersForUpgrade(item);
		try {
			for (int i = 0; i < chest.getSizeInventory(); i++) {
				ItemStack t = chest.getStackInSlot(i);
				if (t != null && UpgradeHelper.isItemAllowed(t, list)) {
					t.updateAnimation(world, null, i, true);
				}
			}
		} catch (NullPointerException e) {
			BetterChests.logger
					.log(Level.WARN,
							"BetterChests was unable to tick one of the contents in one of the chests.");
			BetterChests.logger
					.log(Level.WARN,
							"Disabling the Upgrade and continuing. This is not a BetterChests bug, just an information.");
			LogHelper.logException("Failed to use ticking upgrade on chest: "
					+ chest.toString() + " Coords: x=" + chest.getXCoord()
					+ " y=" + chest.getYCoord() + " z=" + chest.getZCoord(), e);
			chest.setUpgradeDisabled(item, true);
		} finally {
			chest.getEnergyObject().setCurrent(
					chest.getEnergyObject().getCurrent()
							- Reference.Conf.ENERGY_TICKING);
		}

	}

}
