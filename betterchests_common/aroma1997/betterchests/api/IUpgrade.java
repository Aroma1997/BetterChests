/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.api;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Implement this Interface in your Item and it'll be accepted by BetterChest.
 * You can also have a Multi-Item, where the Upgrades have a different metadata.
 * @author Aroma1997
 *
 */
public interface IUpgrade {
	
	/**
	 * If the Upgrade can be put on a Chest.
	 * @param item The Upgrade to Check.
	 * @return
	 */
	public boolean canChestTakeUpgrade(ItemStack item);
	
	/**
	 * If the Upgrade can be Put on a Bag.
	 * @param item The item to check
	 * @return
	 */
	public boolean canBagTakeUpgrade(ItemStack item);
	
	/**
	 * Get the list of Upgrades Required for this Upgrade.
	 * @param item The Upgrade to check.
	 * @return 
	 */
	public List<ItemStack> getRequiredUpgrade(ItemStack item);
	
	/**
	 * Called, whenerever the Chest gets ticked
	 * @param chest The Adjustable Chest or the Bag
	 * @param tick The tick (counts from 0 to 64)
	 * @param world The world
	 */
	public void update(IUpgradeProvider chest, int tick, World world);
	
	/**
	 * The Max amount of Upgrades per Chest/Bag.
	 * @param item The Upgrade to Check.
	 * @return
	 */
	public int getMaxUpgrades(ItemStack item);
	
	
	/**
	 * The name of The Upgrade.
	 * @param item The Upgrade to check
	 * @return The localized(!!) name of the Upgrade
	 */
	public String getName(ItemStack item);
	
}
