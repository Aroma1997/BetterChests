package aroma1997.betterchests;

import aroma1997.betterchests.api.IBetterChest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class SlotUpgrades extends Slot {
	
	private IBetterChest chest;
	
	private ItemStack item;
	
	public SlotUpgrades(IBetterChest chest, ItemStack item, int posX, int posY) {
		super(null, -1, posX, posY);
		this.chest = chest;
		this.item = item;
	}
	
    public void onSlotChange(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
    	
    }

    protected void onCrafting(ItemStack par1ItemStack, int par2) {}

    protected void onCrafting(ItemStack par1ItemStack) {}

    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
    {
        this.onSlotChanged();
    }
    
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return false;
    }
    
    public ItemStack getStack()
    {
        ItemStack stack = item.copy();
        stack.stackSize = chest.getAmountUpgrade(item);
        if (stack.stackSize <= 0) return null;
        return stack;
    }

    public void putStack(ItemStack par1ItemStack)
    {
    	
    }
    
    public void onSlotChanged()
    {
        this.chest.onInventoryChanged();
    }
    public int getSlotStackLimit()
    {
        return 64;
    }
    
    public ItemStack decrStackSize(int par1)
    {
    	int upgrades = chest.getAmountUpgrade(item);
    	ItemStack ret = item.copy();
    	ret.stackSize = Math.min(upgrades, par1);
        chest.setAmountUpgrade(item, chest.getAmountUpgrade(item) - ret.stackSize);
        item.stackSize -= ret.stackSize;
        if (ret.stackSize <= 0) return null;
        return ret;
    }
    
    public boolean isSlotInInventory(IInventory par1IInventory, int par2)
    {
        return false;
    }
    
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_111238_b()
    {
        return true;
    }

    /**
     * Retrieves the index in the inventory for this slot, this value should typically not
     * be used, but can be useful for some occasions.
     *
     * @return Index in associated inventory for this slot.
     */
    public int getSlotIndex()
    {
        return -1;
    }
	
}
