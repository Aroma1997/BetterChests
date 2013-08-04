
package aroma1997.betterchests;


import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;

public class TileEntityBChest extends TileEntityChest {
	
	private short slotLimit;
	
	private boolean redstoneUpgrade;
	
	private boolean cobbleGen;
	
	private boolean light;
	
	private boolean comparator;
	
	private boolean playerUpgrade;
	
	private String player;
	
	private boolean voidU;
	
	private boolean indestructable;
	
	private boolean rain;
	
	public TileEntityBChest() {
		slotLimit = Reference.Conf.SLOT_START;
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
		
		if (cobbleGen && new Random().nextFloat() > Reference.Conf.COBBLEGEN_THINGY) {
			int bucketLava = - 1;
			int bucketWater = - 1;
			int empty = -1;
			for (int i = 0; i < getSizeInventory(); i++) {
				if (getStackInSlot(i) != null
					&& getStackInSlot(i).itemID == Item.bucketWater.itemID && bucketWater == - 1) {
					bucketWater = i;
					continue;
				}
				if (getStackInSlot(i) != null && bucketLava == - 1 && getStackInSlot(i).itemID == Item.bucketLava.itemID) {
					bucketLava = i;
					continue;
				}
				if (empty == -1 && (getStackInSlot(i) == null || (getStackInSlot(i) != null && getStackInSlot(i).itemID == Block.cobblestone.blockID && getStackInSlot(i).stackSize < getStackInSlot(i).getMaxStackSize()))) {
					empty = i;
					continue;
				}
			}
			if (bucketLava == - 1 || bucketWater == - 1 || empty == -1) {
				return;
			}
			int amount;
			
			if (getStackInSlot(empty) == null) {
				amount = 1;
			}
			else {
				amount = 1 + getStackInSlot(empty).stackSize;
			}
			
			setInventorySlotContents(empty, new ItemStack(Block.cobblestone, amount));
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
			case REDSTONE: {
				if (redstoneUpgrade) {
					return false;
				}
				redstoneUpgrade = true;
				onUpgradeInserted(player);
				return true;
			}
			case LIGHT: {
				if (light) {
					return false;
				}
				light = true;
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
			case COBBLEGEN: {
				if (cobbleGen) return false;
				cobbleGen = true;
				onUpgradeInserted(player);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		slotLimit 		= par1NBTTagCompound.getShort("slotLimit");
		redstoneUpgrade = par1NBTTagCompound.getBoolean("redstoneUpgrade");
		light 			= par1NBTTagCompound.getBoolean("light");
		comparator 		= par1NBTTagCompound.getBoolean("comparator");
		player 			= par1NBTTagCompound.getString("player");
		playerUpgrade 	= par1NBTTagCompound.getBoolean("playerUpgrade");
		voidU 			= par1NBTTagCompound.getBoolean("voidU");
		indestructable 	= par1NBTTagCompound.getBoolean("indestructable");
		rain 			= par1NBTTagCompound.getBoolean("rain");
		cobbleGen 		= par1NBTTagCompound.getBoolean("cobbleGen");
		super.readFromNBT(par1NBTTagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("slotLimit", slotLimit);
		par1NBTTagCompound.setBoolean("redstoneUpgrade", redstoneUpgrade);
		par1NBTTagCompound.setBoolean("light", light);
		par1NBTTagCompound.setBoolean("comparator", comparator);
		par1NBTTagCompound.setString("player", player);
		par1NBTTagCompound.setBoolean("playerUpgrade", playerUpgrade);
		par1NBTTagCompound.setBoolean("voidU", voidU);
		par1NBTTagCompound.setBoolean("indestructable", indestructable);
		par1NBTTagCompound.setBoolean("rain", rain);
		par1NBTTagCompound.setBoolean("cobbleGen", this.cobbleGen);
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
		return light ? 15 : 0;
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

	public void playerOpenChest(EntityPlayer player) {
		if (!this.playerUpgrade) return;
		if (player.username.equalsIgnoreCase(this.player)) return;
		player.attackEntityFrom(DamageSource.outOfWorld, 5.0F);
	}
	
}
