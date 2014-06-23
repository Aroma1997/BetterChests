/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.client;

import net.minecraftforge.client.MinecraftForgeClient;
import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.CommonProxy;
import aroma1997.betterchests.TileEntityBChest;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBChest.class, // "betterChest",
		        new BChestRenderer());
		
		MinecraftForgeClient.registerItemRenderer(BetterChests.bag, new BagRenderer());
		
		ClientRegistry.registerKeyBinding(new BetterChestsKeyBinding());
		new EventListenerClient();
	}
	
}
