package aroma1997.betterchests.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import aroma1997.core.container.slot.SlotInventoryPart;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.betterchests.inventories.InventoryPartUpgrades;

public class SlotUpgrade extends SlotInventoryPart {
	public SlotUpgrade(InventoryPartUpgrades part, int index, int xPosition, int yPosition) {
		super(part, index, xPosition, yPosition);
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		assert checkRequirements();

		ItemStack prev = part.getStackInSlot(localIndex);
		part.setInventorySlotContents(localIndex, ItemStack.EMPTY);
		boolean ret = checkRequirements();
		part.setInventorySlotContents(localIndex, prev);
		return ret;
	}

	private boolean checkRequirements() {
		for (ItemStack upgrade : part) {
			if (!((IUpgrade)upgrade.getItem()).areRequirementsMet((IUpgradableBlock) part.getContainer(), upgrade)) {
				return false;
			}
		}
		return true;
	}
}
