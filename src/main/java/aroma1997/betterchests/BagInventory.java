/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.core.client.inventories.GUIContainer;
import aroma1997.core.inventories.ContainerBasic;
import aroma1997.core.inventories.ContainerItem;
import aroma1997.core.inventories.IAdvancedInventory;
import aroma1997.core.inventories.ISpecialInventory;
import aroma1997.core.util.FileUtil;
import aroma1997.core.util.ItemUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BagInventory implements IBetterChest, IAdvancedInventory, ISpecialInventory {
	
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
	
	private ArrayList<ItemStack> upgrades = new ArrayList<ItemStack>();
	
	@Override
	public ItemStack getStackInSlot(int par1)
	{
		if (par1 >= items.length || par1 < 0 || isUpgradeInstalled(Upgrade.VOID.getItem())) {
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
				markDirty();
				return itemstack;
			}
			else
			{
				itemstack = items[par1].splitStack(par2);
				
				if (items[par1].stackSize == 0)
				{
					items[par1] = null;
				}
				
				markDirty();
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
		
		markDirty();
	}
	
	@Override
	public String getInventoryName()
	{
		return hasCustomInventoryName() ? customName : "inv.betterchests:bag.name";
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	
	@Override
	public int getSizeInventory() {
		return getAmountUpgrade(Upgrade.SLOT.getItem()) * 9 + 9;
	}
	
	@Override
	public Slot getSlot(int slot, int index, int x, int y) {
		return new Slot(this, index, x, y);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void drawGuiContainerForegroundLayer(GUIContainer gui, ContainerBasic container,
		int par1, int par2) {
		for (ItemStack item : upgrades) {
			if (UpgradeHelper.isUpgrade(item)) {
				continue;
			}
			((IUpgrade) item.getItem()).drawGuiContainerForegroundLayer(gui, container, par1, par2,
				item);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void drawGuiContainerBackgroundLayer(GUIContainer gui, ContainerBasic container,
		float f, int i, int j) {
		
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		for (Upgrade upgrade : Upgrade.values()) {
			int amount = nbt.getInteger(upgrade.toString());
			if (amount == 0) {
				continue;
			}
			setAmountUpgradeWithoutNotify(upgrade.getItem(), amount);
			nbt.removeTag(upgrade.toString());
		}
		NBTTagList list = nbt.getTagList("upgrades", new NBTTagCompound().getId());
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound upgradenbt = (NBTTagCompound) list.getCompoundTagAt(i);
			ItemStack item = ItemStack.loadItemStackFromNBT(upgradenbt);
			upgrades.add(item);
		}
		if (nbt.hasKey("display"))
		{
			NBTTagCompound nbttagcompound = nbt.getCompoundTag("display");
			
			if (nbttagcompound.hasKey("Name"))
			{
				customName = nbttagcompound.getString("Name");
			}
		}
		items = new ItemStack[getSizeInventory()];
		FileUtil.readFromNBT(this, nbt);
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		FileUtil.writeToNBT(this, nbt);
		NBTTagList list = new NBTTagList();
		for (ItemStack item : upgrades) {
			NBTTagCompound upgradesbt = new NBTTagCompound();
			item.writeToNBT(upgradesbt);
			list.appendTag(upgradesbt);
		}
		nbt.setTag("upgrades", list);
	}
	
	@Override
	public ContainerBasic getContainer(EntityPlayer player, int i) {
		return new ContainerItem(player.inventory, this, i);
	}
	
	@Override
	public int getAmountUpgrade(ItemStack upgrade) {
		if (! UpgradeHelper.isUpgrade(upgrade)) {
			return 0;
		}
		for (ItemStack item : upgrades) {
			if (ItemUtil.areItemsSame(item, upgrade)) {
				return item.stackSize;
			}
		}
		return 0;
	}
	
	@Override
	public boolean isUpgradeInstalled(ItemStack upgrade) {
		return getAmountUpgrade(upgrade) > 0;
	}
	
	@Override
	public void setAmountUpgrade(ItemStack upgrade, int amount) {
		setAmountUpgradeWithoutNotify(upgrade, amount);
		markDirty();
	}
	
	public void setAmountUpgradeWithoutNotify(ItemStack upgrade, int amount) {
		for (ItemStack item : upgrades) {
			if (ItemUtil.areItemsSame(item, upgrade)) {
				if (amount <= 0) {
					upgrades.remove(item);
					return;
				}
				else {
					item.stackSize = amount;
					return;
				}
			}
		}
		upgrade = upgrade.copy();
		upgrade.stackSize = amount;
		upgrades.add(upgrade);
	}
	
	@Override
	public boolean hasEnergy() {
		return isUpgradeInstalled(Upgrade.ENERGY.getItem());
	}
	
	private int tick = new Random().nextInt(64);
	
	private EntityPlayer player;
	
	public void onUpdate(EntityPlayer player) {
		if (player.worldObj.isRemote) {
			// this.readFromNBT(item.stackTagCompound);
		}
		if (tick-- <= 0) {
			tick = 64;
		}
		this.player = player;
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
		if (isUpgradeInstalled(Upgrade.VOID.getItem())) {
			return;
		}
		items[slot] = item;
		
		if (item != null && item.stackSize > getInventoryStackLimit())
		{
			item.stackSize = getInventoryStackLimit();
		}
	}
	
	@Override
	public double getXPos() {
		return player.posX;
	}
	
	@Override
	public double getYPos() {
		return player.posY + 1.0D;
	}
	
	@Override
	public double getZPos() {
		return player.posZ;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<ItemStack> getUpgrades() {
		return (ArrayList<ItemStack>) upgrades.clone();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return customName != null && customName.length() > 0;
	}

	@Override
	public void markDirty() {
		writeToNBT(item.stackTagCompound);
		
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}
	
}
