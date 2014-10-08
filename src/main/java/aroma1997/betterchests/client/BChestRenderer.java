/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import aroma1997.betterchests.Reference;
import aroma1997.betterchests.TileEntityBChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BChestRenderer extends TileEntitySpecialRenderer {

	private ResourceLocation model = new ResourceLocation(Reference.MOD_ID
			+ ":textures/blocks/tile.betterChest.png");

	@SuppressWarnings("unused")
	private ResourceLocation modelLight = new ResourceLocation(Reference.MOD_ID
			+ ":textures/blocks/tile.betterChestLight.png");

	@SuppressWarnings("unused")
	private ResourceLocation modelSolar = new ResourceLocation(Reference.MOD_ID
			+ ":textures/blocks/tile.betterChestSolar.png");

	private ModelChest chestModel = new ModelChest();

	public void renderTileEntityChestAt(TileEntityBChest par1TileEntityChest,
			double par2, double par4, double par6, float par8) {
		int i;

		if (!par1TileEntityChest.hasWorldObj()) {
			i = 0;
		} else {
			Block block = par1TileEntityChest.getBlockType();
			i = par1TileEntityChest.getBlockMetadata();

			if (block instanceof BlockChest && i == 0) {
				i = par1TileEntityChest.getBlockMetadata();
			}
		}
		ModelChest modelchest = chestModel;
		bindTexture(model);

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float) par2, (float) par4 + 1.0F,
				(float) par6 + 1.0F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		short short1 = 0;

		if (i == 2) {
			short1 = 180;
		}

		if (i == 3) {
			short1 = 0;
		}

		if (i == 4) {
			short1 = 90;
		}

		if (i == 5) {
			short1 = -90;
		}

		GL11.glRotatef(short1, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		float f1 = par1TileEntityChest.prevLidAngle
				+ (par1TileEntityChest.lidAngle - par1TileEntityChest.prevLidAngle)
				* par8;

		f1 = 1.0F - f1;
		f1 = 1.0F - f1 * f1 * f1;
		modelchest.chestLid.rotateAngleX = -(f1 * (float) Math.PI / 2.0F);
		modelchest.renderAll();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void renderTileEntityAt(TileEntity par1TileEntity, double par2,
			double par4, double par6, float par8) {
		renderTileEntityChestAt((TileEntityBChest) par1TileEntity, par2, par4,
				par6, par8);
	}

}
