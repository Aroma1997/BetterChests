package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.BagInventory;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
				ItemStack get = InvUtil.getFirstItem(inv, itemStack);
				if (get == null) {
					continue;
				}
				itemStack.stackSize += get.stackSize;
			}
		}
	}

}
