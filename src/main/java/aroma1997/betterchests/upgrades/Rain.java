package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Rain extends BasicUpgrade {
	
	@Override
	public void updateChest(IBetterChest chest, int tick, World world, ItemStack item) {
		if (tick != 26) {
			return;
		}
		ItemStack bucket = InvUtil.getFirstItem(chest, new ItemStack(Items.bucket), true);
		if (bucket != null) {
			bucket = bucket.copy();
			bucket.stackSize = 1;
			if (InvUtil.putIntoFirstSlot(chest, new ItemStack(Items.water_bucket), false) == null) {
				InvUtil.getFirstItem(chest, bucket, false);
			}
		}
	}
	
}
