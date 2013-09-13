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

import aroma1997.core.client.util.Colors;
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
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.Hopper;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

public class TileEntityBChest extends TileEntityChest implements Hopper {
	
	private String player;
	
	private int tick;
	
	private FakePlayer fplayer;
	
	private HashMap<Upgrade, Integer> upgrades = new HashMap<Upgrade, Integer>();
	
	public TileEntityBChest() {
		player = "";
		tick = new Random().nextInt(64);
		for (Upgrade upgrade : Upgrade.values()) {
			upgrades.put(upgrade, 0);
		}
	}
	
	@Override
	public String getInvName() {
		if (isUpgradeInstalled(Upgrade.VOID)) {
			return Colors.RED + "Void Chest";
		}
		return "Adjustable Chest";
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot >= getSizeInventory()) {
			return null;
		}
		return super.getStackInSlot(slot);
	}
	
	@Override
	public void validate() {
		super.validate();

		if (!worldObj.isRemote) {
			fplayer = FakePlayer.getFakePlayer(worldObj);
			fplayer.posX = xCoord;
			fplayer.posY = yCoord;
			fplayer.posZ = zCoord;
		}
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
		if (isUpgradeInstalled(Upgrade.VOID)) {
			for (int i = 0; i < getSizeInventory(); i++) {
				if (getStackInSlot(i) == null) {
					continue;
				}
				decrStackSize(i, getStackInSlot(i).stackSize);
			}
		}
		
		if (isUpgradeInstalled(Upgrade.RAIN) && worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord) && worldObj.isRaining()
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
		
		if (isUpgradeInstalled(Upgrade.COBBLEGEN) && tick == 30) {
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
		
		if (isUpgradeInstalled(Upgrade.FURNACE) && tick == 40 && hasEnergy()) {
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
		
		if (isUpgradeInstalled(Upgrade.COLLECTOR) && tick == 50) {
			for (int i = - getAmountUpgrade(Upgrade.COLLECTOR); i <= getAmountUpgrade(Upgrade.COLLECTOR); i++) {
				for (int j = - getAmountUpgrade(Upgrade.COLLECTOR); j <= getAmountUpgrade(Upgrade.COLLECTOR); j++) {
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
		
		if (isUpgradeInstalled(Upgrade.TICKING) && tick % 8 == 0) {
			for (int i = 0; i < getSizeInventory(); i++) {
				ItemStack item = getStackInSlot(i);
				if (item == null || item.getItem() == null) continue;
				fplayer.inventory.mainInventory[0] = this.getStackInSlot(i);
				fplayer.inventory.onInventoryChanged();
				item.getItem().onUpdate(item, worldObj, fplayer, 0, false);
				onInventoryChanged();
			}
		}
	}
	
	@Override
    public Packet getDescriptionPacket()
    {
    	NBTTagCompound nbt = new NBTTagCompound();
    	writeToNBT(nbt);
    	Packet132TileEntityData packet = new Packet132TileEntityData();
    	packet.data = nbt;
        return packet;
    }
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}
	
	@Override
	public int getSizeInventory() {
		return 9 + (getAmountUpgrade(Upgrade.SLOT) * 9);
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		if (! super.isUseableByPlayer(par1EntityPlayer)) {
			return false;
		}
		if (! (isUpgradeInstalled(Upgrade.PLAYER))) {
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
		
		if (!(getAmountUpgrade(upgrade) >= upgrade.getMaxAmount())) {
			if (upgrade.getRequirement() == null || isUpgradeInstalled(upgrade.getRequirement())) {
				setAmountUpgrade(upgrade, getAmountUpgrade(upgrade) + 1);
				onUpgradeInserted(player);
				return true;
			}
		}
		
		return false;
	}
	
	private int getAmountUpgrade(Upgrade upgrade) {
		return upgrades.get(upgrade);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("slotLimit")) {
			readFromNBTOld(nbt);
			return;
		}
		for (Upgrade upgrade : Upgrade.values()) {
			setAmountUpgrade(upgrade, nbt.getInteger(upgrade.toString()));
		}
		player = nbt.getString("player");
		super.readFromNBT(nbt);
	}
	
	private void readFromNBTOld(NBTTagCompound par1NBTTagCompound)
	{
		setAmountUpgrade(Upgrade.SLOT, par1NBTTagCompound.getShort("slotLimit"));
		setAmountUpgrade(Upgrade.REDSTONE, par1NBTTagCompound.getBoolean("redstoneUpgrade") ? 1 : 0);
		setAmountUpgrade(Upgrade.LIGHT, par1NBTTagCompound.getBoolean("light") ? 1 : 0);
		setAmountUpgrade(Upgrade.COMPARATOR, par1NBTTagCompound.getBoolean("comparator") ? 1 : 0);
		setAmountUpgrade(Upgrade.PLAYER, par1NBTTagCompound.getBoolean("playerUpgrade") ? 1 : 0);;
		setAmountUpgrade(Upgrade.VOID, par1NBTTagCompound.getBoolean("voidU") ? 1 : 0);
		setAmountUpgrade(Upgrade.UNBREAKABLE, par1NBTTagCompound.getBoolean("indestructable") ? 1 : 0);
		setAmountUpgrade(Upgrade.RAIN, par1NBTTagCompound.getBoolean("rain") ? 1 : 0);
		setAmountUpgrade(Upgrade.COBBLEGEN, par1NBTTagCompound.getBoolean("cobbleGen") ? 1 : 0);
		setAmountUpgrade(Upgrade.SOLAR, par1NBTTagCompound.getBoolean("solar") ? 1 : 0);
		setAmountUpgrade(Upgrade.FURNACE, par1NBTTagCompound.getBoolean("furnace") ? 1 : 0);
		setAmountUpgrade(Upgrade.COLLECTOR, par1NBTTagCompound.getBoolean("suckItems") ? 1 : 0);
		setAmountUpgrade(Upgrade.TICKING, par1NBTTagCompound.getBoolean("ticking") ? 1 : 0);
		player = par1NBTTagCompound.getString("player");
		super.readFromNBT(par1NBTTagCompound);
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		for (Upgrade upgrade : Upgrade.values()) {
			nbt.setInteger(upgrade.toString(), getAmountUpgrade(upgrade));
		}
		nbt.setString("player", player);
	}
	
	private void onUpgradeInserted(EntityPlayer player) {
		player.inventory.mainInventory[player.inventory.currentItem].stackSize -= 1;
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		readFromNBT(nbttagcompound);
		onInventoryChanged();
		
	}
	
	public int getLightValue() {
		return isUpgradeInstalled(Upgrade.LIGHT) ? 15 : 0;
	}
	
	public int getComparatorOutput() {
		
		if (!isUpgradeInstalled(Upgrade.COMPARATOR)) {
			return 0;
		}
		if (isUpgradeInstalled(Upgrade.RAIN)) {
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
		if (! isUpgradeInstalled(Upgrade.PLAYER)) {
			return;
		}
		if (isUseableByPlayer(player)) {
			return;
		}
		player.attackEntityFrom(DamageSource.outOfWorld, 5.0F);
	}
	
	public ItemStack[] getItemUpgrades() {
		int amount = 0;
		for (Upgrade upgrade : Upgrade.values()) {
			if (isUpgradeInstalled(upgrade)) {
				amount++;
			}
		}
		ItemStack[] items = new ItemStack[amount];
		int pos = 0;
		for (Upgrade upgrade : Upgrade.values()) {
			if (!isUpgradeInstalled(upgrade)) {
				continue;
			}
			items[pos] = new ItemStack(BetterChests.upgrade, getAmountUpgrade(upgrade), upgrade.ordinal());
			pos++;
		}
		return items;
	}
	
	private boolean hasEnergy() {
		return isUpgradeInstalled(Upgrade.SOLAR) && (worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord) && worldObj.isDaytime() || new Random().nextFloat() > Reference.Conf.ENERGY_NONDAY);
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
		if (! isUpgradeInstalled(Upgrade.REDSTONE)) {
			return 0;
		}
		if (isUpgradeInstalled(Upgrade.RAIN)) {
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
	
	public boolean isUpgradeInstalled(Upgrade upgrade) {
		return getAmountUpgrade(upgrade) > 0;
	}
	
	private void setAmountUpgrade(Upgrade upgrade, int amount) {
		upgrades.remove(upgrade);
		upgrades.put(upgrade,  amount);
	}
	
}
