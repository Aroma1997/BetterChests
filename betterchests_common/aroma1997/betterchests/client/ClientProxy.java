/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.client;


import aroma1997.betterchests.BetterChestsKeyHandler;
import aroma1997.betterchests.CommonProxy;
import aroma1997.betterchests.TileEntityBChest;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRenderers() {
		ClientRegistry.registerTileEntity(TileEntityBChest.class, "betterChest",
			new BChestRenderer());

			KeyBindingRegistry.registerKeyBinding(new BetterChestsKeyHandler());
	}
	
}
