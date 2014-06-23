package aroma1997.betterchests.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.ItemBag;
import aroma1997.betterchests.PacketOpenBag;
import aroma1997.core.util.InvUtil;
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
	
	@SubscribeEvent
	public void renderPlayer(RenderPlayerEvent.Specials.Post event) {
		
		int i = InvUtil.getFirstItem(event.entityPlayer.inventory, ItemBag.class);
		if (i == -1 || i == event.entityPlayer.inventory.currentItem) return;
		ItemStack item = event.entityPlayer.inventory.getStackInSlot(i);
		
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (event.entityPlayer.isSneaking()) {
        	GL11.glRotatef(28.6F, 1.0F, 0.0F, 0.0F);
        }
        GL11.glTranslatef(0.0F, -0.03F, 0.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(ModelBackPack.image);
        ModelBackPack.instance.renderFromItem(event.entityPlayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, item);
        GL11.glPopMatrix();
	}
	
}
