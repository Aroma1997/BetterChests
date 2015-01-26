/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

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
	 * 
	 * @return
	 */
	boolean isBlacklist();

}
