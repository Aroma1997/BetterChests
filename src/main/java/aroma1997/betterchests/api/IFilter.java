package aroma1997.betterchests.api;

import net.minecraft.item.ItemStack;

/**
 * A interface for filters. Filters can be received via {@link IBetterChest#getFilterFor(ItemStack)}
 * @author Aroma1997
 */
public interface IFilter {

	/**
	 * Returns whether the given ItemStack matches this filter.
	 * @param stack The Item to check.
	 * @return whether the given Item matches this filter.
	 */
	boolean matchesStack(ItemStack stack);

	/**
	 * Returns whether there are any filters restricting accepted Items at all.
	 * @return whether this filter has any actual filters.
	 */
	boolean hasStackFilter();

}
