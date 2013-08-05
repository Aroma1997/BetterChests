/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum Colors {
	BLACK,
	BLUE,
	GREEN,
	CYAN,
	RED,
	PURPLE,
	ORANGE,
	LIGHTGRAY,
	GRAY,
	LIGHTBLUE,
	LIME,
	TURQUISE,
	PINK,
	MAGENTA,
	YELLOW,
	WHITE;
	
	@Override
	public String toString() {
		return "\u00a7" + Integer.toHexString(ordinal());
	}
	
}
