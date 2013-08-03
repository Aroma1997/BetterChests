package aroma1997.betterchests;

import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockBChest extends BlockChest {
	
	public BlockBChest(int id) {
		super(id, -1);
		setHardness(3.0F);
		setCreativeTab(BetterChests.creativeTabBC);
		setUnlocalizedName("betterChest");
		setLightOpacity(0);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBChest();
	}
	
	@Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
		if (par5EntityPlayer.getHeldItem() == null || !(par5EntityPlayer.getHeldItem().itemID == BetterChests.upgrade.itemID && par5EntityPlayer.getHeldItem().getItemDamage() != Upgrade.BASIC.ordinal())){
			return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
		}
		
		TileEntityBChest te = (TileEntityBChest) par1World.getBlockTileEntity(par2, par3, par4);
		
		return te.upgrade(par5EntityPlayer, par1World);
		
    }
	
	@Override
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (!((TileEntityBChest)par1IBlockAccess.getBlockTileEntity(par2, par3, par4)).hasRedstoneUpgrade())
        {
            return 0;
        }
        else
        {
            int i1 = ((TileEntityChest)par1IBlockAccess.getBlockTileEntity(par2, par3, par4)).numUsingPlayers;
            return MathHelper.clamp_int(i1, 0, 15);
        }
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
		return ((TileEntityBChest)world.getBlockTileEntity(x, y, z)).getLightValue();
    }
	
	@Override
    public IInventory getInventory(World par1World, int par2, int par3, int par4)
    {
        Object object = (TileEntityChest)par1World.getBlockTileEntity(par2, par3, par4);

        if (object == null)
        {
            return null;
        }
        return (IInventory)object;
    }
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
		
    }
	
	@Override
    public int getMixedBrightnessForBlock(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return 15;
    }
	
	@Override
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("cobblestone");
    }
	
	@Override
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
		if (!((TileEntityBChest)par1World.getBlockTileEntity(par2, par3, par4)).isComparator()) {
			return 0;
		}
        return Container.calcRedstoneFromInventory(this.getInventory(par1World, par2, par3, par4));
    }
	
}
