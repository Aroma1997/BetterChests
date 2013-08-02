package aroma1997.betterchests;

import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


public class BlockBChest extends BlockChest {
	
	public BlockBChest(int id) {
		super(id, -1);
		setHardness(3.0F);
		setCreativeTab(BetterChests.creativeTabBC);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBChest();
	}
	
	@Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
		if (!(par5EntityPlayer.getHeldItem().getItem() instanceof ItemUpgrade)) return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
		
		TileEntityBChest te = (TileEntityBChest) par1World.getBlockTileEntity(par2, par3, par4);
		
		return te.upgrade(par5EntityPlayer, par1World);
		
    }
	
}
