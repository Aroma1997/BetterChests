package aroma1997.betterchests.inventory;

import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.ItemUpgrade;
import aroma1997.betterchests.Upgrade;
import aroma1997.core.client.inventories.GUIContainer;
import aroma1997.core.inventories.ContainerBasic;
import aroma1997.core.inventories.ContainerItem;
import aroma1997.core.inventories.ISpecialInventory;
import aroma1997.core.util.FileUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


public class SubChest implements ISpecialInventory {
	
	private Type type;
	private IInventory inv;
	
	public SubChest(Type type, IInventory inv) {
		this.type = type;
		this.inv = inv;
	}
	
	private ItemStack[] chestContents;

	@Override
    public ItemStack getStackInSlot(int par1)
    {
		try {
			return this.chestContents[par1];
		}
		catch(Exception e) {
			return null;
		}
    }
	
	@Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.chestContents[par1] != null)
        {
            ItemStack itemstack;

            if (this.chestContents[par1].stackSize <= par2)
            {
                itemstack = this.chestContents[par1];
                this.chestContents[par1] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.chestContents[par1].splitStack(par2);

                if (this.chestContents[par1].stackSize == 0)
                {
                    this.chestContents[par1] = null;
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
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.chestContents[par1] != null)
        {
            ItemStack itemstack = this.chestContents[par1];
            this.chestContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }
	
	@Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.chestContents[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }
	
	@Override
    public String getInvName()
    {
        return "Upgrade Contents";
    }
	
	@Override
    public boolean isInvNameLocalized()
    {
        return false;
    }
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public void onInventoryChanged() {
		
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public void openChest() {
		
	}
	
	@Override
	public void closeChest() {
		
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return itemstack.getItem() instanceof ItemUpgrade;
	}
	
	@Override
	public int getSizeInventory() {
		return Upgrade.values().length - 1;
	}
	
	@Override
	public Slot getSlot(int slot, int index, int x, int y) {
		return new Slot(this, index, x, y);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(GUIContainer gui, ContainerBasic container,
		int par1, int par2) {
		
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(GUIContainer gui, ContainerBasic container,
		float f, int i, int j) {
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		FileUtil.writeToNBT(this, nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.chestContents = new ItemStack[this.getSizeInventory()];
		FileUtil.readFromNBT(this, nbt);
		
	}

	@Override
	public ContainerBasic getContainer(EntityPlayer player, int i) {
		if (type == Type.BAG) {
			return new ContainerItem(player.inventory, this, i);
		}
		return new ContainerBasic(player.inventory, this);
	}
	
	public int getAmountUpgrade(Upgrade upgrade) {
		for (int i = 0; i < this.getSizeInventory(); i++) {
			ItemStack item = this.getStackInSlot(i);
			if (item != null && item.getItem() instanceof ItemUpgrade && upgrade.ordinal() == item.getItemDamage()) {
				return item.stackSize;
			}
		}
		return 0;
	}
	
	public boolean isUpgradeInstalled(Upgrade upgrade) {
		return getAmountUpgrade(upgrade) > 0;
	}

	public void setAmountUpgrade(Upgrade upgrade, int i) {
		for (int j = 0; i < this.getSizeInventory(); j++) {
			ItemStack item = this.getStackInSlot(j);
			if (item != null && item.getItem() instanceof ItemUpgrade && item.getItemDamage() == upgrade.ordinal()) {
				item.stackSize = i;
				return;
			}
		}
		for (int j = 0; j <  this.getSizeInventory(); j++) {
			ItemStack item = this.getStackInSlot(j);
			if (item == null) {
				this.setInventorySlotContents(j, new ItemStack(BetterChests.upgrade, i, upgrade.ordinal()));
				return;
			}
		}
	}
	
	public void update() {
		
	}
	
	public void validate() {
		this.chestContents = new ItemStack[this.getSizeInventory()];
	}

	@Override
	public IInventory getTopLevelInv() {
		return inv;
	}
	
}
