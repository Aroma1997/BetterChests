/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import aroma1997.core.util.InvUtil;

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
			int c = -1;
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack != null && stack.getItem() instanceof ItemBag) {
					BagInventory inv = ItemBag.getBagInventory(stack);
					if (inv.isUpgradeInstalled(Upgrade.COLLECTOR.getItem())
							&& inv.isUpgradeDisabled(Upgrade.COLLECTOR
									.getItem())) {
						c = i;
						break;
					}
				}
			}
			if (c != -1) {
				BagInventory inv = ItemBag.getBagInventory(player.inventory
						.getStackInSlot(c));
				ItemStack ret = InvUtil.putIntoFirstSlot(inv, item, false);
				if (ret == null) {
					event.setCanceled(true);
					eitem.setDead();
				} else if (!ItemStack.areItemStacksEqual(item, ret)) {
					eitem.setEntityItemStack(ret);
				}
			}
		}
	}

}
