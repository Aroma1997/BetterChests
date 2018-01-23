package aroma1997.betterchests.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import aroma1997.core.client.util.RenderHelper;
import aroma1997.betterchests.tank.TileEntityBTank;

public class TESRBTank extends TileEntitySpecialRenderer<TileEntityBTank> {

	public static final TESRBTank INSTANCE = new TESRBTank();

	private TESRBTank(){}

	@Override
	public void render(TileEntityBTank te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		FluidTank tank = te.getTank();
		FluidStack stack = tank.getFluid();
		if (stack != null) {

			float fillPercentage = ((float)stack.amount) / tank.getCapacity();
			boolean gaseous = stack.getFluid().isGaseous(stack);
			GlStateManager.pushMatrix();
			GL11.glEnable(GL11.GL_BLEND);

			ResourceLocation loc = stack.getFluid().getStill(stack);
			TextureAtlasSprite sprite = RenderHelper.getAtlasSprite(loc);
			RenderHelper.bindBlockTexture();

			float minY = !gaseous ? 0 : 1 - fillPercentage;
			float maxY = gaseous ? 1 : fillPercentage;

			float minU = sprite.getMinU();
			float maxU = sprite.getMaxU();
			float minV = sprite.getMinV();
			float maxV = sprite.getMaxV();
			float minVHorizontal = minV + (maxV - minV) * minY;
			float maxVHorizontal = minV + (maxV - minV) * maxY;

			GlStateManager.translate(x, y, z);

			GlStateManager.translate(0.5, 0.5, 0.5);
			GlStateManager.scale(12/16D, 12/16D, 12/16D);
			GlStateManager.translate(-0.5, -0.5, -0.5);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder builder = tessellator.getBuffer();

			//south
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			builder.pos(0, minY, 1).tex(minU, minVHorizontal).endVertex();
			builder.pos(1, minY, 1).tex(maxU, minVHorizontal).endVertex();
			builder.pos(1, maxY, 1).tex(maxU, maxVHorizontal).endVertex();
			builder.pos(0, maxY, 1).tex(minU, maxVHorizontal).endVertex();
			tessellator.draw();

			//north
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			builder.pos(1, minY, 0).tex(minU, minVHorizontal).endVertex();
			builder.pos(0, minY, 0).tex(maxU, minVHorizontal).endVertex();
			builder.pos(0, maxY, 0).tex(maxU, maxVHorizontal).endVertex();
			builder.pos(1, maxY, 0).tex(minU, maxVHorizontal).endVertex();
			tessellator.draw();

			//east
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			builder.pos(1, minY, 1).tex(minU, minVHorizontal).endVertex();
			builder.pos(1, minY, 0).tex(maxU, minVHorizontal).endVertex();
			builder.pos(1, maxY, 0).tex(maxU, maxVHorizontal).endVertex();
			builder.pos(1, maxY, 1).tex(minU, maxVHorizontal).endVertex();
			tessellator.draw();

			//west
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			builder.pos(0, minY, 0).tex(minU, minVHorizontal).endVertex();
			builder.pos(0, minY, 1).tex(maxU, minVHorizontal).endVertex();
			builder.pos(0, maxY, 1).tex(maxU, maxVHorizontal).endVertex();
			builder.pos(0, maxY, 0).tex(minU, maxVHorizontal).endVertex();
			tessellator.draw();

			//up
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			builder.pos(1, maxY, 0).tex(minU, minV).endVertex();
			builder.pos(0, maxY, 0).tex(maxU, minV).endVertex();
			builder.pos(0, maxY, 1).tex(maxU, maxV).endVertex();
			builder.pos(1, maxY, 1).tex(minU, maxV).endVertex();
			tessellator.draw();

			//down
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			builder.pos(0, minY, 0).tex(minU, minV).endVertex();
			builder.pos(1, minY, 0).tex(maxU, minV).endVertex();
			builder.pos(1, minY, 1).tex(maxU, maxV).endVertex();
			builder.pos(0, minY, 1).tex(minU, maxV).endVertex();
			tessellator.draw();

			GL11.glDisable(GL11.GL_BLEND);
			GlStateManager.popMatrix();
		}
	}
}
