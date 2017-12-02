package aroma1997.betterchests.api;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This provides basic functionality for dealing with upgrades.
 * @author Aroma1997
 */
public class UpgradeHelper {

	/**
	 * The actual IUpgradeHelper instance
	 */
	public static IUpgradeHelper INSTANCE;

	public static interface IUpgradeHelper {
		/**
		 * Returns whether the given ItemStack is the first upgrade of that type in the given Upgradable Block.
		 * This is useful, when you want to for example increase the radius of what your upgrade does depending on
		 * the amount of upgrades, but you don't want every upgrade to do this action.
		 * @param block The Block to check
		 * @param stack The Upgrade to check
		 * @return whether the given ItemStack is the first Upgrade of that type in the give chest.
		 */
		boolean isFirstUpgrade(IUpgradableBlock block, ItemStack stack);

		/**
		 * Returns a integer in the interval from including 0 to excluding frequency. This is distributed somewhat
		 * evenly, so a call with the same frequency on the same chest with different upgrades most likely returns a
		 * different number.
		 * This is useful, when you want to have a upgrade only do something every so often, you call this number with
		 * how many ticks should pass between each operation and you check whether the return value is 0.
		 * Doing this with different upgrades with the same frequency will most likely return a different number, so it
		 * is safe to check for the same number (0) on all upgrades.
		 * @param block The Upgradable Block the upgrade is in
		 * @param stack The actual upgrade
		 * @param frequency The interval in which the result should be.
		 * @return A number continuously increasing modulo frequency.
		 */
		int getFrequencyTick(IUpgradableBlock block, ItemStack stack, int frequency);

		/**
		 * Returns a Upgradable Block, if the given ItemStack represents one.
		 * @param stack The Stack to get the UpgradableBlock for.
		 * @param containerEntity The entity, that's currently holding the ItemStack. May be null, if unknown.
		 * @return A UpgradableBlock if the ItemStack represents a UpgradableBlock or null, if it doesn't.
		 */
		@Nullable IUpgradableBlock getInventory(ItemStack stack, @Nullable Entity containerEntity);

		/**
		 * Returns a Upgradable Block, if there is a Upgradable Block at the given position in the given world.
		 * @param world The world to check
		 * @param pos The position to check
		 * @return A Upgradable Block, if there is one at the specified position or null if not.
		 */
		@Nullable IUpgradableBlock getInventory(World world, BlockPos pos);
	}
}
