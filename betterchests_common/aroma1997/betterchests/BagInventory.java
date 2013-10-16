package aroma1997.betterchests;

import java.util.HashMap;

import aroma1997.core.client.inventories.GUIContainer;
import aroma1997.core.inventories.ContainerBasic;
import aroma1997.core.inventories.ContainerItem;
import aroma1997.core.inventories.ISpecialInventory;
import aroma1997.core.util.FileUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


public class BagInventory implements ISpecialInventory, IUpgradeProvider {
	
	private ItemStack[] items;
	private String customName;
	private HashMap<Upgrade, Integer> upgrades = new HashMap<Upgrade, Integer>();

	@Override
    public ItemStack getStackInSlot(int par1)
    {
		if (par1 >= items.length) return null;
        return this.items[par1];
    }
	
	@Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.items[par1] != null)
        {
            ItemStack itemstack;

            if (this.items[par1].stackSize <= par2)
            {
                itemstack = this.items[par1];
                this.items[par1] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.items[par1].splitStack(par2);

                if (this.items[par1].stackSize == 0)
                {
                    this.items[par1] = null;
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
        if (this.items[par1] != null)
        {
            ItemStack itemstack = this.items[par1];
            this.items[par1] = null;
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
        this.items[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }
	
	@Override
    public String getInvName()
    {
        return this.isInvNameLocalized() ? this.customName : "inv.betterchests:bag.name";
    }
	
	@Override
    public boolean isInvNameLocalized()
    {
        return this.customName != null && this.customName.length() > 0;
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
		return true;
	}
	
	@Override
	public int getSizeInventory() {
		return getAmountUpgrade(Upgrade.SLOT) * 9 + 9;
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
	public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("display"))
        {
            NBTTagCompound nbttagcompound = nbt.getCompoundTag("display");

            if (nbttagcompound.hasKey("Name"))
            {
                customName = nbttagcompound.getString("Name");
            }
        }
		for (Upgrade upgrade : Upgrade.values()) {
			setAmountUpgrade(upgrade, nbt.getInteger(upgrade.toString()));
		}
		this.items = new ItemStack[getSizeInventory()];
		FileUtil.readFromNBT(this, nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		FileUtil.writeToNBT(this, nbt);
		for (Upgrade upgrade : Upgrade.values()) {
			nbt.setInteger(upgrade.toString(), getAmountUpgrade(upgrade));
		}
	}

	@Override
	public ContainerBasic getContainer(EntityPlayer player, int i) {
		return new ContainerItem(player.inventory, this, i);
	}

	@Override
	public int getAmountUpgrade(Upgrade upgrade) {
		return upgrades.get(upgrade);
	}

	@Override
	public boolean isUpgradeInstalled(Upgrade upgrade) {
		return getAmountUpgrade(upgrade) > 0;
	}

	@Override
	public void setAmountUpgrade(Upgrade upgrade, int amount)  {
		if (upgrades.containsKey(upgrade)) {
			upgrades.remove(upgrade);
		}
		upgrades.put(upgrade, new Integer(amount));
	}

	@Override
	public boolean hasEnergy() {
		return isUpgradeInstalled(Upgrade.SOLAR);
	}
	
	public void onUpdate() {
		UpgradeHelper.updateChest(this);
	}
	
}
