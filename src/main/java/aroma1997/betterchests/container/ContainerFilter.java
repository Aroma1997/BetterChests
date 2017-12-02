package aroma1997.betterchests.container;

import net.minecraft.entity.player.EntityPlayer;

import aroma1997.core.container.ContainerBase;
import aroma1997.core.container.slot.SlotGhost;
import aroma1997.betterchests.filter.InventoryFilter;

public class ContainerFilter extends ContainerBase<InventoryFilter> {
	public ContainerFilter(InventoryFilter inventory, EntityPlayer player) {
		super(inventory, player);

		for (int i = 0; i < inventory.filterInv.getSizeInventory(); i++) {
			addSlotToContainer(new SlotGhost(inventory.filterInv, i, 27 + i % 3 * 18, 21 + i / 3 * 18));
		}

		addSlotToContainer(new SlotGhost(inventory.upgradeInv, 0, 135, 39));
		layoutPlayerInventory(9, 96, player);
	}
}
