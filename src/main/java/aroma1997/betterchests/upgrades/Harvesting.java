package aroma1997.betterchests.upgrades;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockMelon;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockReed;
import net.minecraft.block.IGrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import aroma1997.betterchests.Reference;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

public class Harvesting extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		int range = Reference.Conf.PLANTS_RANGE_MULTIPLIER * item.stackSize;
		int doubleRange = range * 2 + 1;

		int num = (int) (chest.getLongTick() % (doubleRange * doubleRange * 2));
		if (num >= doubleRange * doubleRange)
			return;
		int xcoord = chest.getXCoord() + num / doubleRange - range;
		int zcoord = chest.getZCoord() + num % doubleRange - range;
		int ycoord = chest.getYCoord();
		if (!InvUtil.hasSpace(chest))
			return;
		doBlock(chest, tick, world, item, xcoord, ycoord, zcoord);

	}

	private static void doBlock(IBetterChest chest, int tick, World world,
			ItemStack item, int xcoord, int ycoord, int zcoord) {
		Block block = world.getBlock(xcoord, ycoord, zcoord);
		int meta = world.getBlockMetadata(xcoord, ycoord, zcoord);
		if (block != null && block instanceof IGrowable) {
			IGrowable gr = (IGrowable) block;
			if (!gr.func_149851_a(world, xcoord, ycoord, zcoord, false)) {
				ArrayList<ItemStack> items = block.getDrops(world, xcoord,
						ycoord, zcoord,
						world.getBlockMetadata(xcoord, ycoord, zcoord), 0);
				boolean b = false;
				for (ItemStack itemGet : items) {
					if (InvUtil.putIntoFirstSlot(chest, itemGet, true) != null) {
						b = true;
						break;
					}
				}
				if (!b) {
					for (ItemStack itemGet : items) {
						InvUtil.putIntoFirstSlot(chest, itemGet, false);
					}
					world.setBlockToAir(xcoord, ycoord, zcoord);
				}
			}
		} else if (block != null
				&& (block instanceof BlockMelon || block instanceof BlockPumpkin)) {
			ItemStack itemS = new ItemStack(block.getItemDropped(meta,
					world.rand, 0), block.quantityDropped(meta, 0, world.rand),
					block.damageDropped(meta));
			if (InvUtil.putIntoFirstSlot(chest, itemS, true) == null) {
				InvUtil.putIntoFirstSlot(chest, itemS, false);
				world.setBlockToAir(xcoord, ycoord, zcoord);
			}
		} else if (block != null
				&& (block instanceof BlockCactus || block instanceof BlockReed)) {
			int i = 0;
			while (world.getBlock(xcoord, ycoord + i, zcoord) == block) {
				i++;
			}
			if (i > 2) {
				meta = world.getBlockMetadata(xcoord, ycoord + i - 1, zcoord);
				ItemStack itemS = new ItemStack(block.getItemDropped(meta,
						world.rand, 0), block.quantityDropped(meta, 0,
						world.rand), block.damageDropped(meta));
				if (InvUtil.putIntoFirstSlot(chest, itemS, true) == null) {
					InvUtil.putIntoFirstSlot(chest, itemS, false);
					world.setBlockToAir(xcoord, ycoord + i - 1, zcoord);
				}
			}
		}
	}

}
