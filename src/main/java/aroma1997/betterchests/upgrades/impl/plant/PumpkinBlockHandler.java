package aroma1997.betterchests.upgrades.impl.plant;

import java.util.Collection;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

import aroma1997.core.util.WorldUtil;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.planter.IHarvestHandler;
import aroma1997.betterchests.api.planter.IPlantHandler;

public class PumpkinBlockHandler implements IPlantHandler, IHarvestHandler {

	@Override
	public boolean canHandlePlant(Collection<ItemStack> items, World world, BlockPos pos, IBlockState state) {
		if (!WorldUtil.isBlockAir(world, pos)) {
			return false;
		}
		MutableBlockPos mut = new MutableBlockPos();
		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			mut.setPos(pos).move(facing);
			IBlockState other = world.getBlockState(mut);
			if (other.getBlock() == Blocks.MELON_STEM || other.getBlock() == Blocks.PUMPKIN_STEM) {
				return  true;
			}
		}
		return false;
	}

	@Override
	public boolean handlePlant(IBetterChest chest, Collection<ItemStack> items, World world, BlockPos pos) {
		return false;
	}

	@Override
	public int getPlantPriority() {
		return 20;
	}

	@Override
	public boolean canHandleHarvest(IBlockState state, World world, BlockPos pos) {
		return state.getBlock() == Blocks.PUMPKIN_STEM || state.getBlock() == Blocks.MELON_STEM;
	}

	@Override
	public boolean handleHarvest(IBetterChest chest, IBlockState state, World world, BlockPos pos) {
		return false;
	}

	@Override
	public int getHarvestPriority() {
		return 20;
	}
}
