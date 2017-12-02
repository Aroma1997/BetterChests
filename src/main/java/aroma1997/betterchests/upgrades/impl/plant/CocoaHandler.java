package aroma1997.betterchests.upgrades.impl.plant;

import java.util.Collection;

import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import aroma1997.core.util.WorldUtil;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.planter.IHarvestHandler;
import aroma1997.betterchests.api.planter.IPlantHandler;

public class CocoaHandler implements IHarvestHandler, IPlantHandler {

	@Override
	public boolean canHandlePlant(Collection<ItemStack> items, World world, BlockPos pos, IBlockState state) {
		if (!WorldUtil.isBlockAir(state)) {
			return false;
		}
		boolean found = false;
		for (EnumFacing dir : EnumFacing.HORIZONTALS) {
			BlockPos toCheck = pos.offset(dir);
			IBlockState other = world.getBlockState(toCheck);
			if (other.getBlock() == Blocks.LOG && other.getValue(BlockOldLog.VARIANT) == EnumType.JUNGLE) {
				found = true;
				break;
			}
		}
		return found && items.stream().anyMatch(this::canPlantStack);
	}

	@Override
	public boolean handlePlant(IBetterChest chest, Collection<ItemStack> items, World world, BlockPos pos) {

		EnumFacing jungleBlock = EnumFacing.UP;
		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			if (isJungleTree(world.getBlockState(pos.offset(side)))) {
				jungleBlock = side;
				break;
			}
		}
		world.setBlockState(pos, Blocks.COCOA.getDefaultState().withProperty(BlockCocoa.FACING, jungleBlock));
		ItemStack stack = items.stream().filter(this::canPlantStack).findFirst().get();
		stack.setCount(stack.getCount() - 1);
		return true;
	}

	private boolean canPlantStack(ItemStack stack) {
		return stack.getItem() == Items.DYE && stack.getMetadata() == EnumDyeColor.BROWN.getDyeDamage();
	}

	@Override
	public boolean canHandleHarvest(IBlockState state, World world, BlockPos pos) {
		if (!isJungleTree(state)) {
			return false;
		}

		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			BlockPos toCheck = pos.offset(side);
			if (world.getBlockState(toCheck).getBlock() == Blocks.COCOA) {
				return true;
			}
		}
		return false;

	}

	@Override
	public boolean handleHarvest(IBetterChest chest, IBlockState state, World world, BlockPos pos) {
		return false;
	}

	@Override
	public int getHarvestPriority() {
		return 10;
	}

	private boolean isJungleTree(IBlockState state) {
		return state.getBlock() == Blocks.LOG && state.getValue(BlockOldLog.VARIANT) == EnumType.JUNGLE;
	}
}
