package aroma1997.betterchests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;


public class CommonProxy implements IGuiHandler {

	public void registerRenderer() {
		
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer thePlayer, World world, int x, int y,
		int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityBChest) {
			TileEntityBChest tileEntityBC = (TileEntityBChest) tileEntity;
			return new ContainerBChest(thePlayer.inventory,
				tileEntityBC);
		}
		return null;
	}

	@Override
	public Object
		getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
	
}
