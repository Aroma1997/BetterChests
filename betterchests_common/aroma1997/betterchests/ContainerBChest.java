package aroma1997.betterchests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;


public class ContainerBChest extends Container {
	
	private TileEntityBChest tileEntity;
	
	public ContainerBChest(IInventory inventory, TileEntityBChest tileEntity) {
		this.tileEntity = tileEntity;
		layoutContainer(inventory, tileEntity);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	private void layoutContainer(IInventory playerInventory, IInventory inventory) {
		for (int inventoryRow = 0; inventoryRow < 3; inventoryRow++) {
			for (int inventoryColumn = 0; inventoryColumn < 9; inventoryColumn++) {
				addSlotToContainer(new Slot(playerInventory, inventoryColumn
					+ inventoryRow * 9 + 9, 8 + inventoryColumn * 18,
					84 + inventoryRow * 18));
			}
		}
		
		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			addSlotToContainer(new Slot(playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 142));
		}
		for (int chestRow = 0; chestRow < inventory.getSizeInventory() / 9; chestRow++) {
			for (int chestColumn = 0; chestColumn < getColumnAmount(chestRow, chestColumn, inventory.getSizeInventory()); chestColumn++) {
				addSlotToContainer(new Slot(inventory, 36, 80, 26));
			}
		}
	}
	
	private int getColumnAmount(int row, int column, int totalSlots) {
		if (totalSlots / 9 >= row) {
			return 9;
		}
		return totalSlots % 9;
	}
	
}
