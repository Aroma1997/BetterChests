/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.IBetterChest;

public class VoidUpgrade extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		int i = (int) (chest.getLongTick() % chest.getSizeInventory());
		ItemStack stack = chest.getStackInSlot(i);
		if (stack != null) {
			if (UpgradeHelper.isItemAllowed(stack,
					chest.getFiltersForUpgrade(item))) {
				chest.setInventorySlotContents(i, null);
			}
		}

	}

}
