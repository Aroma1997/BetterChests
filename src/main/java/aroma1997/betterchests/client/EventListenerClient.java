package aroma1997.betterchests.client;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import aroma1997.betterchests.ItemBag;
import aroma1997.core.inventories.Inventories;
import aroma1997.core.util.InvUtil;

@SideOnly(Side.CLIENT)
public class EventListenerClient {

	public EventListenerClient() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	boolean pressedBefore = false;

	@SubscribeEvent
	public void tick(ClientTickEvent event) {
		if (ClientProxy.openBag.isPressed() && !pressedBefore
				&& Minecraft.getMinecraft().theWorld != null) {
			int i = InvUtil
					.getFirstItem(Minecraft.getMinecraft().thePlayer.inventory,
							ItemBag.class);
			if (i != -1) {
				Inventories.sendItemInventoryOpen(i);
			}
		}
		pressedBefore = ClientProxy.openBag.isPressed();
	}

	@SubscribeEvent
	public void renderPlayer(RenderPlayerEvent.Specials.Post event) {

		int i = InvUtil.getFirstItem(event.entityPlayer.inventory,
				ItemBag.class);
		if (i == -1 || i == event.entityPlayer.inventory.currentItem)
			return;
		ItemStack item = event.entityPlayer.inventory.getStackInSlot(i);

		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (event.entityPlayer.isSneaking()) {
			GL11.glRotatef(28.6F, 1.0F, 0.0F, 0.0F);
		}
		GL11.glTranslatef(0.0F, -0.03F, 0.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(ModelBackPack.image);
		ModelBackPack.instance.renderFromItem(event.entityPlayer, 0.0F, 0.0F,
				0.0F, 0.0F, 0.0F, 0.0625F, item);
		GL11.glPopMatrix();
	}

}
