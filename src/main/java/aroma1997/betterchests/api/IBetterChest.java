package aroma1997.betterchests.api;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

/**
 * This interface is implemented on all Upgradable Blocks, that represent an Inventory like a BetterChest or BetterBag.
 * To access the inventory internally via upgrades, you have to make sure to only access slots returned by
 * {@link #getSlotsForFace(EnumFacing)} with the side null. Internally, you can ignore
 * {@link #canExtractItem(int, ItemStack, EnumFacing)} and {@link #canInsertItem(int, ItemStack, EnumFacing)}
 */
public interface IBetterChest extends IUpgradableBlock, ISidedInventory {

	/**
	 * Returns a Filter object containing all filters for the given upgrade.
	 * @param stack The upgrade to get the filter for.
	 * @return A Filter for this upgrade.
	 */
	IFilter getFilterFor(ItemStack stack);
}
