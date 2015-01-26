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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import aroma1997.betterchests.InventoryFilter.BCFilterFilter;
import aroma1997.betterchests.Reference;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IInventoryFilter;
import aroma1997.core.util.InvUtil;

public class Energy extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		if (tick == 53) {
			int missing = chest.getEnergyObject().getMax()
					- chest.getEnergyObject().getCurrent();

			int fuelSlot = InvUtil.getFirstItem(chest, Item.class, null,
					InvUtil.fuelFilter);
			if (fuelSlot == -1)
				return;

			int fuelAmount = TileEntityFurnace.getItemBurnTime(chest
					.getStackInSlot(fuelSlot))
					* Reference.Conf.ENERGY_FUEL_MULTIPLIER;

			if (missing >= fuelAmount) {
				chest.getEnergyObject().add(fuelAmount);
				chest.decrStackSize(fuelSlot, 1);
			}

		}
	}

	private static class FuelSpecialFilter extends BCFilterFilter {

		public FuelSpecialFilter(List<IInventoryFilter> list) {
			super(list);
		}

		@Override
		public boolean isOk(ItemStack items) {
			return super.isOk(items) && InvUtil.fuelFilter.isOk(items);
		}

	}

}
