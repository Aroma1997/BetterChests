package aroma1997.betterchests.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import aroma1997.betterchests.BlocksItemsBetterChests;

import static aroma1997.core.client.models.ModelHelper.ONE_PIXEL;

public class LayerBag extends LayerBipedArmor {

	private final ItemStack bag = new ItemStack(BlocksItemsBetterChests.betterbag);

	public LayerBag() {
		super(null);
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount,
	                          float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

		GL11.glPushMatrix();
		GL11.glRotatef(180, 0, 0, 1);
		GL11.glTranslatef(0, -1 * ONE_PIXEL, 4 * ONE_PIXEL);
		Minecraft.getMinecraft().getRenderItem().renderItem(bag, ItemCameraTransforms.TransformType.NONE);
		GL11.glPopMatrix();
	}
}
