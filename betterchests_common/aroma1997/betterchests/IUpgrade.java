package aroma1997.betterchests;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public interface IUpgrade {
	
	public boolean canChestTakeUpgrade(ItemStack item);
	
	public boolean canBagTakeUpgrade(ItemStack item);
	
	public List<ItemStack> getRequiredUpgrade(ItemStack item);
	
	/**
	 * Called, whenerever the Chest gets ticked
	 * @param chest The Adjustable Chest or the Bag
	 * @param tick The tick (counts from 0 to 64)
	 * @param world The world
	 */
	public void update(IBetterChest chest, int tick, World world);
	
	public int getMaxUpgrades(ItemStack item);
	
	public String getName(ItemStack item);
	
}
