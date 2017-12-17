package aroma1997.betterchests.chest;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import aroma1997.core.container.ContainerHelper;
import aroma1997.core.inventory.InvUtil;
import aroma1997.betterchests.api.IUpgrade;

public class BlockBetterChest extends BlockChestBase {

	public static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(1/16F, 0/16F, 1/16F, 15/16F, 14/16F, 15/16F);

	public BlockBetterChest() {
		super(Material.ROCK);
		setUnlocalizedName("betterchests:betterchest");
		setHardness(2.5F);
	}

	@Override
	public Class<? extends TileEntity> getTeClass() {
		return TileEntityBChest.class;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityBChest) {
			TileEntityBChest chest = (TileEntityBChest) te;
			ItemStack currentItem = player.getHeldItem(hand);
			if (currentItem.getItem() instanceof IUpgrade) {
				IUpgrade upgrade = (IUpgrade) currentItem.getItem();
				ItemStack toPut = currentItem.copy();
				toPut.setCount(1);
				if (upgrade.canBePutInChest(chest, toPut)) {
					if (InvUtil.putStackInInventoryFirst(toPut, chest.getUpgradePart(), false, false, false, null).isEmpty()) {
						currentItem.setCount(currentItem.getCount() - 1);
						chest.markDirty();
						return true;
					}
				}
			}
			ContainerHelper.openGui((TileEntityBChest) te, player, (short) (player.isSneaking() ? 1 : 0));
		}

		return true;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}
}
