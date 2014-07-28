package aroma1997.betterchests.upgrades;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.client.inventories.RenderHelper;
import aroma1997.core.client.inventories.RenderHelper.Tex;
import aroma1997.core.inventories.ContainerBasic;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Blocking extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void draw(GuiContainer gui, ContainerBasic container, int par1, int par2, ItemStack item) {
		int blocked = item.stackSize * 9;
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
		GL11.glEnable(GL11.GL_BLEND);
		for (int i = 0; i < blocked; i++) {
			Slot slot = container.getSlot(i + 36);
			RenderHelper.renderTex(gui, Tex.REDCROSS, slot.xDisplayPosition - 1, slot.yDisplayPosition - 1);
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

}
