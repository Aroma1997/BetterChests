package aroma1997.betterchests;

import net.minecraft.block.BlockChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


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
		return null;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityBChest();
	}
	
}
