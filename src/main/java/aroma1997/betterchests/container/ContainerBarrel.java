package aroma1997.betterchests.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import aroma1997.core.container.ContainerBase;
import aroma1997.core.container.slot.SlotInventoryPart;
import aroma1997.core.inventory.InvUtil;
import aroma1997.core.inventory.inventorypart.InventoryPartBase;
import aroma1997.core.util.ItemUtil;
import aroma1997.betterchests.inventories.IBetterBarrelInternal;
import aroma1997.betterchests.inventories.InventoryPartBarrel;

public class ContainerBarrel extends ContainerBase<IBetterBarrelInternal> {

	public ContainerBarrel(IBetterBarrelInternal inventory, EntityPlayer player) {
		super(inventory, player);
		addSlotToContainer(new SlotDrain(inventory.getChestPart(), 0, 72, 21));
		addSlotToContainer(new SlotFill(inventory.getChestPart(), 1, 90, 21));

		layoutPlayerInventory(9, 58, player);
	}

	private class SlotDrain extends SlotInventoryPart {

		public SlotDrain(InventoryPartBase part, int index, int xPosition, int yPosition) {
			super(part, index, xPosition, yPosition);
		}

		public InventoryPartBarrel getBarrel() {
			return (InventoryPartBarrel) part;
		}

		@Override
		public ItemStack decrStackSize(int amount) {
			ItemStack ret = getBarrel().getDummy().copy();
			amount = Math.min(amount, ret.getMaxStackSize());
			int gotten = 0;
			for (int i : getBarrel().getSlotsForFace(null)) {
				if (ItemUtil.areItemsSameMatchingIdDamageNbt(ret, getBarrel().getStackInSlot(i))) {
					gotten += getBarrel().decrStackSize(i, amount - gotten).getCount();
					if (gotten >= amount) {
						break;
					}
				}
			}
			ret.setCount(gotten);
			onSlotChanged();
			return ret;
		}

		@Override
		public int getSlotStackLimit() {
			return 64;
		}

		@Override
		public ItemStack getStack() {
			ItemStack ret = getBarrel().getDummy().copy();
			ret.setCount(Math.min(ret.getMaxStackSize(), getBarrel().getTotalAmountOfItems()));
			return ret;
		}

		@Override
		public boolean isHere(IInventory inv, int slot) {
			return inv == inventory && slot == slotNumber;
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}

		@Override
		public void putStack(ItemStack stack) {
			decrStackSize(getStack().getCount() - stack.getCount());
		}

		@Override
		public void onSlotChanged() {
			part.markDirty();
		}
	}

	private class SlotFill extends SlotInventoryPart {

		public SlotFill(InventoryPartBase part, int index, int xPosition, int yPosition) {
			super(part, index, xPosition, yPosition);
		}

		public InventoryPartBarrel getBarrel() {
			return (InventoryPartBarrel) part;
		}

		@Override
		public ItemStack decrStackSize(int amount) {
			return ItemStack.EMPTY;
		}

		@Override
		public int getSlotStackLimit() {
			if (getBarrel().isItemSet()) {
				return getBarrel().getActualSize() * getBarrel().getDummy().getMaxStackSize() - getBarrel().getTotalAmountOfItems();
			} else {
				return 64;
			}
		}

		@Override
		public ItemStack getStack() {
			return ItemStack.EMPTY;
		}

		@Override
		public boolean isHere(IInventory inv, int slot) {
			return inv == inventory && slot == slotNumber;
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return getBarrel().isItemValidForSlot(0, stack);
		}

		@Override
		public void putStack(ItemStack stack) {
			InvUtil.putStackInInventoryInternal(stack, getBarrel(), false);
			onSlotChanged();
		}

		@Override
		public void onSlotChanged() {
			part.markDirty();
		}

	}
}
