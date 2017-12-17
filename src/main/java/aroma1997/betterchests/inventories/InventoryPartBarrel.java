package aroma1997.betterchests.inventories;

import java.util.BitSet;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.fml.common.FMLCommonHandler;

import aroma1997.core.block.te.TileEntityBase;
import aroma1997.core.network.AutoEncode;
import aroma1997.core.util.ItemUtil;
import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.ChestModifier;

public class InventoryPartBarrel extends InventoryPartChest {

	private static final int MAX_SIZE = 256;
	private static final int DEFAULT_SIZE = 32;
	/**
	 * Has bits set at the used slots.
	 * Is only set accurately on the serverside.
	 */
	private BitSet usedSlots = new BitSet(MAX_SIZE);
	@AutoEncode
	private ItemStack dummy = ItemStack.EMPTY;

	public InventoryPartBarrel(IUpgradableBlockInternal container) {
		super(container, MAX_SIZE);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return !isItemSet() || dummy.isEmpty() || ItemUtil.areItemsSameMatchingIdDamageNbt(dummy, stack);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
		return isItemValidForSlot(index, stack) && (!isItemSet() || ItemUtil.areItemsSameMatchingIdDamageNbt(dummy, stack));
	}

	@Override
	public int getSizeInventory() {
		return MAX_SIZE;
	}

	@Override
	public int getActualSize() {
		int size = DEFAULT_SIZE + UpgradeHelper.INSTANCE.intSum(getChest(), ChestModifier.SIZE);
		return Math.min(size, MAX_SIZE);
	}

	@Override
	public int getAccessibleBegin() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return FMLCommonHandler.instance().getEffectiveSide().isServer() ? usedSlots.isEmpty() : super.isEmpty();
	}

	@Override
	public void clear() {
		super.clear();
		usedSlots.clear();
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		try {
			return super.decrStackSize(index, count);
		} finally {
			update(index);
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		try {
			return super.removeStackFromSlot(index);
		} finally {
			update(index);
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		super.setInventorySlotContents(index, stack);
		if (dummy.isEmpty() || isEmpty()) {
			dummy = stack.copy();
		}
		update(index);
	}

	protected void update(int slot) {
		usedSlots.set(slot, !getStackInSlot(slot).isEmpty());
	}

	public ItemStack getDummy() {
		return dummy;
	}

	public boolean isItemSet() {
		return !isEmpty() || !canChangeItem();
	}

	private boolean canChangeItem() {
		return !UpgradeHelper.INSTANCE.booleanSum(getChest(), ChestModifier.ACCEPTANCE_LOCK, false);
	}

	public int getTotalAmountOfItems() {
		int amount = 0;
		for (ItemStack stack : this) {
			amount += stack.getCount();
		}
		return amount;
	}

	@Override
	public void markDirtyInternal() {
		super.markDirtyInternal();
		if (getChest() instanceof TileEntityBase) {
			((TileEntityBase)getChest()).sendUpdates();
		}
		if (isEmpty() && canChangeItem()) {
			dummy = ItemStack.EMPTY;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		dummy = new ItemStack(nbt.getCompoundTag("dummy"));
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);
		nbt.setTag("dummy", dummy.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	public String getAmountDescr() {
		ItemStack stack = getDummy();
		int stacksize = stack.getMaxStackSize();
		int totalAmount = getTotalAmountOfItems();
		String text;
		if (totalAmount >= stacksize && stacksize > 1) {
			text = (totalAmount / stacksize) + "x" + stacksize + "+" + (totalAmount % stacksize);
		} else {
			text = "" + totalAmount;
		}
		return text;
	}
}
