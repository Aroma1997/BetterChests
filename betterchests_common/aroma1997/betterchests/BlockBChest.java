package aroma1997.betterchests;

import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class BlockBChest extends BlockChest {

	public BlockBChest(int id) {
		super(id, 0);
		setUnlocalizedName("betterChest");
		setHardness(4.0F);
		setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public int damageDropped(int meta) {
		return 0;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBChest();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + "BChest");
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer thePlayer,
		int s, float f1, float f2, float f3) {
		if (thePlayer.isSneaking()) {
			return false;
		}
		
		if (world.isRemote) {
			return true;
		}
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityBChest) {
			thePlayer.openGui(BetterChests.instance, 1, world, x, y, z);
		}
		return true;
	}
	
}
