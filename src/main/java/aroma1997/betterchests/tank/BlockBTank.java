package aroma1997.betterchests.tank;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.inventory.InvUtil;
import aroma1997.core.util.WorldUtil;
import aroma1997.betterchests.chest.BlockChestBase;

public class BlockBTank extends BlockChestBase {
	public BlockBTank() {
		super(Material.ROCK);
		setUnlocalizedName("betterchests:bettertank");
	}

	@Override
	public Class<? extends TileEntity> getTeClass() {
		return TileEntityBTank.class;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack original = player.getHeldItem(hand);
		if (!original.isEmpty()) {
			ItemStack stack = original.copy();
			stack.setCount(1);
			TileEntity tank = getTileEntity(world, pos);
			if (tank instanceof TileEntityBTank) {
				IFluidHandler handler = ((TileEntityBTank) tank).getFluidHandler();
				FluidActionResult result = FluidUtil.tryEmptyContainer(stack, handler, Fluid.BUCKET_VOLUME, player, true);
				if (!result.success) {
					result = FluidUtil.tryFillContainer(stack, handler, Fluid.BUCKET_VOLUME, player, true);
				}
				if (result.success) {
					original.setCount(original.getCount() - 1);
					stack = InvUtil.putStackInInventory(result.result, player.inventory, true, false, false, null);
					if (!stack.isEmpty()) {
						WorldUtil.dropItemsRandom(world, stack, player.getPosition());
					}
					return true;
				}
			}
		}
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
