/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests.nei;

import aroma1997.betterchests.GUIFilter;
import aroma1997.betterchests.Reference;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIBetterChestsConfig implements IConfigureNEI {

	@Override
	public String getName() {
		return "BetterChests NEI Plugin";
	}

	@Override
	public String getVersion() {
		return Reference.VERSION;
	}

	@Override
	public void loadConfig() {
//		API.registerGuiOverlay(GUIFilter.class, "crafting", new FilterStackPositioner());
		API.registerGuiOverlayHandler(GUIFilter.class, new FilterOverlayHandler(), "crafting");
	}

}
