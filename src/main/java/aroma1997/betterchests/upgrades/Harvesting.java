package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.Reference;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;

public class Harvesting extends BasicUpgrade {
	
	@Override
	public void updateChest(IBetterChest chest, int tick, World world, ItemStack item) {
		int range = Reference.Conf.PLANTS_RANGE_MULTIPLIER * item.stackSize;
		int doubleRange = range * 2 + 1;
		
		int num = (int) (chest.getLongTick() % (doubleRange * doubleRange));
		int xcoord = (int) chest.getXPos() + num / doubleRange - range;
		int zcoord = (int) chest.getZPos() + num % doubleRange - range;
		int ycoord = (int) chest.getYPos();
		int slot = InvUtil.getFirstItem(chest, IPlantable.class);
		for (int i = Reference.Conf.PLANTS_START + 1; i <= Reference.Conf.PLANTS_START
		        + Reference.Conf.PLANTS_HEIGHT + 1; i++ ) {
			doBlock(chest, tick, world, item, xcoord, ycoord + i, zcoord, slot);
		}
		
	}
	
	private static void doBlock(IBetterChest chest, int tick, World world, ItemStack item,
	        int xcoord, int ycoord, int zcoord, int slot) {
		Block block = world.getBlock(xcoord, ycoord, zcoord);
		if (block != null && block instanceof IGrowable) {
			IGrowable gr = (IGrowable) block;
			if (! gr.func_149851_a(world, xcoord, ycoord, zcoord, false)) {
				ArrayList<ItemStack> items = block.getDrops(world, xcoord, ycoord, zcoord,
				        world.getBlockMetadata(xcoord, ycoord, zcoord), 0);
				boolean b = false;
				for (ItemStack itemGet : items) {
					if (InvUtil.putIntoFirstSlot(chest, itemGet, true) != null) {
						b = true;
						break;
					}
				}
				if (! b) {
					for (ItemStack itemGet : items) {
						InvUtil.putIntoFirstSlot(chest, itemGet, false);
					}
					world.setBlockToAir(xcoord, ycoord, zcoord);
				}
			}
		}
	}
	
}
