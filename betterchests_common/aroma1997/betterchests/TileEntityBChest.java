package aroma1997.betterchests;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;


public class TileEntityBChest extends TileEntityChest {
	
	private int slot;
	private ItemStack[] inventoryContent;
	private int stackLimit;
	private int pages;
	private int MAXSLOTS = 1;
	
	public TileEntityBChest() {
		slot = 1;
		stackLimit = 1;
		pages = 1;
		inventoryContent = new ItemStack[MAXSLOTS];
	}

	@Override
	public int getSizeInventory() {
		return slot;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (i >= MAXSLOTS || i < 0) return null;
		return inventoryContent[i];
	}
	
	@Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (this.inventoryContent[i] != null)
        {
            ItemStack itemstack = this.inventoryContent[i];
            this.inventoryContent[i] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }
	
	@Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (this.inventoryContent[i] != null)
        {
            ItemStack itemstack;

            if (this.inventoryContent[i].stackSize <= j)
            {
                itemstack = this.inventoryContent[i];
                this.inventoryContent[i] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.inventoryContent[i].splitStack(j);

                if (this.inventoryContent[i].stackSize == 0)
                {
                    this.inventoryContent[i] = null;
                }

                this.onInventoryChanged();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }
	
	@Override
    public void setInventorySlotContents(int i, ItemStack itemStack)
    {
        this.inventoryContent[i] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit())
        {
        	itemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }
	
	@Override
    public String getInvName()
    {
		return "Adjustable Chest";
    }
	
	@Override
    public boolean isInvNameLocalized()
    {
        return false;
    }
	
	@Override
    public int getInventoryStackLimit()
    {
        return stackLimit;
    }
	
	
}
