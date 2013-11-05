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
import java.util.List;
import java.util.Random;

import aroma1997.core.client.inventories.GUIContainer;
import aroma1997.core.inventories.ContainerBasic;
import aroma1997.core.inventories.ContainerItem;
import aroma1997.core.inventories.IAdvancedInventory;
import aroma1997.core.util.FileUtil;
import aroma1997.core.util.InvUtil;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

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
			setAmountUpgradeWithoutNotify(upgrade, nbt.getInteger(upgrade.toString()));
		}
		items = new ItemStack[getSizeInventory()];
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
	
	public void onUpdate(EntityPlayer player) {
		this.readFromNBT(item.stackTagCompound);
		if (tick-- <= 0) {
			tick = 64;
		}
		UpgradeHelper.updateChest(this, tick);
		if (isUpgradeInstalled(Upgrade.COLLECTOR)) {
//	      int dragCount = player.username.hashCode();
	      float radius = this.getAmountUpgrade(Upgrade.COLLECTOR) - 0.2F;
	      AxisAlignedBB bounds = player.boundingBox.expand(radius, radius, radius);
	      World world = player.worldObj;
	      if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
	      {
	        bounds.expand(0.2000000029802322D, 0.2000000029802322D, 0.2000000029802322D);
	      }
	      List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, bounds);
	      for (EntityItem e : list) {
	        if (e.age >= 10)
	        {
	          ItemStack itemBack = InvUtil.putIntoFirstSlot(this, e.getEntityItem());
	          this.writeToNBT(this.item.stackTagCompound);
	        	if (itemBack == null) {
	        		e.setDead();
	        	}
	        	else {
	        		e.setEntityItemStack(itemBack);
	        		double x = player.posX - e.posX;
	  	          double y = player.posY - e.posY;
	  	          double z = player.posZ - e.posZ;
	  	          
	  	          double length = Math.sqrt(x * x + y * y + z * z) * 2.0D;
	  	          
	  	          x = x / length + player.motionX / 2.0D;
	  	          y = y / length + player.motionY / 2.0D;
	  	          z = z / length + player.motionZ / 2.0D;
	  	          
	  	          e.motionX = x;
	  	          e.motionY = y;
	  	          e.motionZ = z;
	  	          e.isAirBorne = true;
	  	          if (e.isCollidedHorizontally) {
	  	            e.motionY += 1.0D;
	  	          }
	        	}
	          if (world.rand.nextInt(20) == 0)
	          {
	            float pitch = 0.85F - world.rand.nextFloat() * 3.0F / 10.0F;
	            world.playSoundEffect(e.posX, e.posY, e.posZ, "mob.endermen.portal", 0.6F, pitch);
	          }
	        }
	      }
		}
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
	
}
