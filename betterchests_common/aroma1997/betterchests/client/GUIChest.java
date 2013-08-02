package aroma1997.betterchests.client;

import aroma1997.betterchests.ContainerBChest;
import aroma1997.betterchests.Reference;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;


public class GUIChest extends GuiContainer {

	public GUIChest(ContainerBChest container) {
		super(container);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.func_110577_a(new ResourceLocation(Reference.MOD_ID
			+ ":textures/gui/ChestGUIpng"));
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}
	
}
