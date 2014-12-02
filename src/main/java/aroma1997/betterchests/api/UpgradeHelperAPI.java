package aroma1997.betterchests.api;

import java.util.List;

import net.minecraft.item.ItemStack;
import aroma1997.betterchests.InventoryFilter;

public final class UpgradeHelperAPI {

	private UpgradeHelperAPI() {}
	
	public static boolean isUpgrade(ItemStack stack) {
		try {
			return (Boolean) getUClass().getMethod("isUpgrade", ItemStack.class).invoke(null, stack);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean isItemAllowed(ItemStack item, List<IInventoryFilter> list) {
		try {
			return (Boolean) getUClass().getMethod("isItemAllowed", ItemStack.class, List.class).invoke(null, item, list);
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
