package aroma1997.betterchests.api;

import net.minecraft.item.ItemStack;
import aroma1997.core.inventories.IAdvancedInventory;
import aroma1997.core.inventories.ISpecialInventory;

public interface IInventoryFilter extends ISpecialInventory, IAdvancedInventory {
	
	/**
	 * If this InventoryFilter is dedicated to the upgrade
	 */
	boolean matchesUpgrade(ItemStack upgrade);
	
	/**
	 * If this is a Whitelist Filter
	 */
	boolean isWhitelist();
	
	/**
	 * If this is a Blacklist Filter
	 * @return
	 */
	boolean isBlacklist();

}
