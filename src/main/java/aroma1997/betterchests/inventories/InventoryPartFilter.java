package aroma1997.betterchests.inventories;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import aroma1997.core.inventory.IInventoryPartContainer;
import aroma1997.core.inventory.inventorypart.InventoryPartBase;
import aroma1997.betterchests.BlocksItemsBetterChests;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.filter.InventoryFilter;
import aroma1997.betterchests.filter.ItemFilter;

public class InventoryPartFilter extends InventoryPartBase {
	public InventoryPartFilter(IInventoryPartContainer container) {
		super(container, "betterchests:inventorypart.filter", 8, false, false);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return stack.getItem() == BlocksItemsBetterChests.filter;
	}

	public IFilter getFilterForUpgrade(ItemStack upgrade) {
		List<InventoryFilter> applyingFilters = new ArrayList<>();
		for (ItemStack filterStack : this) {
			InventoryFilter filter = ItemFilter.getInventoryFor(filterStack);
			if (filter.appliesToUpgrade(upgrade)) {
				applyingFilters.add(filter);
			}
		}

		return new IFilter() {
			@Override
			public boolean matchesStack(ItemStack stack) {
				if (stack.isEmpty()) {
					return !applyingFilters.stream().filter(InventoryFilter::isEmpty).map(InventoryFilter::isBlacklist)
							.findFirst().orElse(true);
				}
				boolean hasWhitelist = false;
				boolean isOnWhitelist = false;
				for (InventoryFilter filter : applyingFilters) {
					if (!filter.isBlacklist()) {
						hasWhitelist = true;
						if (filter.isAffected(stack)) {
							isOnWhitelist = true;
						}
					} else {
						if (filter.isAffected(stack)) {
							return false;
						}
					}
				}

				if (hasWhitelist) {
					return isOnWhitelist;
				}
				return true;
			}

			@Override
			public boolean hasStackFilter() {
				return !applyingFilters.isEmpty();
			}
		};
	}
}
