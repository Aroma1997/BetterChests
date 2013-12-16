package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.Upgrade;
import aroma1997.betterchests.api.IBetterChest;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.World;


public class Furnace extends BasicUpgrade {
	
	@Override
	public void updateChest(IBetterChest inv, int tick, World world,  ItemStack item) {
		if (inv.isUpgradeInstalled(Upgrade.FURNACE.getItem()) && tick %32 == 5) {
			int cooking = - 1;
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack == null) {
					continue;
				}
				if (FurnaceRecipes.smelting().getSmeltingResult(stack) == null) {
					continue;
				}
				cooking = i;
				break;
			}
			if (cooking != - 1) {
				ItemStack smelted = FurnaceRecipes.smelting().getSmeltingResult(
					inv.getStackInSlot(cooking)).copy();
				if (smelted.stackSize <= 0) {
					smelted.stackSize = 1;
				}
				int result = - 1;
				for (int i = 0; i < inv.getSizeInventory(); i++) {
					if (inv.getStackInSlot(i) == null
						|| smelted.isItemEqual(inv.getStackInSlot(i)) && smelted.stackSize
						+ inv.getStackInSlot(i).stackSize <= 64) {
						result = i;
						break;
					}
				}
				if (result != - 1) {
					inv.decrStackSize(cooking, 1);
					ItemStack put = inv.getStackInSlot(result);
					if (put != null) {
						put.stackSize += smelted.stackSize;
					}
					else {
						put = smelted;
					}
					inv.setInventorySlotContents(result, put);
				}
			}
		}
	}
	
}
