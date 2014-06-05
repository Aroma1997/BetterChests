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
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.core.inventories.Inventories;
import aroma1997.core.util.WorldUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBChest extends BlockContainer {
	
	public BlockBChest() {
		super(Material.rock);
		setHardness(3.0F);
		setCreativeTab(BetterChests.creativeTabBC);
		setBlockName("betterchests:betterChest");
		setLightOpacity(0);
		disableStats();
		setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityBChest();
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return 22;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4,
	        EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		TileEntityBChest chest = (TileEntityBChest) par1World.getTileEntity(par2, par3, par4);
		if (par5EntityPlayer.isSneaking() || chest == null) {
			return false;
		}
		if (par1World.isRemote) {
			return true;
		}
		if (! chest.isUseableByPlayer(par5EntityPlayer)) {
			
			par5EntityPlayer.attackEntityFrom(DamageSourceBChest.INSTANCE, 2.0F);
			return true;
		}
		
		ItemStack item = par5EntityPlayer.getHeldItem();
		
		if (item == null || ! UpgradeHelper.isUpgrade(item)) {
			Inventories.openContainerTileEntity(par5EntityPlayer, chest, true);
			return true;
		}
		if (((IUpgrade) item.getItem()).canChestTakeUpgrade(item)) {
			return chest.upgrade(par5EntityPlayer);
		}
		Inventories.openContainerTileEntity(par5EntityPlayer, chest, true);
		return true;
		
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4,
	        int par5) {
		return ((TileEntityBChest) par1IBlockAccess.getTileEntity(par2, par3, par4))
		        .getRedstoneOutput();
	}
	
	@Override
	public boolean shouldCheckWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		return false;
	}
	
	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return ((TileEntityBChest) world.getTileEntity(x, y, z)).getLightValue();
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3,
	        int par4) {
		
	}
	
	@Override
	public int getMixedBrightnessForBlock(IBlockAccess par1IBlockAccess, int par2, int par3,
	        int par4) {
		return 15;
	}
	
	@Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
		return ! ((TileEntityBChest) world.getTileEntity(x, y, z))
		        .isUpgradeInstalled(Upgrade.UNBREAKABLE.getItem())
		        && ! super.canEntityDestroy(world, x, y, z, entity);
	}
	
	@Override
	public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
		return ((TileEntityBChest) par1World.getTileEntity(par2, par3, par4)).getComparatorOutput();
	}
	
	@Override
	public String getItemIconName() {
		return Reference.MOD_ID + ":chest";
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
		return ((TileEntityBChest) world.getTileEntity(x, y, z))
		        .isUpgradeInstalled(Upgrade.REDSTONE.getItem());
	}
	
	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
		if (((TileEntityBChest) world.getTileEntity(x, y, z))
		        .isUpgradeInstalled(Upgrade.UNBREAKABLE.getItem())) {
			return;
		}
		super.onBlockExploded(world, x, y, z, explosion);
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		return false;
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer par1EntityPlayer, World par2World,
	        int par3, int par4, int par5) {
		if (par2World.isRemote) {
			return super.getPlayerRelativeBlockHardness(par1EntityPlayer, par2World, par3, par4,
			        par5);
		}
		if (! ((TileEntityBChest) par2World.getTileEntity(par3, par4, par5))
		        .isUseableByPlayer(par1EntityPlayer)) {
			return - 1.0F;
		}
		else {
			return super.getPlayerRelativeBlockHardness(par1EntityPlayer, par2World, par3, par4,
			        par5);
		}
	}
	
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
		TileEntityBChest te = (TileEntityBChest) par1World.getTileEntity(par2, par3, par4);
		if (te == null) {
			super.breakBlock(par1World, par2, par3, par4, par5, par6);
			return;
		}
		ItemStack[] items = te.getItems();
		for (ItemStack item : items) {
			ItemStack itemstack = item;
			
			if (itemstack != null) {
				WorldUtil.dropItemsRandom(par1World, item, par2, par3, par4);
			}
		}
		for (ItemStack item : te.getUpgrades()) {
			if (item != null) {
				UpgradeHelper.enableUpgrade(item);
				WorldUtil.dropItemsRandom(par1World, item, par2, par3, par4);
			}
		}
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
	
	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return true;
	}
	
	@Override
	public boolean canProvidePower() {
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4,
	        EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		byte facing = 0;
		int rotation = MathHelper
		        .floor_double(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		
		if (rotation == 0) {
			facing = 2;
		}
		
		if (rotation == 1) {
			facing = 5;
		}
		
		if (rotation == 2) {
			facing = 3;
		}
		
		if (rotation == 3) {
			facing = 4;
		}
		par1World.setBlockMetadataWithNotify(par2, par3, par4, facing, 3);
	}
	
	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}
	
	@Override
	public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_,
	        int p_149748_4_, int p_149748_5_) {
		return isProvidingWeakPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
		return Blocks.cobblestone.getIcon(p_149691_1_, p_149691_2_);
    }
	
}
