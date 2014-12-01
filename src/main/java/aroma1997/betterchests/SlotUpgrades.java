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
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import aroma1997.betterchests.api.IBetterChest;

public class SlotUpgrades extends Slot {

	private IBetterChest chest;

	private ItemStack item;

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
