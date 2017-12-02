package aroma1997.betterchests.container;

import net.minecraft.entity.player.EntityPlayer;

import aroma1997.core.container.ContainerBase;
import aroma1997.core.container.slot.SlotInventoryPart;
import aroma1997.betterchests.inventories.IBetterChestInternal;

public class ContainerBChest extends ContainerBase<IBetterChestInternal> {

	public final int width;
	public final int height;
	public final int startChestInvX, startChestInvY;
	public final int startInvX, startInvY;

	public ContainerBChest(IBetterChestInternal inventory, EntityPlayer player) {
		super(inventory, player);

		startChestInvX = 9;
		startChestInvY = 21;

		int amountSlots = inventory.getChestPart().getActualSize();
		int width = ContainerHelper.getWidthForSlots(amountSlots);
		for (int i = 0; i < amountSlots; i++) {
			addSlotToContainer(new SlotInventoryPart(inventory.getChestPart(), i, startChestInvX + 18 * (i % width), startChestInvY + 18 * (i / width)));
		}
		int height = amountSlots / width;
		if (amountSlots % width != 0) {
			height++;
		}
		this.width = width * 18 + 18;

		startInvX = this.width / 2 - 9 * 9;
		startInvY = 21 + 17 + height * 18;

		layoutPlayerInventory(startInvX, startInvY, player);

		this.height = startChestInvY * 2 + height * 18 + 76 + 5;
	}
}
