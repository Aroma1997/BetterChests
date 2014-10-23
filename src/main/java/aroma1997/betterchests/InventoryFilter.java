/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import aroma1997.core.client.inventories.GUIContainer;
import aroma1997.core.inventories.AromaContainer;
import aroma1997.core.inventories.ContainerBasic;
import aroma1997.core.inventories.ContainerItem;
import aroma1997.core.inventories.SlotGhost;
import aroma1997.core.items.inventory.InventoryItem;
import aroma1997.core.util.InvUtil.IFilter;
import aroma1997.core.util.ItemUtil;
import aroma1997.core.util.ItemUtil.ItemMatchCriteria;

public class InventoryFilter extends InventoryItem {
	
	public static final int SLOT_UPGRADE = 9;

	public InventoryFilter(ItemStack item) {
		super(item);
	}

	@Override
	public int getSizeInventory() {
		return 10;
	}
	
	@Override
	public Slot getSlot(int slot, int index, int x, int y) {
		if (slot != SLOT_UPGRADE) {
			return new SlotGhost(this, index, x - 36, y);
		}
		return new SlotFilterUpgrade(this, index, 129, 30);
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
		if (!UpgradeHelper.isUpgrade(upgrade)) {
			return false;
		}
		if (getStackInSlot(SLOT_UPGRADE) == null) {
			return true;
		}
		return ItemUtil.areItemsSameMatching(upgrade, getStackInSlot(SLOT_UPGRADE), ItemMatchCriteria.ID, ItemMatchCriteria.DAMAGE);
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
				if (i == SLOT_UPGRADE) continue;
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
	
	@Override
	public AromaContainer getContainer(EntityPlayer player, int i) {
		return new ContainerFilter(player.inventory, this, i);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(GUIContainer gui,
			ContainerBasic container, int par1, int par2) {
		gui.getFontRender().drawString(StatCollector.translateToLocal("gui.betterchests:filter.upgrade"), 120, 18, 0x404040);
	}
	
	public static class BCFilterFilter implements IFilter {
		
		private List<InventoryFilter> list;
		
		public BCFilterFilter(List<InventoryFilter> list) {
			this.list = list;
		}

		@Override
		public boolean isOk(ItemStack items) {
			return isItemAllowed(items, list);
		}
		
	}

}
