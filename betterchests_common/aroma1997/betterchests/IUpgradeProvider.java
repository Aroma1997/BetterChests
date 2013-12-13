/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import net.minecraft.item.ItemStack;


public interface IUpgradeProvider {
	
	public int getAmountUpgrade(ItemStack upgrade);
	
	public boolean isUpgradeInstalled(ItemStack upgrade);
	
	public void setAmountUpgrade(ItemStack upgrade, int amount);
	
	public boolean hasEnergy();
}
