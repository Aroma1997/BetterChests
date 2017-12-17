package aroma1997.betterchests.chest;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import aroma1997.core.block.AromicBlockContainer;
import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.api.IUpgradableBlock;

public abstract class BlockChestBase extends AromicBlockContainer {

	public static final PropertyEnum<EnumFacing> directions = PropertyEnum.create("direction", EnumFacing.class, EnumFacing.HORIZONTALS);

	protected BlockChestBase(Material materialIn) {
		super(materialIn);
		setCreativeTab(BetterChests.creativeTab);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof IUpgradableBlock && UpgradeHelper.INSTANCE.booleanSum((IUpgradableBlock) te, ChestModifier.HARDNESS, false)) {
			return Float.POSITIVE_INFINITY;
		}
		return super.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, directions);
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(directions, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(directions, EnumFacing.HORIZONTALS[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(directions).getHorizontalIndex();
	}

}
