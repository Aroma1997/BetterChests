
package aroma1997.betterchests.client;


import aroma1997.betterchests.CommonProxy;
import aroma1997.betterchests.TileEntityBChest;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRenderers() {
		ClientRegistry.registerTileEntity(TileEntityBChest.class, "betterChest",
			new BChestRenderer());
	}
	
}
