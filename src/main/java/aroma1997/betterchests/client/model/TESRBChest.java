package aroma1997.betterchests.client.model;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.betterchests.chest.TileEntityBChest;

@SideOnly(Side.CLIENT)
public class TESRBChest extends TileEntitySpecialRenderer<TileEntityBChest> {

	public static final TESRBChest INSTANCE = new TESRBChest();
	public static final ItemCameraTransforms TRANSFORMS = new ItemCameraTransforms(
			new ItemTransformVec3f( //Thirdperson left
					new Vector3f(75.0F, 315.0F, 0.0F),
					new Vector3f(0.0F, 0.15625F, 0.0F),
					new Vector3f(0.375F, 0.375F, 0.375F)),
			new ItemTransformVec3f( //Thirdperson right
					new Vector3f(75.0F, 315.0F, 0.0F),
					new Vector3f(0.0F, 0.15625F, 0.0F),
					new Vector3f(0.375F, 0.375F, 0.375F)),
			new ItemTransformVec3f( //Firstperson left
					new Vector3f(0.0F, 315.0F, 0.0F),
					new Vector3f(0.0F, 0.0F, 0.0F),
					new Vector3f(0.4F, 0.4F, 0.4F)),
			new ItemTransformVec3f( //Firstperson right
					new Vector3f(0.0F, 315.0F, 0.0F),
					new Vector3f(0.0F, 0.0F, 0.0F),
					new Vector3f(0.4F, 0.4F, 0.4F)),
			new ItemTransformVec3f( //Head
					new Vector3f(0.0F, 180.0F, 0.0F),
					new Vector3f(0.0F, 0.0F, 0.0F),
					new Vector3f(1.0F, 1.0F, 1.0F)),
			new ItemTransformVec3f( //GUI
					new Vector3f(30.0F, 45.0F, 0.0F),
					new Vector3f(0.0F, 0.0F, 0.0F),
					new Vector3f(0.625F, 0.625F, 0.625F)),
			new ItemTransformVec3f( //Ground
					new Vector3f(0.0F, 0.0F, 0.0F),
					new Vector3f(0.0F, 0.1875F, 0.0F),
					new Vector3f(0.25F, 0.25F, 0.25F)),
			new ItemTransformVec3f( //Fixed
					new Vector3f(0.0F, 180.0F, 0.0F),
					new Vector3f(0.0F, 0.0F, 0.0F),
					new Vector3f(0.5F, 0.5F, 0.5F))
	);

	private ModelChest model = new ModelChest();
	private ResourceLocation texture = new ResourceLocation("betterchests:textures/blocks/betterchest.png");

	private TESRBChest() {}

	@Override
	public void render(TileEntityBChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GL11.glPushMatrix();
		bindTexture(texture);

		GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
		GlStateManager.scale(1.0F, -1.0F, -1.0F);
		GlStateManager.translate(0.5F, 0.5F, 0.5F);

		float angle = 0;
		if (te != null) {
			switch (te.getBlockMetadata()) {
			case 0:
				//south
				angle = 0;
				break;
			case 1:
				//west
				angle = 90;
				break;
			case 2:
				//north
				angle = 180;
				break;
			case 3:
				//east
				angle = -90;
				break;
			}


			float f = te.prevAngle + (te.angle - te.prevAngle) * partialTicks;
			f = 1 - f;
			f = 1 - f * f * f;
			model.chestLid.rotateAngleX = (float) (-f * Math.PI / 2);

		} else {
			model.chestLid.rotateAngleX = 0;
		}
		GlStateManager.rotate(angle, 0, 1, 0);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		model.renderAll();
		GL11.glPopMatrix();
	}
}
