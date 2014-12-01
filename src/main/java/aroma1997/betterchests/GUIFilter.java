/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import aroma1997.core.client.inventories.GUIContainer;

/**
 * This is only here for NEI Plugin stuff.
 */
@SideOnly(Side.CLIENT)
public class GUIFilter extends GUIContainer {

	public GUIFilter(ContainerFilter container) {
		super(container);
	}

}
