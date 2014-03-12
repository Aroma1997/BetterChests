/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.api;

import java.util.ArrayList;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * This is implemented in the Bag and in the Chest.
 * 
 * @author Aroma1997
 * 
 */
public interface IBetterChest extends IInventory {

	/**
	 * The position
	 * 
	 * @return x-coord
	 */
	public double getXPos();

	/**
	 * The position
	 * 
	 * @return y-coord
	 */
	public double getYPos();

	/**
	 * The position
	 * 
	 * @return z-coord
	 */
	public double getZPos();

	/**
	 * How many of the Upgrades are installed. This is ignoring NBT data of the
	 * stack.
	 * 
	 * @param upgrade
	 *            The upgrade to check.
	 * @return
	 */
	public int getAmountUpgrade(ItemStack upgrade);

	/**
	 * How many of the Upgrades are installed.
	 * 
	 * @param upgrade
	 *            The upgrade to check.
	 * @return
	 */
	public int getAmountUpgradeExact(ItemStack upgrade);

	/**
	 * If the Amount of Upgrades installed is greater than 0
	 * 
	 * @param upgrade
	 *            The Upgrade to check
	 * @return
	 */
	public boolean isUpgradeInstalled(ItemStack upgrade);

	/**
	 * Set the Amount of an Upgrade
	 * 
	 * @param upgrade
	 * @param amount
	 */
	public void setAmountUpgrade(ItemStack upgrade, int amount);

	/**
	 * If the Chest has Energy. For Energy-Relying Upgrades.
	 * 
	 * @return
	 */
	public boolean hasEnergy();

	/**
	 * Getter for the Upgrade list
	 * 
	 * @return The list of Upgrades
	 */
	public ArrayList<ItemStack> getUpgrades();
	
	/**
	 * Getter for the long tick.
	 * This will increase by 1 every tick. (Used to have a greater tick than 64)
	 * @return The long tick
	 */
	public long getLongTick();

}
