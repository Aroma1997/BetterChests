/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests;


import java.util.Random;

import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.ForgeDirection;

public class BlockBChest extends BlockChest {
	
	public BlockBChest(int id) {
		super(id, - 1);
		setHardness(3.0F);
		setCreativeTab(BetterChests.creativeTabBC);
		setUnlocalizedName("betterChest");
		setLightOpacity(0);
		disableStats();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBChest();
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4,
		EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		if (par5EntityPlayer.getHeldItem() == null
			|| ! (par5EntityPlayer.getHeldItem().itemID == BetterChests.upgrade.itemID && par5EntityPlayer.getHeldItem().getItemDamage() != Upgrade.BASIC.ordinal())) {
			
			((TileEntityBChest) par1World.getBlockTileEntity(par2, par3, par4)).playerOpenChest(par5EntityPlayer);
			
			return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6,
				par7, par8, par9);
		}
		
		TileEntityBChest te = (TileEntityBChest) par1World.getBlockTileEntity(par2, par3, par4);
		
		return te.upgrade(par5EntityPlayer);
		
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4,
		int par5)
	{
		return ((TileEntityBChest) par1IBlockAccess.getBlockTileEntity(par2, par3, par4)).getRedstoneOutput();
	}
	
	@Override
	public void unifyAdjacentChests(World par1World, int par2, int par3, int par4)
	{
		
	}
	
	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		return ((TileEntityBChest) world.getBlockTileEntity(x, y, z)).getLightValue();
	}
	
	@Override
	public IInventory getInventory(World par1World, int par2, int par3, int par4)
	{
		Object object = par1World.getBlockTileEntity(par2, par3, par4);
		
		if (object == null)
		{
			return null;
		}
		return (IInventory) object;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3,
		int par4)
	{
		
	}
	
	@Override
	public int getMixedBrightnessForBlock(IBlockAccess par1IBlockAccess, int par2, int par3,
		int par4)
	{
		return 15;
	}
	
	@Override
	public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity)
	{
		return ! ((TileEntityBChest) world.getBlockTileEntity(x, y, z)).isUpgradeInstalled(Upgrade.UNBREAKABLE);
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon("cobblestone");
	}
	
	@Override
	public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
	{
		return ((TileEntityBChest) par1World.getBlockTileEntity(par2, par3, par4)).getComparatorOutput();
	}
	
	@Override
	public String getItemIconName()
	{
		return Reference.MOD_ID + ":chest";
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
	{
		return ((TileEntityBChest) world.getBlockTileEntity(x, y, z)).isUpgradeInstalled(Upgrade.REDSTONE);
	}
	
	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
	{
		if (((TileEntityBChest) world.getBlockTileEntity(x, y, z)).isUpgradeInstalled(Upgrade.UNBREAKABLE)) {
			return;
		}
		super.onBlockExploded(world, x, y, z, explosion);
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata,
		ForgeDirection face)
	{
		return false;
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer par1EntityPlayer, World par2World,
		int par3, int par4, int par5)
	{
		if (! ((TileEntityBChest) par2World.getBlockTileEntity(par3, par4, par5)).isUseableByPlayer(par1EntityPlayer)) {
			return - 1.0F;
		}
		else {
			return super.getPlayerRelativeBlockHardness(par1EntityPlayer, par2World, par3, par4,
				par5);
		}
	}
	
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
	{
		Random random = new Random();
		TileEntityBChest te = (TileEntityBChest) par1World.getBlockTileEntity(par2, par3, par4);
		ItemStack[] items = te.getItemUpgrades();
		for (ItemStack item : items) {
			ItemStack itemstack = item;
			
			if (itemstack != null)
			{
				float f = random.nextFloat() * 0.8F + 0.1F;
				float f1 = random.nextFloat() * 0.8F + 0.1F;
				EntityItem entityitem;
				
				for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem))
				{
					int k1 = random.nextInt(21) + 10;
					
					if (k1 > itemstack.stackSize)
					{
						k1 = itemstack.stackSize;
					}
					
					itemstack.stackSize -= k1;
					entityitem = new EntityItem(par1World, par2 + f,
						par3 + f1, par4 + f2, new ItemStack(
							itemstack.itemID, k1, itemstack.getItemDamage()));
					float f3 = 0.05F;
					entityitem.motionX = (float) random.nextGaussian() * f3;
					entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float) random.nextGaussian() * f3;
					
					if (itemstack.hasTagCompound())
					{
						entityitem.getEntityItem().setTagCompound(
							(NBTTagCompound) itemstack.getTagCompound().copy());
					}
				}
			}
		}
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
	
	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
	{
		return true;
	}
	
	@Override
	public boolean canProvidePower()
	{
		return true;
	}
	
}
