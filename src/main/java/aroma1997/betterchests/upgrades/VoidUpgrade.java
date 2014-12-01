package aroma1997.betterchests.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import aroma1997.betterchests.InventoryFilter;
import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.IBetterChest;

public class VoidUpgrade extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		int i = (int) (chest.getLongTick() % chest.getSizeInventory());
		ItemStack stack = chest.getStackInSlot(i);
		if (stack != null) {
			if (UpgradeHelper.isItemAllowed(stack, chest.getFiltersForUpgrade(item))) {
				chest.setInventorySlotContents(i, null);
			}
		}

	}

}
