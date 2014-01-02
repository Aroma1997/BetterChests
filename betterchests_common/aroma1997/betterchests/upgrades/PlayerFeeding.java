package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.BagInventory;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class PlayerFeeding extends BasicUpgrade {
	
	@Override
	public void updateChest(IBetterChest chest, int tick, World world, ItemStack item) {
		if (chest != null && chest instanceof BagInventory) {
			BagInventory inv = (BagInventory) chest;
			EntityPlayer player = inv.getPlayer();
//			player.getFoodStats().setFoodLevel(5);
			if (!player.getFoodStats().needFood()) return;
			int slot = InvUtil.getFirstItem(inv, ItemFood.class);
			if (slot == -1) return;
			ItemStack itemStack = inv.getStackInSlot(slot);
			if (itemStack == null) return;
			ItemFood food = (ItemFood)itemStack.getItem();
			if (20 - player.getFoodStats().getFoodLevel() >= food.getHealAmount() || player.getFoodStats().getFoodLevel() <= 16) {
				food.onEaten(itemStack.copy(), world, player);
				inv.decrStackSize(slot, 1);
			}
		}
	}
	
}
