package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.Reference;
import aroma1997.betterchests.Upgrade;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Rain extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		if (tick != 26)
			return;
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
		if (InvUtil.getFirstItem(chest, new ItemStack(Items.water_bucket), true) != null && chest.isUpgradeInstalled(Upgrade.PLANTING.getItem())) {
			int range = chest.getAmountUpgrade(Upgrade.PLANTING.getItem()) * Reference.Conf.PLANTS_RANGE_MULTIPLIER;
			 for (int x = -range; x <= range; x++) {
				 for (int z = -range; z <= range; z++) {
					 for (int y = Reference.Conf.PLANTS_START; y <= Reference.Conf.PLANTS_HEIGHT + Reference.Conf.PLANTS_START; y++) {
						 int xcoord = (int) chest.getXPos() + x;
						 int ycoord = (int) chest.getYPos() + y;
						 int zcoord = (int) chest.getYPos() + y;
						 Block block = world.getBlock(xcoord, ycoord, zcoord);
						 if (block == Blocks.farmland) {
							 world.setBlock(xcoord, ycoord, zcoord, block, 10, 0);
						 }
					 }
				 }
			 }
		}
	}

}
