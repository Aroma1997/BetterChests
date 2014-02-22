
package aroma1997.betterchests;


import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import aroma1997.core.util.InvUtil;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventListener {
	
	public EventListener() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void pickup(EntityItemPickupEvent event) {
		EntityPlayer player = event.entityPlayer;
		EntityItem eitem = event.item;
		ItemStack item = eitem.getEntityItem();
		if (item != null) {
			int c = - 1;
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack != null && stack.getItem() instanceof ItemBag) {
					BagInventory inv = ItemBag.getInventory(stack);
					if (inv.isUpgradeInstalled(Upgrade.COLLECTOR.getItem())) {
						c = i;
						break;
					}
				}
			}
			if (c != - 1) {
				BagInventory inv = ItemBag.getInventory(player.inventory.getStackInSlot(c));
				ItemStack ret = InvUtil.putIntoFirstSlot(inv, item, false);
				if (ret == null) {
					event.setCanceled(true);
					eitem.setDead();
				}
				else if (! ItemStack.areItemStacksEqual(item, ret)) {
					eitem.setEntityItemStack(ret);
				}
			}
		}
	}
	
}
