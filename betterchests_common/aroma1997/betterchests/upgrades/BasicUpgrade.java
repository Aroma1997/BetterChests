package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.api.IBetterChest;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public abstract class BasicUpgrade {
	
	public abstract void updateChest(IBetterChest chest, int tick, World world, ItemStack item);
	
}
