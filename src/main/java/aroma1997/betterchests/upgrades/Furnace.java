package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.Upgrade;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.World;

public class Furnace extends BasicUpgrade {
	
	@Override
	public void updateChest(IBetterChest inv, int tick, World world, ItemStack item) {
		if (inv.isUpgradeInstalled(Upgrade.FURNACE.getItem()) && tick  == 5) {
			int cooking = - 1;
			for (int i = 0; i < inv.getSizeInventory(); i++ ) {
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
				        inv.getStackInSlot(cooking));
				if (smelted.stackSize <= 0) {
					smelted.stackSize = 1;
				}
				if (InvUtil.putIntoFirstSlot(inv, smelted, true) == null) {
					InvUtil.putIntoFirstSlot(inv, smelted, false);
					inv.decrStackSize(cooking, 1);
				}
			}
		}
	}
	
}
