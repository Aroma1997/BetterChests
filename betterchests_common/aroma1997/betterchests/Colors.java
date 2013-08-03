
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
