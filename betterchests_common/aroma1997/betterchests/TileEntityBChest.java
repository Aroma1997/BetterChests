package aroma1997.betterchests;

import net.minecraft.tileentity.TileEntityChest;


public class TileEntityBChest extends TileEntityChest {
	
	private short stackLimit;
	private short slotLimit;

	public TileEntityBChest() {
		stackLimit = 1;
		slotLimit = 0;
	}

	@Override
	public String getInvName() {
		return "Adjustable Chest";
	}
	
	@Override
	public int getSizeInventory() {
		return slotLimit;
	}

	@Override
	public int getInventoryStackLimit() {
		return stackLimit;
	}
	
}
