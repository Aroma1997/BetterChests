package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.log.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Ticking extends BasicUpgrade {
	
	@Override
	public void updateChest(IBetterChest chest, int tick, World world, ItemStack item) {
		try {
			for (int i = 0; i < chest.getSizeInventory(); i++) {
				ItemStack t = chest.getStackInSlot(i);
				t.updateAnimation(world, null, i, true);
			}
		}
		catch (NullPointerException e) {
			LogHelper.logException("Failed to use ticking upgrade on chest: " + chest.toString() + " Coords: x=" + chest.getXCoord() + " y=" + chest.getYCoord() + " z=" + chest.getZCoord(), e);
		}
		
	}
	
}
