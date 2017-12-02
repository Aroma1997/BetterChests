package aroma1997.betterchests.upgrades.impl.plant;

import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.planter.IHarvestHandler;

public class NetherWartHandler implements IHarvestHandler {
	@Override
	public boolean canHandleHarvest(IBlockState state, World world, BlockPos pos) {
		return state.getPropertyKeys().contains(BlockNetherWart.AGE);
	}

	@Override
	public boolean handleHarvest(IBetterChest chest, IBlockState state, World world, BlockPos pos) {
		if (state.getValue(BlockNetherWart.AGE) == 3) {
			PlantHarvestHelper.breakBlockHandleDrop(world, pos, state, chest);
			return true;
		}
		return false;
	}

	@Override
	public int getHarvestPriority() {
		return 10;
	}
}
