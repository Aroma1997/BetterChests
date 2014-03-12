package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.Reference;
import aroma1997.betterchests.Upgrade;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.common.IPlantable;

public class Planting extends BasicUpgrade {
	
	@Override
	public void updateChest(IBetterChest chest, int tick, World world, ItemStack item) {
		int range = Reference.Conf.PLANTS_RANGE_MULTIPLIER * item.stackSize;
		int doubleRange = range * 2 + 1;
		
		int num = (int) (chest.getLongTick() % (doubleRange * doubleRange));
		int xcoord = (int) chest.getXPos() + num / doubleRange - range;
		int zcoord = (int) chest.getZPos() + num % doubleRange - range;
		int ycoord = (int) chest.getYPos();
		int slot = InvUtil.getFirstItem(chest, IPlantable.class);
		for (int i = Reference.Conf.PLANTS_START; i <= Reference.Conf.PLANTS_START
		        + Reference.Conf.PLANTS_HEIGHT; i++ ) {
			doBlock(chest, tick, world, item, xcoord, ycoord + i, zcoord, slot);
		}
	}
	
	private static void doBlock(IBetterChest chest, int tick, World world, ItemStack item,
	        int xcoord, int ycoord, int zcoord, int slot) {
		if (slot == - 1) {
			return;
		}
		Block block = world.getBlock(xcoord, ycoord, zcoord);
		if (block == null) {
			return;
		}
		if (block == Blocks.dirt || block == Blocks.grass) {
			Block blockAbove = world.getBlock(xcoord, ycoord + 1, zcoord);
			if (blockAbove == null || blockAbove == Blocks.air
			        || blockAbove instanceof BlockTallGrass || blockAbove instanceof BlockFlower) {
				world.setBlock(xcoord, ycoord, zcoord, Blocks.farmland, 10, 3);
				world.setBlockToAir(xcoord, ycoord + 1, zcoord);
			}
		}
		block = world.getBlock(xcoord, ycoord, zcoord);
		if (block instanceof BlockFarmland) {
			if (world.getBlockMetadata(xcoord, ycoord, zcoord) <= 2
			        && chest.isUpgradeInstalled(Upgrade.RAIN.getItem())
			        && InvUtil.getFirstItem(chest, new ItemStack(Items.water_bucket), true) != null) {
				world.setBlock(xcoord, ycoord, zcoord, block, 5, 2);
			}
			Block blockAboveNew = world.getBlock(xcoord, ycoord + 1, zcoord);
			if (blockAboveNew == null || blockAboveNew == Blocks.air
			        || blockAboveNew instanceof BlockTallGrass
			        || blockAboveNew instanceof BlockFlower) {
				ItemStack itemPlant = chest.getStackInSlot(slot);
				if (itemPlant == null || itemPlant.stackSize <= 0) {
					return;
				}
				IPlantable pl = (IPlantable) itemPlant.getItem();
				if (Block.getBlockFromItem((Item) pl).canPlaceBlockAt(world, xcoord, ycoord + 1,
				        zcoord)) {
					world.setBlock(xcoord, ycoord + 1, zcoord,
					        pl.getPlant(world, xcoord, ycoord + 1, zcoord),
					        pl.getPlantMetadata(world, xcoord, ycoord, zcoord), 3);
					chest.decrStackSize(slot, 1);
				}
			}
		}
	}
	
}
