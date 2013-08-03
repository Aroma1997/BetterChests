
package aroma1997.betterchests;


import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;

public class TileEntityBChest extends TileEntityChest {
	
	private short stackLimit;
	
	private short slotLimit;
	
	private boolean redstoneUpgrade;
	
	private short light;
	
	private boolean comparator;
	
	private boolean playerUpgrade;
	
	private String player;
	
	private boolean voidU;
	
	private boolean indestructable;
	
	private boolean rain;
	
	public TileEntityBChest() {
		stackLimit = Reference.Conf.STACK_START;
		slotLimit = Reference.Conf.SLOT_START;
		light = Reference.Conf.LIGHT_START;
		player = "";
	}
	
	@Override
	public String getInvName() {
		if (voidU) {
			return Colors.RED + "Void Chest";
		}
		return "Adjustable Chest";
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if (voidU) {
			for (int i = 0; i < getSizeInventory(); i++) {
				if (getStackInSlot(i) == null) {
					continue;
				}
				decrStackSize(i, getStackInSlot(i).stackSize);
			}
		}
		
		if (rain && worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord) && worldObj.isRaining()
			&& new Random().nextFloat() > Reference.Conf.RAIN_THINGY) {
			doRainThingy();
		}
	}
	
	@Override
	public int getSizeInventory() {
		return slotLimit;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		if (! super.isUseableByPlayer(par1EntityPlayer)) {
			return false;
		}
		if (! playerUpgrade) {
			return true;
		}
		/*
		 * if (!MinecraftServer.getServer().isDedicatedServer()) { this.player =
		 * par1EntityPlayer.username; return true; }
		 */
		if (MinecraftServer.getServerConfigurationManager(MinecraftServer.getServer()).getOps().contains(
			par1EntityPlayer.username)
			|| player.equalsIgnoreCase(par1EntityPlayer.username)) {
			return true;
		}
		return false;
		
	}
	
	@Override
	public int getInventoryStackLimit() {
		return stackLimit;
	}
	
	public boolean upgrade(EntityPlayer player) {
		if (! (player.getHeldItem().getItem() instanceof ItemUpgrade)
			|| ! isUseableByPlayer(player)) {
			return false;
		}
		ItemStack item = player.getHeldItem();
		Upgrade upgrade = Upgrade.values()[item.getItemDamage()];
		switch (upgrade) {
			case SLOT: {
				if (slotLimit + Reference.Conf.SLOT_UPGRADE > Reference.Conf.SLOT_LIMIT) {
					return false;
				}
				slotLimit += Reference.Conf.SLOT_UPGRADE;
				onUpgradeInserted(player);
				return true;
			}
			case STACK: {
				if (stackLimit + Reference.Conf.STACK_UPGRADE > Reference.Conf.STACK_LIMIT) {
					return false;
				}
				stackLimit += Reference.Conf.STACK_UPGRADE;
				onUpgradeInserted(player);
				return true;
			}
			case REDSTONE: {
				if (redstoneUpgrade) {
					return false;
				}
				redstoneUpgrade = true;
				onUpgradeInserted(player);
				return true;
			}
			case LIGHT: {
				if (light + Reference.Conf.LIGHT_UPGRADE > Reference.Conf.LIGHT_LIMIT) {
					return false;
				}
				light++;
				onUpgradeInserted(player);
				return true;
			}
			case COMPARATOR: {
				if (comparator) {
					return false;
				}
				comparator = true;
				onUpgradeInserted(player);
				return true;
			}
			case PLAYER: {
				if (playerUpgrade) {
					return false;
				}
				playerUpgrade = true;
				this.player = player.username;
				onUpgradeInserted(player);
				return true;
			}
			case VOID: {
				if (voidU) {
					return false;
				}
				voidU = true;
				onUpgradeInserted(player);
				return true;
			}
			case UNBREAKABLE: {
				if (indestructable) {
					return false;
				}
				indestructable = true;
				onUpgradeInserted(player);
				return true;
			}
			case RAIN: {
				if (rain) {
					return false;
				}
				rain = true;
				onUpgradeInserted(player);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		slotLimit = par1NBTTagCompound.getShort("slotLimit");
		stackLimit = par1NBTTagCompound.getShort("stackLimit");
		redstoneUpgrade = par1NBTTagCompound.getBoolean("redstoneUpgrade");
		light = par1NBTTagCompound.getShort("lightValue");
		comparator = par1NBTTagCompound.getBoolean("comparator");
		player = par1NBTTagCompound.getString("player");
		playerUpgrade = par1NBTTagCompound.getBoolean("playerUpgrade");
		voidU = par1NBTTagCompound.getBoolean("voidU");
		indestructable = par1NBTTagCompound.getBoolean("indestructable");
		rain = par1NBTTagCompound.getBoolean("rain");
		super.readFromNBT(par1NBTTagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("slotLimit", slotLimit);
		par1NBTTagCompound.setShort("stackLimit", stackLimit);
		par1NBTTagCompound.setBoolean("redstoneUpgrade", redstoneUpgrade);
		par1NBTTagCompound.setShort("lightValue", light);
		par1NBTTagCompound.setBoolean("comparator", comparator);
		par1NBTTagCompound.setString("player", player);
		par1NBTTagCompound.setBoolean("playerUpgrade", playerUpgrade);
		par1NBTTagCompound.setBoolean("voidU", voidU);
		par1NBTTagCompound.setBoolean("indestructable", indestructable);
		par1NBTTagCompound.setBoolean("rain", rain);
	}
	
	private void onUpgradeInserted(EntityPlayer player) {
		player.inventory.mainInventory[player.inventory.currentItem].stackSize -= 1;
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		super.readFromNBT(nbttagcompound);
	}
	
	public boolean hasRedstoneUpgrade() {
		return redstoneUpgrade;
	}
	
	public int getLightValue() {
		return light;
	}
	
	public boolean isPlayerUpgrade() {
		return playerUpgrade;
	}
	
	public boolean hasIndestructableUpgrade() {
		return indestructable;
	}
	
	private void doRainThingy() {
		int bucketEmpty = - 1;
		int emptySpace = - 1;
		for (int i = 0; i < getSizeInventory(); i++) {
			if (getStackInSlot(i) != null
				&& getStackInSlot(i).itemID == Item.bucketEmpty.itemID && bucketEmpty == - 1) {
				bucketEmpty = i;
				if (getStackInSlot(i).stackSize == 1) {
					emptySpace = i;
					break;
				}
				continue;
			}
			if (emptySpace == - 1 && getStackInSlot(i) == null) {
				emptySpace = i;
				continue;
			}
		}
		if (bucketEmpty == - 1 || emptySpace == - 1) {
			return;
		}
		decrStackSize(bucketEmpty, 1);
		setInventorySlotContents(emptySpace, new ItemStack(Item.bucketWater));
	}
	
	public int getComparatorOutput() {
		
		if (! comparator) {
			return 0;
		}
		if (rain) {
			int w = 0;
			int e = 0;
			for (int i = 0; i < getSizeInventory(); i++) {
				if (getStackInSlot(i) != null
					&& getStackInSlot(i).itemID == Item.bucketWater.itemID) {
					w++;
				}
				if (getStackInSlot(i) != null
					&& getStackInSlot(i).itemID == Item.bucketEmpty.itemID) {
					e += getStackInSlot(i).stackSize;
				}
			}
			if (w == 0) {
				return 0;
			}
			if (e == 0) {
				return 16;
			}
			return (int) ((float) w / (float) e * 2);
		}
		return Container.calcRedstoneFromInventory(this);
	}
	
}
