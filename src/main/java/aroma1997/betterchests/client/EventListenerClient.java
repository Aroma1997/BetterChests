package aroma1997.betterchests.client;

import aroma1997.betterchests.ItemBag;
import aroma1997.betterchests.PacketOpenBag;
import aroma1997.core.network.NetworkHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

import net.minecraftforge.common.MinecraftForge;

public class EventListenerClient {
	
	public EventListenerClient() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@SubscribeEvent
	public void tick(ClientTickEvent event) {
		if (BetterChestsKeyBinding.getInstance().getIsKeyPressed()) {
			NBTTagCompound nbt = new NBTTagCompound();
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			for (int i = 0; i < player.inventory.getSizeInventory(); i++ ) {
				if (player.inventory.getStackInSlot(i) != null
				        && player.inventory.getStackInSlot(i).getItem() instanceof ItemBag) {
					NetworkHelper.sendPacketToPlayers(new PacketOpenBag().setSlot(i));
					break;
				}
			}
		}
	}
	
}
