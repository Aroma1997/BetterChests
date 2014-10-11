package aroma1997.betterchests;

import java.util.List;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import aroma1997.core.inventories.SlotGhost;
import aroma1997.core.items.inventory.InventoryItem;
import aroma1997.core.util.ItemUtil;
import aroma1997.core.util.ItemUtil.ItemMatchCriteria;

public class InventoryFilter extends InventoryItem {

	public InventoryFilter(ItemStack item) {
		super(item);
	}

	@Override
	public int getSizeInventory() {
		return 9;
	}
	
	@Override
	public Slot getSlot(int slot, int index, int x, int y) {
		return new SlotGhost(this, index, x, y);
	}

	@Override
	public String getInventoryName() {
		return item.getUnlocalizedName() + ".name";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}
	
	public boolean matchesUpgrade(ItemStack upgrade) {
		return true;
	}
	
	public boolean isWhitelist() {
		return item.getItemDamage() == 0;
	}
	
	public boolean isBlacklist() {
		return !isWhitelist();
	}
	
	public static boolean isItemAllowed(ItemStack item, List<InventoryFilter> list) {
		if (list == null || list.isEmpty()) {
			return true;
		}
		boolean hasWhitelist = false;
		boolean isOnWhitelist = false;
		
		for (InventoryFilter filter : list) {
			if (filter.isWhitelist()) {
				hasWhitelist = true;
			}
			for (int i = 0; i < filter.getSizeInventory(); i++) {
				if (ItemUtil.areItemsSameMatching(item, filter.getStackInSlot(i), ItemMatchCriteria.ID, ItemMatchCriteria.DAMAGE)) {
					if (filter.isWhitelist()) {
						isOnWhitelist = true;
					}
					else {
						return false;
					}
				}
			}
		}
		if (hasWhitelist) {
			return isOnWhitelist;
		}
		return true;
		
	}

}
