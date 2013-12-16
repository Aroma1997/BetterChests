package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.Upgrade;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class CobbleGen extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest inv, int tick, World world, ItemStack item) {
		if (inv.isUpgradeInstalled(Upgrade.COBBLEGEN.getItem()) && tick % 8 == 5) {
			int bucketLava = - 1;
			int bucketWater = - 1;
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i) != null
					&& inv.getStackInSlot(i).itemID == Item.bucketWater.itemID
					&& bucketWater == - 1) {
					bucketWater = i;
					continue;
				}
				if (inv.getStackInSlot(i) != null && bucketLava == - 1
					&& inv.getStackInSlot(i).itemID == Item.bucketLava.itemID) {
					bucketLava = i;
					continue;
				}
			}
			if (bucketLava == - 1 || bucketWater == - 1) {
				return;
			}
			InvUtil.putIntoFirstSlot(inv, new ItemStack(Block.cobblestone));
		}
	}
	
}
