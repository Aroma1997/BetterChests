/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;


import java.util.HashMap;
import java.util.Random;

import aroma1997.core.client.inventories.GUIContainer;
import aroma1997.core.inventories.ContainerBasic;
import aroma1997.core.inventories.ContainerItem;
import aroma1997.core.inventories.IAdvancedInventory;
import aroma1997.core.util.FileUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class BagInventory implements IBetterChest, IAdvancedInventory {
	
	private final ItemStack item;
	
	public BagInventory(ItemStack item) {
		this.item = item;
		if (item.getTagCompound() == null) {
			item.setTagCompound(new NBTTagCompound());
		}
		readFromNBT(item.stackTagCompound);
	}
	
	private ItemStack[] items;
	
	private String customName;
	
	private HashMap<Upgrade, Integer> upgrades = new HashMap<Upgrade, Integer>();
	
	@Override
	public ItemStack getStackInSlot(int par1)
	{
		if (par1 >= items.length) {
			return null;
		}
		return items[par1];
	}
	
	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (items[par1] != null)
		{
			ItemStack itemstack;
			
			if (items[par1].stackSize <= par2)
			{
				itemstack = items[par1];
				items[par1] = null;
				onInventoryChanged();
				return itemstack;
			}
			else
			{
				itemstack = items[par1].splitStack(par2);
				
				if (items[par1].stackSize == 0)
				{
					items[par1] = null;
				}
				
				onInventoryChanged();
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
		if (items[par1] != null)
		{
			ItemStack itemstack = items[par1];
			items[par1] = null;
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
		setStackInSlotWithoutNotify(par1, par2ItemStack);
		
		onInventoryChanged();
	}
	
	@Override
	public String getInvName()
	{
		return isInvNameLocalized() ? customName : "inv.betterchests:bag.name";
	}
	
	@Override
	public boolean isInvNameLocalized()
	{
		return customName != null && customName.length() > 0;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public void onInventoryChanged() {
		writeToNBT(item.stackTagCompound);
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
			setAmountUpgradeWithoutNotify(upgrade, nbt.getInteger(upgrade.toString()));
		}
		items = new ItemStack[getSizeInventory()];
		FileUtil.readFromNBT(this, nbt);
	}

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
	public void setAmountUpgrade(Upgrade upgrade, int amount) {
		setAmountUpgradeWithoutNotify(upgrade, amount);
		onInventoryChanged();
	}
	
	public void setAmountUpgradeWithoutNotify(Upgrade upgrade, int amount) {
		if (upgrades.containsKey(upgrade)) {
			upgrades.remove(upgrade);
		}
		upgrades.put(upgrade, new Integer(amount));
	}
	
	@Override
	public boolean hasEnergy() {
		return isUpgradeInstalled(Upgrade.ENERGY);
	}
	
	private int tick = new Random().nextInt(64);
	
	private double x;
	private double y;
	private double z;
	
	public void onUpdate(EntityPlayer player) {
		if (player.worldObj.isRemote) {
			this.readFromNBT(item.stackTagCompound);
		}
		if (tick-- <= 0) {
			tick = 64;
		}
		this.x = player.posX;
		this.y = player.posY;
		this.z = player.posZ;
		UpgradeHelper.updateChest(this, tick, player.worldObj);
	}
	
	private static HashMap<ItemStack, BagInventory> invs = new HashMap<ItemStack, BagInventory>();
	
	public static BagInventory getInvForItem(ItemStack item) {
		if (! invs.containsKey(item)) {
			BagInventory inv = new BagInventory(item);
			invs.put(item, inv);
			return inv;
		}
		return invs.get(item);
	}

	@Override
	public void setStackInSlotWithoutNotify(int slot, ItemStack item) {
		items[slot] = item;
		
		if (item != null && item.stackSize > getInventoryStackLimit())
		{
			item.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public double getXPos() {
		return x;
	}

	@Override
	public double getYPos() {
		return y;
	}

	@Override
	public double getZPos() {
		return z;
	}
	
}
