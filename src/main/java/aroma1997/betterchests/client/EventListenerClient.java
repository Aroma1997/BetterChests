
package aroma1997.betterchests.client;


import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Level;

import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.ItemBag;
import aroma1997.betterchests.PacketOpenBag;
import aroma1997.betterchests.Reference;
import aroma1997.core.network.NetworkHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EventListenerClient {
	
	public EventListenerClient() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	private static final String PREFIX = Reference.MOD_ID + ":";
	
	public static final String SOUND_OPEN_CHEST = PREFIX + "bchestopen";
	
	public static final String SOUND_CLOSE_CHEST = PREFIX + "bchestclose";
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void loadSounds(SoundLoadEvent event) {
		for (Field field : getClass().getFields()) {
			if (! field.isAccessible()) {
				field.setAccessible(true);
			}
			Object o;
			try {
				o = field.get(null);
			}
			catch (Exception e1) {
				continue;
			}
			if (o != null && o instanceof String && field.getName().startsWith("SOUND_")) {
				String str = (String) o;
				BetterChests.logger.log(Level.TRACE, "Loading sound: " + str);
				try {
//					event.manager.addSound(str + ".ogg");
				}
				catch (Exception e) {
					BetterChests.logger.log(Level.ERROR, "Failed to load sound: " + str, e);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void tick(ClientTickEvent event) {
		if (BetterChestsKeyBinding.getInstance().getIsKeyPressed()) {
			NBTTagCompound nbt = new NBTTagCompound();
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() instanceof ItemBag) {
					NetworkHelper.sendPacketToPlayers(new PacketOpenBag().setSlot(i));
					break;
				}
			}
		}
	}
	
}
