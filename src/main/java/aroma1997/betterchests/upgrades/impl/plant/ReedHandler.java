package aroma1997.betterchests.upgrades.impl.plant;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.planter.IHarvestHandler;

public class ReedHandler implements IHarvestHandler {
	@Override
	public boolean canHandleHarvest(IBlockState state, World world, BlockPos pos) {
		return state.getBlock() == Blocks.REEDS || state.getBlock() == Blocks.CACTUS;
	}

	@Override
	public boolean handleHarvest(IBetterChest chest, IBlockState state, World world, BlockPos pos) {
		MutableBlockPos mut = new MutableBlockPos(pos);
		while (world.getBlockState(mut).getBlock() == state.getBlock()) {
			mut.move(EnumFacing.UP);
		}
		mut.move(EnumFacing.DOWN);
		if (mut.getY() != pos.getY()) {
			PlantHarvestHelper.breakBlockHandleDrop(world, mut, state, chest);
			return true;
		}
		return false;
	}
}
