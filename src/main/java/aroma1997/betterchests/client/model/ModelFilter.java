package aroma1997.betterchests.client.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.betterchests.filter.ItemFilter;

@SideOnly(Side.CLIENT)
public class ModelFilter implements IModel {

	private static ResourceLocation whitelistRL = new ResourceLocation("betterchests:item/filter.whitelist");
	private static ResourceLocation blacklistRL = new ResourceLocation("betterchests:item/filter.blacklist");

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.of(whitelistRL, blacklistRL);
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		IBakedModel whitelist;
		IBakedModel blacklist;
		try {
			whitelist = ModelLoaderRegistry.getModel(whitelistRL).bake(state, format, bakedTextureGetter);
			blacklist = ModelLoaderRegistry.getModel(blacklistRL).bake(state, format, bakedTextureGetter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new BakedModelFilter(whitelist, blacklist);
	}

	private static class BakedModelFilter implements IBakedModel {

		private final IBakedModel whitelist;
		private final IBakedModel blacklist;

		public BakedModelFilter(IBakedModel whitelist, IBakedModel blacklist) {
			this.whitelist = whitelist;
			this.blacklist = blacklist;
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
			return whitelist.getQuads(state, side, rand);
		}

		@Override
		public boolean isAmbientOcclusion() {
			return whitelist.isAmbientOcclusion();
		}

		@Override
		public boolean isGui3d() {
			return whitelist.isGui3d();
		}

		@Override
		public boolean isBuiltInRenderer() {
			return false;
		}

		@Override
		public TextureAtlasSprite getParticleTexture() {
			return whitelist.getParticleTexture();
		}

		@Override
		public ItemOverrideList getOverrides() {
			return new ItemOverrideList(Collections.emptyList()) {
				@Override
				public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
					if (ItemFilter.getInventoryFor(stack).isBlacklist()) {
						return blacklist;
					} else {
						return whitelist;
					}
				}
			};
		}
	}
}
