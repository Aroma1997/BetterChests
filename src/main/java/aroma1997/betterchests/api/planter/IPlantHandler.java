package aroma1997.betterchests.api.planter;

import java.util.Collection;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import aroma1997.betterchests.api.IBetterChest;

/**
 * This can be used to add support for additional crops in the Planting Upgrade.
 * @author Aroma1997
 */
public interface IPlantHandler {

	/**
	 * This method should return whether this PlantHandler <b>can</b> plant the given stack at the given position.
	 * Returning true will will also prevent other PlantHandlers from getting the chance to plant. This can be useful,
	 * if you want to override the behaviour of other PlantHandlers. <br/> For performance reasons, you should first
	 * check whether the position you're at is viable for planting and oinly then check if you have seeds available.
	 * @param items All items currently available for planting.
	 * @param world The world to plant at.
	 * @param pos The position to plant at.
	 * @param state The BlockState at the given position.
	 * @return Whether this PlantHandler can Plant any of the given items at the given position.
	 */
	boolean canHandlePlant(Collection<ItemStack> items, World world, BlockPos pos, IBlockState state);
	/**
	 * In this method you should actually perform the planting work if all requirements are met. You don't have to
	 * check for requirements tested in {@link #canHandlePlant(Collection, World, BlockPos, IBlockState)}  since this method
	 * will only be called, if canHandlePlant returned true.<br/>
	 * You should then return whether you actually performed a planting step.
	 * @param chest The BetterChest containing the planting upgrade.
	 * @param items The items currently available.
	 * @param world The world to plant at.
	 * @param pos The position zo plant at.
	 * @return Whether you actually performed a planting step.
	 */
	boolean handlePlant(IBetterChest chest, Collection<ItemStack> items, World world, BlockPos pos);

	/**
	 * Returns the priority of this PlantHandler. If you want to override another PlantHandler, you need to return a
	 * higher priority here as PlantHandler will be queried in decreasing priority.
	 * @return The priority of this PlantHandler.
	 */
	default int getPlantPriority() {
		return 0;
	}

}
