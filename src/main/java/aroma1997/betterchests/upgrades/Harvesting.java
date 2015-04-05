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

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockMelon;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockReed;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import aroma1997.betterchests.Reference;
import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

public class Harvesting extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		if (chest.getEnergyObject().getCurrent() < Reference.Conf.ENERGY_HARVESTING)
			return;
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
		doBlock(chest, tick, world, item, new BlockPos(xcoord, ycoord, zcoord));
	}

	private static void doBlock(IBetterChest chest, int tick, World world,
			ItemStack item, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block != null && block instanceof IGrowable) {
			IGrowable gr = (IGrowable) block;
			if (!gr.canGrow(world, pos, state, world.isRemote)) {
				List<ItemStack> items = block.getDrops(world, pos, state, 0);
				boolean b = false;
				for (ItemStack itemGet : items) {
					if (InvUtil.putIntoFirstSlot(chest, itemGet, true) != null
							&& UpgradeHelper.isItemAllowed(itemGet,
									chest.getFiltersForUpgrade(item))) {
						b = true;
						break;
					}
				}
				if (!b) {
					for (ItemStack itemGet : items) {
						InvUtil.putIntoFirstSlot(chest, itemGet, false);
					}
					world.setBlockToAir(pos);
					chest.getEnergyObject().setCurrent(
							chest.getEnergyObject().getCurrent()
									- Reference.Conf.ENERGY_HARVESTING);
				}
			}
		} else if (block != null
				&& (block instanceof BlockMelon || block instanceof BlockPumpkin)) {
			ItemStack itemS = new ItemStack(block.getItemDropped(state,
					world.rand, 0),
					block.quantityDropped(state, 0, world.rand),
					block.damageDropped(state));
			if (InvUtil.putIntoFirstSlot(chest, itemS, true) == null
					&& UpgradeHelper.isItemAllowed(itemS,
							chest.getFiltersForUpgrade(item))) {
				InvUtil.putIntoFirstSlot(chest, itemS, false);
				world.setBlockToAir(pos);
				chest.getEnergyObject().setCurrent(
						chest.getEnergyObject().getCurrent()
								- Reference.Conf.ENERGY_HARVESTING);
			}
		} else if (block != null
				&& (block instanceof BlockCactus || block instanceof BlockReed)) {
			int i = 0;
			while (world.getBlockState(pos.offset(EnumFacing.UP, i + 1))
					.getBlock() == block) {
				i++;
			}
			if (i > 2) {
				state = world.getBlockState(pos.offset(EnumFacing.UP, i));
				ItemStack itemS = new ItemStack(block.getItemDropped(state,
						world.rand, 0), block.quantityDropped(state, 0,
						world.rand), block.damageDropped(state));
				if (InvUtil.putIntoFirstSlot(chest, itemS, true) == null
						&& UpgradeHelper.isItemAllowed(itemS,
								chest.getFiltersForUpgrade(item))) {
					InvUtil.putIntoFirstSlot(chest, itemS, false);
					world.setBlockToAir(pos.offset(EnumFacing.UP, i));
					chest.getEnergyObject().setCurrent(
							chest.getEnergyObject().getCurrent()
									- Reference.Conf.ENERGY_HARVESTING);
				}
			}
		} else if (block != null
				&& state.getProperties().containsKey(BlockNetherWart.AGE)) {
			if ((int) state.getValue(BlockNetherWart.AGE) >= 3) {
				List<ItemStack> drops = block.getDrops(world, pos, state, 0);
				boolean isOK = true;
				for (ItemStack drop : drops) {
					if (InvUtil.putIntoFirstSlot(chest, drop, true) != null) {
						isOK = false;
						break;
					}
				}
				if (isOK) {
					world.setBlockToAir(pos);
					for (ItemStack drop : drops) {
						InvUtil.putIntoFirstSlot(chest, drop, false);
					}
				}
			}
		}
	}

}
