package aroma1997.betterchests.inventory;

import java.util.Random;

import aroma1997.betterchests.Reference;
import aroma1997.betterchests.Upgrade;
import aroma1997.core.client.inventories.GUIContainer;
import aroma1997.core.client.util.Colors;
import aroma1997.core.inventories.ContainerBasic;
import aroma1997.core.inventories.ContainerItem;
import aroma1997.core.inventories.ISpecialInventory;
import aroma1997.core.util.FileUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;


public class MainChest implements ISpecialInventory {
	
	private Chest chest;
	
	private IInventory inv;
	
	public MainChest(Type type, Chest chest, IInventory inv) {
		this.type = type;
		this.chest = chest;
		this.inv = inv;
	}
	
	private Type type;
	
	private ItemStack[] items;

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot >= getSizeInventory() || items == null) {
			return null;
		}
		return items[slot];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		items[i] = itemstack;
	}

	@Override
	public String getInvName() {
		if (chest.getSub().isUpgradeInstalled(Upgrade.VOID)) {
			return Colors.RED + "Void Chest";
		}
		return "Adjustable Chest";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	private boolean c = false;

	@Override
	public void onInventoryChanged() {
		if(c) return;
		c = true;
		this.getTopLevelInv().onInventoryChanged();
		c = false;
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
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		FileUtil.writeToNBT(this, nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.items = new ItemStack[this.getSizeInventory()];
		FileUtil.readFromNBT(this, nbt);
	}

	@Override
	public int getSizeInventory() {
		return chest.getSub().getAmountUpgrade(Upgrade.SLOT) * 9 + 9;
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
	public ContainerBasic getContainer(EntityPlayer player, int i) {
		if (this.type == Type.BAG) {
			return new ContainerItem(player.inventory, this, i);
		}
		return new ContainerBasic(player.inventory, this);
	}
	
	public void update() {

	}
	
	private boolean hasEnergy() {
		return chest.getSub().isUpgradeInstalled(Upgrade.SOLAR);
	}

	public void validate() {
		this.items = new ItemStack[this.getSizeInventory()];
	}

	@Override
	public IInventory getTopLevelInv() {
		return inv;
	}
	
	
	
}
