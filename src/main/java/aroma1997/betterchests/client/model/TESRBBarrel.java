package aroma1997.betterchests.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.betterchests.chest.TileEntityBBarrel;

@SideOnly(Side.CLIENT)
public class TESRBBarrel extends TileEntitySpecialRenderer<TileEntityBBarrel> {

	public static final TESRBBarrel INSTANCE = new TESRBBarrel();

	private TESRBBarrel(){}

	@Override
	public void render(TileEntityBBarrel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		if (te.getChestPart().isItemSet()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x, (float) y, (float) z);
			GlStateManager.translate(0.5F, 0.5F, 0.5F);

			ItemStack stack = te.getChestPart().getDummy();

			EnumFacing side = EnumFacing.HORIZONTALS[te.getBlockMetadata()];

			float angle = side.getHorizontalAngle();
			switch (side) {
			case SOUTH:
				//south
				angle = 180;
				break;
			case WEST:
				//west
				angle = 90;
				break;
			case NORTH:
				//north
				angle = 0;
				break;
			case EAST:
				//east
				angle = -90;
				break;
			}
			GlStateManager.rotate(angle, 0, 1, 0);

			{
				GlStateManager.pushMatrix();

				GlStateManager.translate(0, 0.1, -0.5);
				GlStateManager.scale(0.8, 0.8, 0.8);

				int skyBrightness = te.getWorld().getCombinedLight(te.getPosition().offset(side), 0);
				int skyBrightness1 = skyBrightness % 65536;
				int skyBrightness2 = skyBrightness / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, skyBrightness1,
						skyBrightness2);

				Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.FIXED);

				GlStateManager.popMatrix();
			}

			{
				GlStateManager.pushMatrix();

				GlStateManager.translate(0, -0.25, -0.51);
				GlStateManager.scale(0.015, 0.015, 0.015);
				GlStateManager.rotate(180, 0, 0, 1);

				String text = te.getChestPart().getAmountDescr();
				int textlen = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
				Minecraft.getMinecraft().fontRenderer.drawString(text, -textlen / 2, 0, 0xFFFFFFFF);

				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
		}

	}
}
