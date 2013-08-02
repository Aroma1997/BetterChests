package aroma1997.betterchests.client;

import aroma1997.betterchests.CommonProxy;
import aroma1997.betterchests.TileEntityBChest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.client.registry.ClientRegistry;


public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRenderer() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBChest.class, new BChestRenderer());
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer thePlayer, World world, int X, int Y,
		int Z) {
		TileEntity tileEntity = world.getBlockTileEntity(X, Y, Z);
		if (tileEntity != null && tileEntity instanceof TileEntityBChest) {
			TileEntityBChest tileEntityBC = (TileEntityBChest) tileEntity;
//			return GUIChest.GUI.makeGUI(tileEntityBC.getType(), thePlayer.inventory,
//				tileEntityBC);
		}
		return null;
	}
	
}
