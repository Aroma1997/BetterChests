/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.api;

import java.util.List;

import net.minecraft.item.ItemStack;

public final class UpgradeHelperAPI {

	private UpgradeHelperAPI() {
	}

	public static boolean isUpgrade(ItemStack stack) {
		try {
			return (Boolean) getUClass()
					.getMethod("isUpgrade", ItemStack.class)
					.invoke(null, stack);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isItemAllowed(ItemStack item,
			List<IInventoryFilter> list) {
		try {
			return (Boolean) getUClass().getMethod("isItemAllowed",
					ItemStack.class, List.class).invoke(null, item, list);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Class<?> getUClass() {
		try {
			return Class.forName("aroma1997.betterchests.UpgradeHelper");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
