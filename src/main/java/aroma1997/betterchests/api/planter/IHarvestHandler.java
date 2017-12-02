package aroma1997.betterchests.api.planter;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import aroma1997.betterchests.api.IBetterChest;

/**
 * This can be used to add support for additional crops in the Harvesting Upgrade.
 * @author Aroma1997
 */
public interface IHarvestHandler {

	/**
	 * This method should return whether this HarvestHandler <b>can</b> harvest the given block. Returning true will
	 * will also prevent other HarvestHandlers from getting the chance to harvest. This can be useful, if you want to
	 * override the behaviour of other HarvestHandlers.
	 * @param state The BlockState at the given position.
	 * @param world The world the block to harvest is in.
	 * @param pos The position of the block to harvest.
	 * @return Whether this HarvestHandler can harvest the given block.
	 */
	boolean canHandleHarvest(IBlockState state, World world, BlockPos pos);

	/**
	 * In this method you should actually perform the harvesting work if all requirements are met. You don't have to
	 * check for requirements tested in {@link #canHandleHarvest(IBlockState, World, BlockPos)} since this method will
	 * only be called, if canHandleHarvest returned true.<br/>
	 * You should then return whether you actually performed a harvesting step.
	 * @param chest The BetterChest containing the harvesting upgrade. This is where you can put the harvested items.
	 * @param state The BlockState of the block at the given position.
	 * @param world The world the block to harvest is in.
	 * @param pos The position of the block to harvest.
	 * @return Whether you actually performed a harvest step.
	 */
	boolean handleHarvest(IBetterChest chest, IBlockState state, World world, BlockPos pos);

	/**
	 * Returns the priority of this HarvestHandler. If you want to override another HarvestHandler, you need to return a
	 * higher priority here as HarvestHandlers will be queried in decreasing priority.
	 * @return The priority of this HarvestHandler.
	 */
	default int getHarvestPriority() {
		return 0;
	}
}
