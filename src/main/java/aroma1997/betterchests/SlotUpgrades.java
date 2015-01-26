/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.inventories.AromaContainer;
import aroma1997.core.inventories.AromaSlot;
import aroma1997.core.util.InvUtil;
import aroma1997.core.util.WorldUtil;

public class SlotUpgrades extends AromaSlot {

	private IBetterChest chest;

	private ItemStack item;

	private ContainerUpgrades container;

	public SlotUpgrades(IBetterChest chest, ItemStack item, int posX, int posY) {
		super(null, -1, posX, posY);
		this.chest = chest;
		this.item = item;
	}

	@Override
	public void onSlotChange(ItemStack par1ItemStack, ItemStack par2ItemStack) {

	}

	@Override
	protected void onCrafting(ItemStack par1ItemStack, int par2) {
	}

	@Override
	protected void onCrafting(ItemStack par1ItemStack) {
	}

	@Override
	public void onPickupFromSlot(EntityPlayer par1EntityPlayer,
			ItemStack par2ItemStack) {
		onSlotChanged();
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}

	@Override
	public ItemStack getStack() {
		ItemStack stack = item.copy();
		stack.stackSize = chest.getAmountUpgradeExact(item);
		if (stack.stackSize <= 0) {
			return null;
		}
		return stack;
	}

	@Override
	public void putStack(ItemStack par1ItemStack) {

	}

	@Override
	public void onSlotChanged() {
		chest.markDirty();
	}

	@Override
	public int getSlotStackLimit() {
		return 64;
	}

	@Override
	public ItemStack decrStackSize(int par1) {
		int upgrades = chest.getAmountUpgrade(item);
		ItemStack ret = item.copy();
		ret.stackSize = Math.min(upgrades, par1);
		chest.setAmountUpgrade(item, chest.getAmountUpgradeExact(item)
				- ret.stackSize);
		onSlotChanged();
		// item.stackSize -= ret.stackSize;
		if (ret.stackSize <= 0) {
			return null;
		}
		return ret;
	}

	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
		return false;
	}

	@Override
	public ItemStack slotClicked(AromaContainer container, int slot, int par2,
			int par3, EntityPlayer player) {

		if (par3 == 1) {
			if (par2 == 1) {
				ItemStack stack = getStack().copy();
				decrStackSize(1);
				stack.stackSize = 1;
				UpgradeHelper.enableUpgrade(stack);
				if (InvUtil.putIntoFirstSlot(
						((ContainerUpgrades) container).player.inventory,
						stack, false) != null) {
					WorldUtil.dropItemsRandom(
							((ContainerUpgrades) container).player.worldObj,
							stack, ((ContainerUpgrades) container).player
									.getPosition());
				}
			} else if (par2 == 0) {
				ItemStack stack = getStack().copy();
				decrStackSize(getStack().stackSize);
				UpgradeHelper.enableUpgrade(stack);
				stack = InvUtil.putIntoFirstSlot(
						((ContainerUpgrades) container).player.inventory,
						stack, false);
				if (stack != null) {
					WorldUtil.dropItemsRandom(
							((ContainerUpgrades) container).player.worldObj,
							stack, ((ContainerUpgrades) container).player
									.getPosition());
				}

			}

		} else if (par3 == 0 && (par2 == 0 || par2 == 1)) {
			chest.setUpgradeDisabled(getStack(),
					!chest.isUpgradeDisabled(getStack()));
		}

		return null;
	}

	/**
	 * Retrieves the index in the inventory for this slot, this value should
	 * typically not be used, but can be useful for some occasions.
	 * 
	 * @return Index in associated inventory for this slot.
	 */
	@Override
	public int getSlotIndex() {
		return -1;
	}

}
