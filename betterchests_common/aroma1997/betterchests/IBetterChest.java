/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import aroma1997.betterchests.api.IUpgradeProvider;
import aroma1997.core.inventories.ISpecialInventory;


public interface IBetterChest extends ISpecialInventory, IUpgradeProvider {
	
    public double getXPos();

    public double getYPos();

    public double getZPos();
	
}
