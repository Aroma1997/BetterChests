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


public interface IUpgradeProvider extends IInventory {
	
	/**
	 * How many of the Upgrades are installed.
	 * @param upgrade The upgrade to check.
	 * @return
	 */
	public int getAmountUpgrade(ItemStack upgrade);
	
	/**
	 * If the Amount of Upgrades installed is greater than 0
	 * @param upgrade The Upgrade to check
	 * @return
	 */
	public boolean isUpgradeInstalled(ItemStack upgrade);
	
	/**
	 * Set the Amount of an Upgrade
	 * @param upgrade
	 * @param amount
	 */
	public void setAmountUpgrade(ItemStack upgrade, int amount);
	
	/** 
	 * If the Chest has Energy. For Energy-Relying Upgrades.
	 * @return
	 */
	public boolean hasEnergy();
}
