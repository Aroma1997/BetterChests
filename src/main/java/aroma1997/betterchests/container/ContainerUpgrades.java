package aroma1997.betterchests.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import aroma1997.core.container.ContainerBase;
import aroma1997.core.container.slot.SlotInventoryPart;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.betterchests.inventories.IUpgradableBlockInternal;

public class ContainerUpgrades extends ContainerBase<IUpgradableBlockInternal> {

	public static final int startUpgradeInvX = 9;
	public static final int startUpgradeInvY = 21;
	public static final int startFilterInvX = 9;
	public static final int startFilterInvY = startUpgradeInvY + 36 + 20;
	public static final int startInvX = 9;
	public static final int startInvY = startFilterInvY + 18 + 20;

	public ContainerUpgrades(IUpgradableBlockInternal inventory, EntityPlayer player) {
		super(inventory, player);

		int amountSlotsPerRow = 8;

		for (int i = 0; i < inventory.getUpgradePart().getSizeInventory(); i++) {
			addSlotToContainer(new SlotUpgrade(inventory.getUpgradePart(), i,
					startUpgradeInvX + i % amountSlotsPerRow * 18,
					startUpgradeInvY + i / amountSlotsPerRow * 18));
		}

		for (int i = 0; i < inventory.getFilterPart().getSizeInventory(); i++) {
			addSlotToContainer(new SlotInventoryPart(inventory.getFilterPart(), i,
					startFilterInvX + i % amountSlotsPerRow * 18,
					startFilterInvY + i / amountSlotsPerRow * 18));
		}

		layoutPlayerInventory(startInvX, startInvY, player);
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		if (clickTypeIn == ClickType.CLONE) {
			Slot slot = getSlot(slotId);
			if (slot.inventory == inventory) {
				ItemStack stack = slot.getStack();
				if (slot.inventory == inventory && stack.getItem() instanceof IUpgrade && ((IUpgrade) stack.getItem()).canBeDisabled(stack)) {
					inventory.getUpgradePart().setUpgradeDisabled(slot.getStack(), !inventory.getUpgradePart().isUpgradeDisabled(slot.getStack()));
				}
				return ItemStack.EMPTY;
			}
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
}
