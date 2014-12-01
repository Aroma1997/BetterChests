package aroma1997.betterchests.client;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BagRenderer implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type != ItemRenderType.FIRST_PERSON_MAP;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(ModelBackPack.image);
		GL11.glScalef(1.6F, 1.6F, 1.6F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 1.0F);
		if (type == ItemRenderType.EQUIPPED) {
			GL11.glScalef(1.5F, 1.5F, 1.5F);
			GL11.glRotatef(120.0F, 0.0F, -1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -0.5F, -0.5F);
		} else if (type == ItemRenderType.INVENTORY) {
			GL11.glTranslatef(-0.3F, -0.2F, -0.5F);
		} else if (type == ItemRenderType.ENTITY) {
			GL11.glTranslatef(0.0F, -0.5F, -0.3F);
		} else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			GL11.glTranslatef(0.4F, -1.0F, 0.5F);
			GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
		}
		ModelBackPack.instance.renderFromItem(null, 0.0F, 0.0F, 0.0F, 0.0F,
				0.0F, 0.0625F, item);
		GL11.glPopMatrix();
	}

}
