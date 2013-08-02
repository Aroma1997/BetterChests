package aroma1997.betterchests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;


public class TileEntityBChest extends TileEntityChest {
	
	private int slot = 1;
	private ItemStack[] inventoryContent;
	private int stackLimit = 1;
	private int MAXSLOTS = 45;
	
	public TileEntityBChest() {
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
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        //super.readFromNBT(par1NBTTagCompound);
        this.slot = par1NBTTagCompound.getInteger("slot");
        this.stackLimit = par1NBTTagCompound.getInteger("stackLimit");
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
        this.inventoryContent = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventoryContent.length)
            {
                this.inventoryContent[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }
	
	@Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        //super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("slot", this.slot);
        par1NBTTagCompound.setInteger("stackLimit", this.stackLimit);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventoryContent.length; ++i)
        {
            if (this.inventoryContent[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventoryContent[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
    }
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}
	
}
