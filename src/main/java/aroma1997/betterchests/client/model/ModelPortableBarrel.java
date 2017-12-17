package aroma1997.betterchests.client.model;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

import aroma1997.core.client.models.IRenderableModel;
import aroma1997.betterchests.BlocksItemsBetterChests;
import aroma1997.betterchests.bag.InventoryBPortableBarrel;
import aroma1997.betterchests.inventories.InventoryPartBarrel;

public class ModelPortableBarrel implements IModel, IRenderableModel {

	private IBakedModel parent;
	private IModel parentModel;
	private static final ResourceLocation PARENT_RL = new ResourceLocation("betterchests:item/betterportablebarrel_base");

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
		return parent.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return parent.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return parent.isGui3d();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return parent.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return parent.getItemCameraTransforms();

	}

	@Override
	public void render(ItemStack stack, float patrtialTicks) {
		GlStateManager.pushMatrix();
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.renderItem(stack, parent);
		InventoryBPortableBarrel barrel = BlocksItemsBetterChests.betterportablebarrel.getInventoryFor(stack, null);
		if (barrel != null && barrel.getChestPart().isItemSet()) {
			InventoryPartBarrel part = barrel.getChestPart();
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, -0.05);
			GlStateManager.scale(0.8, 0.8, 0.8);

			renderItem.renderItem(part.getDummy(), TransformType.FIXED);

			GlStateManager.enableBlend();
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		try {
			parentModel = ModelLoaderRegistry.getModel(PARENT_RL);
		} catch (Exception e) {
			e.printStackTrace();
			parentModel = ModelLoaderRegistry.getMissingModel();
		}
		parent = parentModel.bake(state, format, bakedTextureGetter);
		return this;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.of(PARENT_RL);
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
 		Pair<? extends IBakedModel, Matrix4f> p = parent.handlePerspective(cameraTransformType);
		return ImmutablePair.of(this, p.getRight());

	}
}
