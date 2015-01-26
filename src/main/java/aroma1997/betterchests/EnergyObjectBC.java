/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.misc.EnergyObject;

public class EnergyObjectBC extends EnergyObject {

	private final IBetterChest chest;

	public EnergyObjectBC(IBetterChest chest) {
		this.chest = chest;
	}

	@Override
	public EnergyObject setCurrent(int c) {
		super.setCurrent(c);
		if (chest instanceof TileEntityBChest) {
			((TileEntityBChest) chest).getWorld().addBlockEvent(
					((TileEntityBChest) chest).getPos(),
					((TileEntityBChest) chest).getBlockType(), 2, c);
		}
		return this;
	}

}
