package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

public class Harvesting extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		int range = 2 * item.stackSize;
		if (tick == 38) {
			for (int x = -range; x <= range; x++) {
				for (int z = -range; z <= range; z++) {
					for (int y = -2; y < 4; y++) {
						int xcoord = (int) chest.getXPos() + x;
						int ycoord = (int) chest.getYPos() + y;
						int zcoord = (int) chest.getZPos() + z;
						Block block = world.getBlock(xcoord, ycoord, zcoord);
						if (block != null && block instanceof IGrowable) {
							IGrowable gr = (IGrowable) block;
							if (!gr.func_149851_a(world, xcoord, ycoord,
									zcoord, false)) {
								ArrayList<ItemStack> items = block.getDrops(
										world, xcoord, ycoord, zcoord, world
												.getBlockMetadata(xcoord,
														ycoord, zcoord), 0);
								boolean b = false;
								for (ItemStack itemGet : items) {
									if (InvUtil.putIntoFirstSlot(chest,
											itemGet, true) != null) {
										b = true;
										break;
									}
								}
								if (!b) {
									for (ItemStack itemGet : items) {
										InvUtil.putIntoFirstSlot(chest,
												itemGet, false);
									}
									world.setBlockToAir(xcoord, ycoord, zcoord);
								}
							}
						}
					}
				}
			}
		}

	}

}
