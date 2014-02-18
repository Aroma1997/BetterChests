
package aroma1997.betterchests.client;


import java.lang.reflect.Field;
import java.util.logging.Level;

import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.Reference;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EventListenerClient {
	
	public EventListenerClient() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private static final String PREFIX = Reference.MOD_ID + ":";
	
	public static final String SOUND_OPEN_CHEST = PREFIX + "bchestopen";
	
	public static final String SOUND_CLOSE_CHEST = PREFIX + "bchestclose";
	
	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
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
				BetterChests.logger.log(Level.FINE, "Loading sound: " + str);
				try {
					event.manager.soundPoolSounds.addSound(str + ".ogg");
				}
				catch (Exception e) {
					BetterChests.logger.log(Level.SEVERE, "Failed to load sound: " + str, e);
				}
			}
		}
	}
	
}
