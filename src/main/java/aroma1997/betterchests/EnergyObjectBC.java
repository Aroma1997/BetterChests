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
