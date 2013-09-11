/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests;


import java.util.Random;

import aroma1997.core.misc.FakePlayer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.Hopper;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

public class TileEntityBChest extends TileEntityChest implements Hopper {
	
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
	
	private boolean solar;
	
	private boolean furnace;
	
	private int tick;
	
	private boolean suckItems;
	
	private boolean ticking;
	
	private FakePlayer fplayer;
	
	public TileEntityBChest() {
		slotLimit = Reference.Conf.SLOT_START;
		player = "";
		tick = new Random().nextInt(64);
	}
	
	@Override
	public String getInvName() {
		if (voidU) {
			return Colors.RED + "Void Chest";
		}
		return "Adjustable Chest";
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot >= slotLimit) {
			return null;
		}
		return super.getStackInSlot(slot);
	}
	
	@Override
	public void validate() {
		super.validate();

		fplayer = FakePlayer.getFakePlayer(worldObj);
		fplayer.posX = xCoord;
		fplayer.posY = yCoord;
		fplayer.posZ = zCoord;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if (worldObj.isRemote) {
			return;
		}
		if (tick-- <= 0) {
			tick = 64;
			onInventoryChanged();
			updateBlock(xCoord + 1, yCoord, zCoord);
			updateBlock(xCoord - 1, yCoord, zCoord);
			updateBlock(xCoord, yCoord, zCoord + 1);
			updateBlock(xCoord, yCoord, zCoord - 1);
			updateBlock(xCoord, yCoord + 1, zCoord);
			updateBlock(xCoord, yCoord - 1, zCoord);
		}
		if (voidU) {
			for (int i = 0; i < getSizeInventory(); i++) {
				if (getStackInSlot(i) == null) {
					continue;
				}
				decrStackSize(i, getStackInSlot(i).stackSize);
			}
		}
		
		if (rain && worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord) && worldObj.isRaining()
			&& new Random().nextFloat() > Reference.Conf.RAIN_THINGY && tick == 20) {
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
		
		if (cobbleGen && tick == 30) {
			int bucketLava = - 1;
			int bucketWater = - 1;
			int empty = - 1;
			for (int i = 0; i < getSizeInventory(); i++) {
				if (getStackInSlot(i) != null
					&& getStackInSlot(i).itemID == Item.bucketWater.itemID && bucketWater == - 1) {
					bucketWater = i;
					continue;
				}
				if (getStackInSlot(i) != null && bucketLava == - 1
					&& getStackInSlot(i).itemID == Item.bucketLava.itemID) {
					bucketLava = i;
					continue;
				}
				if (empty == - 1
					&& (getStackInSlot(i) == null || getStackInSlot(i) != null
					&& getStackInSlot(i).itemID == Block.cobblestone.blockID && getStackInSlot(i).stackSize < getStackInSlot(
						i).getMaxStackSize())) {
					empty = i;
					continue;
				}
			}
			if (bucketLava == - 1 || bucketWater == - 1 || empty == - 1) {
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
		
		if (furnace && tick == 40 && hasEnergy()) {
			int cooking = - 1;
			for (int i = 0; i < getSizeInventory(); i++) {
				ItemStack stack = getStackInSlot(i);
				if (stack == null) {
					continue;
				}
				if (FurnaceRecipes.smelting().getSmeltingResult(stack) == null) {
					continue;
				}
				cooking = i;
				break;
			}
			if (cooking != - 1) {
				ItemStack smelted = FurnaceRecipes.smelting().getSmeltingResult(
					getStackInSlot(cooking)).copy();
				if (smelted.stackSize <= 0) {
					smelted.stackSize = 1;
				}
				int result = - 1;
				for (int i = 0; i < getSizeInventory(); i++) {
					if (getStackInSlot(i) == null
						|| smelted.isItemEqual(getStackInSlot(i)) && smelted.stackSize
						+ getStackInSlot(i).stackSize <= 64) {
						result = i;
						break;
					}
				}
				if (result != - 1) {
					decrStackSize(cooking, 1);
					ItemStack put = getStackInSlot(result);
					if (put != null) {
						put.stackSize += smelted.stackSize;
					}
					else {
						put = smelted;
					}
					setInventorySlotContents(result, put);
				}
			}
		}
		
		if (suckItems && tick == 50) {
			for (int i = - Reference.Conf.HOPPERRADIUS; i <= Reference.Conf.HOPPERRADIUS; i++) {
				for (int j = - Reference.Conf.HOPPERRADIUS; j <= Reference.Conf.HOPPERRADIUS; j++) {
					for (int k = 0; k <= 1; k++) {
						EntityItem entityitem = TileEntityHopper.getEntityAbove(worldObj, xCoord + i,
							(double) yCoord + k, zCoord + j);
						
						if (entityitem != null)
						{
							TileEntityHopper.insertStackFromEntity(this, entityitem);
						}
					}
				}
			}
		}
		
		if (ticking && tick % 32 == 0) {
			for (int i = 0; i < getSizeInventory(); i++) {
				ItemStack item = getStackInSlot(i);
				if (item == null || item.getItem() == null) continue;
				fplayer.inventory.mainInventory[0] = this.getStackInSlot(i);
				fplayer.inventory.onInventoryChanged();
				item.getItem().onUpdate(item, worldObj, fplayer, 0, false);
				onInventoryChanged();
//				this.setInventorySlotContents(i, fplayer.inventory.mainInventory[0]);
			}
		}
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
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
		
		if (! MinecraftServer.getServer().isDedicatedServer()
			&& par1EntityPlayer.username.equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.username)) {
			return true;
		}
		
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
				if (playerUpgrade || ! indestructable) {
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
				if (cobbleGen) {
					return false;
				}
				cobbleGen = true;
				onUpgradeInserted(player);
				return true;
			}
			case SOLAR: {
				if (solar) {
					return false;
				}
				solar = true;
				onUpgradeInserted(player);
				return true;
			}
			case FURNACE: {
				if (furnace || ! solar) {
					return false;
				}
				furnace = true;
				onUpgradeInserted(player);
				return true;
			}
			case COLLECTOR: {
				if (suckItems || ! solar) {
					return false;
				}
				suckItems = true;
				onUpgradeInserted(player);
				return true;
			}
			case TICKING: {
				if (ticking || !solar) {
					return false;
				}
				ticking = true;
				onUpgradeInserted(player);
				return true;
			}
			case BASIC : {}
		}
		return false;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		slotLimit = par1NBTTagCompound.getShort("slotLimit");
		redstoneUpgrade = par1NBTTagCompound.getBoolean("redstoneUpgrade");
		light = par1NBTTagCompound.getBoolean("light");
		comparator = par1NBTTagCompound.getBoolean("comparator");
		player = par1NBTTagCompound.getString("player");
		playerUpgrade = par1NBTTagCompound.getBoolean("playerUpgrade");
		voidU = par1NBTTagCompound.getBoolean("voidU");
		indestructable = par1NBTTagCompound.getBoolean("indestructable");
		rain = par1NBTTagCompound.getBoolean("rain");
		cobbleGen = par1NBTTagCompound.getBoolean("cobbleGen");
		solar = par1NBTTagCompound.getBoolean("solar");
		furnace = par1NBTTagCompound.getBoolean("furnace");
		suckItems = par1NBTTagCompound.getBoolean("suckItems");
		ticking = par1NBTTagCompound.getBoolean("ticking");
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
		par1NBTTagCompound.setBoolean("cobbleGen", cobbleGen);
		par1NBTTagCompound.setBoolean("solar", solar);
		par1NBTTagCompound.setBoolean("furnace", furnace);
		par1NBTTagCompound.setBoolean("suckItems", suckItems);
		par1NBTTagCompound.setBoolean("ticking", ticking);
	}
	
	private void onUpgradeInserted(EntityPlayer player) {
		player.inventory.mainInventory[player.inventory.currentItem].stackSize -= 1;
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		readFromNBT(nbttagcompound);
		onInventoryChanged();
		
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
				return 15;
			}
			return (int) (w / ((float) e + (float) w) * 15);
		}
		return Container.calcRedstoneFromInventory(this);
	}
	
	public void playerOpenChest(EntityPlayer player) {
		if (! playerUpgrade) {
			return;
		}
		if (isUseableByPlayer(player)) {
			return;
		}
		player.attackEntityFrom(DamageSource.outOfWorld, 5.0F);
	}
	
	public ItemStack[] getItemUpgrades() {
		int amount = 0;
		if (slotLimit > Reference.Conf.SLOT_START) {
			amount++;
		}
		if (redstoneUpgrade) {
			amount++;
		}
		if (light) {
			amount++;
		}
		if (comparator) {
			amount++;
		}
		if (playerUpgrade) {
			amount++;
		}
		if (voidU) {
			amount++;
		}
		if (indestructable) {
			amount++;
		}
		if (rain) {
			amount++;
		}
		if (cobbleGen) {
			amount++;
		}
		if (solar) {
			amount++;
		}
		if (furnace) {
			amount++;
		}
		
		ItemStack[] items = new ItemStack[amount];
		int i1 = 0;
		for (int i = 0; i < amount; i++) {
			if (i1 == 0) {
				if (slotLimit > Reference.Conf.SLOT_START) {
					items[i] = new ItemStack(BetterChests.upgrade,
						(slotLimit - Reference.Conf.SLOT_START) / Reference.Conf.SLOT_UPGRADE,
						Upgrade.SLOT.ordinal());
					i1++;
					continue;
				}
				i1++;
			}
			if (i1 == 1) {
				if (redstoneUpgrade) {
					items[i] = new ItemStack(BetterChests.upgrade, 1, Upgrade.REDSTONE.ordinal());
					i1++;
					continue;
				}
				i1++;
			}
			if (i1 == 2) {
				if (light) {
					items[i] = new ItemStack(BetterChests.upgrade, 1, Upgrade.LIGHT.ordinal());
					i1++;
					continue;
				}
				i1++;
			}
			if (i1 == 3) {
				if (comparator) {
					items[i] = new ItemStack(BetterChests.upgrade, 1, Upgrade.COMPARATOR.ordinal());
					i1++;
					continue;
				}
				i1++;
			}
			if (i1 == 4) {
				if (playerUpgrade) {
					items[i] = new ItemStack(BetterChests.upgrade, 1, Upgrade.PLAYER.ordinal());
					i1++;
					continue;
				}
				i1++;
			}
			if (i1 == 5) {
				if (voidU) {
					items[i] = new ItemStack(BetterChests.upgrade, 1, Upgrade.VOID.ordinal());
					i1++;
					continue;
				}
				i1++;
			}
			if (i1 == 6) {
				if (indestructable) {
					items[i] = new ItemStack(BetterChests.upgrade, 1, Upgrade.UNBREAKABLE.ordinal());
					i1++;
					continue;
				}
				i1++;
			}
			if (i1 == 7) {
				if (rain) {
					items[i] = new ItemStack(BetterChests.upgrade, 1, Upgrade.RAIN.ordinal());
					i1++;
					continue;
				}
				i1++;
			}
			if (i1 == 8) {
				if (cobbleGen) {
					items[i] = new ItemStack(BetterChests.upgrade, 1, Upgrade.COBBLEGEN.ordinal());
					i1++;
					continue;
				}
				i1++;
			}
			if (i1 == 9) {
				if (solar) {
					items[i] = new ItemStack(BetterChests.upgrade, 1, Upgrade.SOLAR.ordinal());
					i1++;
					continue;
				}
				i1++;
			}
			if (i1 == 10) {
				if (furnace) {
					items[i] = new ItemStack(BetterChests.upgrade, 1, Upgrade.FURNACE.ordinal());
					i1++;
					continue;
				}
				i1++;
			}
			if (i1 == 11) {
				if (suckItems) {
					items[i] = new ItemStack(BetterChests.upgrade, 1, Upgrade.COLLECTOR.ordinal());
					i1++;
					continue;
				}
				i1++;
			}
			
		}
		return items;
	}
	
	private boolean hasEnergy() {
		return solar && (worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord) && worldObj.isDaytime() || new Random().nextFloat() > Reference.Conf.ENERGY_NONDAY);
	}
	
	public boolean hasSolar() {
		return solar;
	}
	
	@Override
	public double getXPos() {
		return xCoord;
	}
	
	@Override
	public double getYPos() {
		return yCoord;
	}
	
	@Override
	public double getZPos() {
		return zCoord;
	}
	
	public int getRedstoneOutput() {
		if (! redstoneUpgrade) {
			return 0;
		}
		if (rain) {
			if (worldObj.isThundering()) {
				return 2;
			}
			if (worldObj.isRaining()) {
				return 1;
			}
			return 0;
		}
		return MathHelper.clamp_int(numUsingPlayers, 0, 15);
	}
	
	private void updateBlock(int x, int y, int z) {
		if (worldObj.isAirBlock(x, y, z)) {
			return;
		}
		Block.blocksList[worldObj.getBlockId(x, y, z)].onNeighborBlockChange(worldObj, x, y, z,
			BetterChests.chest.blockID);
	}
	
}
