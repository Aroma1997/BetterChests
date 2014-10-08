package aroma1997.betterchests.upgrades;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import aroma1997.betterchests.BagInventory;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

public class Resupply extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		if (tick % 8 != 2) {
			return;
		}
		if (chest != null && chest instanceof BagInventory) {
			BagInventory inv = (BagInventory) chest;
			EntityPlayer player = inv.getPlayer();
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				ItemStack itemStack = player.inventory.getStackInSlot(i);
				if (itemStack == null) {
					continue;
				}
				ItemStack get = InvUtil.getFirstItem(inv, itemStack, true);
				if (get == null) {
					continue;
				}

				if (itemStack.stackSize + get.stackSize > itemStack
						.getMaxStackSize()) {
					int over = itemStack.getMaxStackSize()
							- itemStack.stackSize;
					if (over > 0) {
						get.stackSize -= over;
						itemStack.stackSize += over;
					}
				} else {
					itemStack.stackSize += get.stackSize;
					InvUtil.getFirstItem(inv, itemStack, false);
				}
			}
		}
	}

}
