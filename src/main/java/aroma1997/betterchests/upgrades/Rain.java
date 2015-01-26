/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.upgrades;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

public class Rain extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		if (tick != 26) {
			return;
		}
		ItemStack bucket = InvUtil.getFirstItem(chest, new ItemStack(
				Items.bucket), true);
		if (bucket != null) {
			bucket = bucket.copy();
			bucket.stackSize = 1;
			if (InvUtil.putIntoFirstSlot(chest, new ItemStack(
					Items.water_bucket), false) == null) {
				InvUtil.getFirstItem(chest, bucket, false);
			}
		}
	}

}
