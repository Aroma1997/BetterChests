package aroma1997.betterchests.upgrades;

import java.io.FilterInputStream;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.World;
import aroma1997.betterchests.InventoryFilter;
import aroma1997.betterchests.Upgrade;
import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

public class Furnace extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest inv, int tick, World world,
			ItemStack item) {
		if (inv.isUpgradeInstalled(Upgrade.FURNACE.getItem()) && tick == 5) {
			int cooking = -1;
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack == null) {
					continue;
				}
				if (FurnaceRecipes.instance().getSmeltingResult(stack) == null || !UpgradeHelper.isItemAllowed(stack, inv.getFiltersForUpgrade(item))) {
					continue;
				}
				cooking = i;
				break;
			}
			if (cooking != -1) {
				ItemStack smelted = FurnaceRecipes.instance()
						.getSmeltingResult(inv.getStackInSlot(cooking));
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
