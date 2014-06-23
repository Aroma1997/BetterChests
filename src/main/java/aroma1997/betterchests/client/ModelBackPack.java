package aroma1997.betterchests.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import aroma1997.betterchests.BagInventory;
import aroma1997.betterchests.ItemBag;
import aroma1997.betterchests.Upgrade;

public class ModelBackPack extends ModelBase {

	public static ModelBackPack instance = new ModelBackPack();
	public static ResourceLocation image = new ResourceLocation(
			"betterchests:models/backpack.png");
	// fields
	ModelRenderer BackPackRightSide;
	ModelRenderer BackPackFront;
	ModelRenderer BackPackLeftSide;
	ModelRenderer BackPackBody;

	public ModelBackPack() {
		textureWidth = 128;
		textureHeight = 64;

		BackPackRightSide = new ModelRenderer(this, 0, 34);
		BackPackRightSide.addBox(-4F, 0F, -2F, 3, 4, 4);
		BackPackRightSide.setRotationPoint(-3F, 6F, 4F);
		BackPackRightSide.setTextureSize(128, 64);
		BackPackRightSide.mirror = true;
		setRotation(BackPackRightSide, 0F, 0F, 0F);
		BackPackFront = new ModelRenderer(this, 0, 26);
		BackPackFront.addBox(-4F, 0F, -2F, 8, 4, 3);
		BackPackFront.setRotationPoint(0F, 6F, 7F);
		BackPackFront.setTextureSize(128, 64);
		BackPackFront.mirror = true;
		setRotation(BackPackFront, 0F, 0F, 0F);
		BackPackLeftSide = new ModelRenderer(this, 0, 43);
		BackPackLeftSide.addBox(-4F, 0F, -2F, 3, 4, 4);
		BackPackLeftSide.setRotationPoint(8F, 6F, 4F);
		BackPackLeftSide.setTextureSize(128, 64);
		BackPackLeftSide.mirror = true;
		setRotation(BackPackLeftSide, 0F, 0F, 0F);
		BackPackBody = new ModelRenderer(this, 0, 52);
		BackPackBody.addBox(-4F, 0F, -2F, 8, 8, 4);
		BackPackBody.setRotationPoint(0F, 2F, 4F);
		BackPackBody.setTextureSize(128, 64);
		BackPackBody.mirror = true;
		setRotation(BackPackBody, 0F, 0F, 0F);
	}

	public void renderFromItem(Entity entity, float f, float f1, float f2,
			float f3, float f4, float f5, ItemStack item) {
		BagInventory inv = ItemBag.getInventory(item);
		GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
		render(entity, f, f1, f2, f3, f4, f5);
		int slots = inv.getAmountUpgrade(Upgrade.SLOT.getItem());
		if (slots == 1 || slots == 2 || slots >= 4) {
			BackPackRightSide.render(f5);
		}
		if (slots == 2 || slots >= 5) {
			BackPackLeftSide.render(f5);
		}
		if (slots >= 3) {
			BackPackFront.render(f5);
		}
	}

	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		BackPackBody.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}