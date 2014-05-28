package aroma1997.betterchests.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.ItemBag;
import aroma1997.betterchests.PacketOpenBag;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public class EventListenerClient {
	
	public EventListenerClient() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	boolean pressedBefore = false;
	
	@SubscribeEvent
	public void tick(ClientTickEvent event) {
		if (BetterChestsKeyBinding.getInstance().getIsKeyPressed() && !pressedBefore) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			for (int i = 0; i < player.inventory.getSizeInventory(); i++ ) {
				if (player.inventory.getStackInSlot(i) != null
				        && player.inventory.getStackInSlot(i).getItem() instanceof ItemBag) {
					BetterChests.ph.sendPacketToPlayers(new PacketOpenBag().setSlot(i));
					break;
				}
			}
		}
		pressedBefore = BetterChestsKeyBinding.getInstance().getIsKeyPressed();
	}
	
}
