/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.core.block.AromicBlockContainer;
import aroma1997.core.inventories.Inventories;
import aroma1997.core.util.WorldUtil;

public class BlockBChest extends AromicBlockContainer {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockBChest() {
		super(Material.rock);
		setHardness(3.0F);
		setCreativeTab(BetterChests.creativeTabBC);
		setUnlocalizedName("betterchests:betterChest");
		setLightOpacity(0);
		disableStats();
		setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityBChest();
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
    public boolean isFullCube()
    {
        return false;
    }

	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		TileEntityBChest chest = (TileEntityBChest) world.getTileEntity(
				pos);
		if (player.isSneaking() || chest == null) {
			return false;
		}
		if (world.isRemote) {
			return true;
		}
		if (!chest.isUseableByPlayer(player)) {

			player
					.attackEntityFrom(DamageSourceBChest.INSTANCE, 2.0F);
			return true;
		}

		ItemStack item = player.getHeldItem();

		if (item == null || !UpgradeHelper.isUpgrade(item)) {
			Inventories.openContainerTileEntity(player, chest, true);
			return true;
		}
		if (((IUpgrade) item.getItem()).canChestTakeUpgrade(item)) {
			return chest.upgrade(player);
		}
		Inventories.openContainerTileEntity(player, chest, true);
		return true;

	}

	@Override
    public int isProvidingWeakPower(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing side)
    {
		TileEntity te = world.getTileEntity(pos);
		if (te == null)
			return 0;
		return ((TileEntityBChest) te).getRedstoneOutput();
	}

	@Override
    public boolean shouldCheckWeakPower(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
		return false;
	}

	@Override
    public int getLightValue(IBlockAccess world, BlockPos pos)
    {
		TileEntityBChest te = ((TileEntityBChest) world.getTileEntity(pos));
		if (te != null) return te.getLightValue();
		return super.getLightValue(world, pos);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world,
			BlockPos pos) {

	}

	@Override
	@SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos)
    {
		return 15;
	}

	@Override
    public boolean canEntityDestroy(IBlockAccess world, BlockPos pos, Entity entity)
    {
		return !((TileEntityBChest) world.getTileEntity(pos))
				.isUpgradeInstalled(Upgrade.UNBREAKABLE.getItem())
				&& !super.canEntityDestroy(world, pos, entity);
	}

	@Override
    public int getComparatorInputOverride(World world, BlockPos pos)
    {
		return ((TileEntityBChest) world.getTileEntity(pos))
				.getComparatorOutput();
	}

	@Override
    public boolean canConnectRedstone(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
		return ((TileEntityBChest) world.getTileEntity(pos))
				.isUpgradeInstalled(Upgrade.REDSTONE.getItem());
	}

	@Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
    {
		if (((TileEntityBChest) world.getTileEntity(pos))
				.isUpgradeInstalled(Upgrade.UNBREAKABLE.getItem())) {
			return;
		}
		super.onBlockExploded(world, pos, explosion);
	}

	@Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
		return false;
	}

	@Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, BlockPos pos)
    {
		if (world.isRemote) {
			return super.getPlayerRelativeBlockHardness(player, world, pos);
		}
		if (!((TileEntityBChest) world.getTileEntity(pos))
				.isUseableByPlayer(player)) {
			return -1.0F;
		} else {
			return super.getPlayerRelativeBlockHardness(player, world, pos);
		}
	}

	@Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
		TileEntityBChest te = (TileEntityBChest) world.getTileEntity(pos);
		if (te == null) {
			super.breakBlock(world, pos, state);
			return;
		}
		ItemStack[] items = te.getItems();
		for (ItemStack item : items) {
			ItemStack itemstack = item;

			if (itemstack != null) {
				WorldUtil.dropItemsRandom(world, item, pos);
			}
		}
		for (ItemStack item : te.getUpgrades()) {
			if (item != null) {
				UpgradeHelper.enableUpgrade(item);
				WorldUtil.dropItemsRandom(world, item, pos);
			}
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
        state = state.withProperty(BlockChest.FACING_PROP, enumfacing);

        worldIn.setBlockState(pos, state, 3);
    }
	
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(BlockChest.FACING_PROP, placer.func_174811_aO());
    }

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
    public int isProvidingStrongPower(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing side)
    {
		return isProvidingWeakPower(world, pos, state, side);
	}
	
	@Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING});
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

	@Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

}
