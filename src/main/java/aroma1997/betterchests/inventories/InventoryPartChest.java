package aroma1997.betterchests.inventories;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import aroma1997.core.inventory.inventorypart.InventoryPartBase;
import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.ChestModifier;

public class InventoryPartChest extends InventoryPartBase {

	private static final int MAX_SIZE = 90;
	private static final int DEFAULT_SIZE = 27;

	public InventoryPartChest(IUpgradableBlockInternal container, int size) {
		super(container, "inventory", size);
	}

	public InventoryPartChest(IUpgradableBlockInternal container) {
		this(container, MAX_SIZE);
	}

	protected IUpgradableBlockInternal getChest() {
		return (IUpgradableBlockInternal) container;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
		return super.canInsertItem(index, stack, direction) && index < getActualSize() && index >= getAccessibleBegin();
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return super.canExtractItem(index, stack, direction) && index < getActualSize() && index >= getAccessibleBegin();
	}

	public int getActualSize() {
		int size = DEFAULT_SIZE + UpgradeHelper.INSTANCE.intSum(getChest(), ChestModifier.SIZE);
		return Math.min(size, MAX_SIZE);
	}

	public int getAccessibleBegin() {
		int size = UpgradeHelper.INSTANCE.intSum(getChest(), ChestModifier.SIZE_BEGIN);
		return Math.max(size, 0);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		int[] ret = new int[getActualSize()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = i;
		}
		return ret;
	}
}
