/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.client;

import net.minecraft.client.settings.KeyBinding;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class BetterChestsKeyBinding extends KeyBinding {
	
	private static BetterChestsKeyBinding instance;
	
	public BetterChestsKeyBinding() {
		super("betterchests:key.openBag", Keyboard.KEY_ADD, "key.categories.inventory");
		BetterChestsKeyBinding.instance = this;
	}
	
	public static BetterChestsKeyBinding getInstance() {
		return instance;
	}
	
}
