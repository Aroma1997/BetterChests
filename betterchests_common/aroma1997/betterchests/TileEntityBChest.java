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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import aroma1997.core.client.inventories.GUIContainer;
import aroma1997.core.inventories.ContainerBasic;
import aroma1997.core.misc.FakePlayerFactory;
import aroma1997.core.util.FileUtil;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

public class TileEntityBChest extends TileEntity implements IBetterChest {
	
	private String player;
	
	private int tick;
	
	private EntityPlayer fplayer;
	
	private int ticksSinceSync = - 1;
	
	private HashMap<Upgrade, Integer> upgrades = new HashMap<Upgrade, Integer>();
	
	public TileEntityBChest() {
		player = "";
		tick = new Random().nextInt(64);
		items = new ItemStack[9];
	}
	
	@Override
	public String getInvName() {
		return "inv.betterchests:chest.name";
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot < 0 || slot >= items.length) {
			return null;
		}
		return items[slot];
	}
	
	@Override
	public void validate() {
		super.validate();
		if (! worldObj.isRemote) {
			fplayer = FakePlayerFactory.getFakePlayer(worldObj);
			fplayer.posX = xCoord;
			fplayer.posY = yCoord;
			fplayer.posZ = zCoord;
		}
	}
	
	private boolean firstInit = false;
	
	private int numUsingPlayers;
	
	public float prevLidAngle;
	
	public float lidAngle;
	
	private ItemStack[] items;
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		doNormalChestUpdate();
		if (firstInit) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		if (worldObj.isRemote) {
			return;
		}
		UpgradeHelper.updateChest(this, tick, this.worldObj);
		
		if (tick-- <= 0) {
			tick = 64;
			onInventoryChanged();
			updateBlock(xCoord + 1, yCoord, zCoord);
			updateBlock(xCoord - 1, yCoord, zCoord);
			updateBlock(xCoord, yCoord, zCoord + 1);
			updateBlock(xCoord, yCoord, zCoord - 1);
			updateBlock(xCoord, yCoord + 1, zCoord);
			updateBlock(xCoord, yCoord - 1, zCoord);
		};
		
		if (isUpgradeInstalled(Upgrade.TICKING) && tick % 8 == 0) {
			for (int i = 0; i < getSizeInventory(); i++) {
				ItemStack item = getStackInSlot(i);
				if (item == null || item.getItem() == null) {
					continue;
				}
				fplayer.inventory.mainInventory[0] = getStackInSlot(i);
				fplayer.inventory.onInventoryChanged();
				item.getItem().onUpdate(item, worldObj, fplayer, 0, false);
				onInventoryChanged();
			}
		}
		
		if (isUpgradeInstalled(Upgrade.RAIN) && worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord)
			&& worldObj.isRaining()
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
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		Packet132TileEntityData packet = new Packet132TileEntityData();
		packet.data = nbt;
		packet.xPosition = xCoord;
		packet.yPosition = yCoord;
		packet.zPosition = zCoord;
		packet.actionType = 1;
		return packet;
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}
	
	@Override
	public int getSizeInventory() {
		return getAmountUpgrade(Upgrade.SLOT) * 9 + 9;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		if (! isUpgradeInstalled(Upgrade.PLAYER)) {
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
		
		if (! (getAmountUpgrade(upgrade) >= upgrade.getMaxAmount())) {
			if (upgrade.getRequirement() == null || isUpgradeInstalled(upgrade.getRequirement())) {
				setAmountUpgrade(upgrade, getAmountUpgrade(upgrade) + 1);
				if (upgrade == Upgrade.PLAYER) {
					this.player = player.username;
				}
				onUpgradeInserted(player);
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		for (Upgrade upgrade : Upgrade.values()) {
			setAmountUpgrade(upgrade, nbt.getInteger(upgrade.toString()));
		}
		items = new ItemStack[getSizeInventory()];
		FileUtil.readFromNBT(this, nbt);
		player = nbt.getString("player");
		super.readFromNBT(nbt);
		if (worldObj != null && worldObj.isRemote) {
			worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("player", player);
		FileUtil.writeToNBT(this, nbt);
		for (Upgrade upgrade : Upgrade.values()) {
			nbt.setInteger(upgrade.toString(), getAmountUpgrade(upgrade));
		}
	}
	
	private void onUpgradeInserted(EntityPlayer player) {
		if (! player.capabilities.isCreativeMode) {
			player.inventory.mainInventory[player.inventory.currentItem].stackSize -= 1;
		}
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		readFromNBT(nbttagcompound);
		onInventoryChanged();
		
	}
	
	public int getLightValue() {
		return isUpgradeInstalled(Upgrade.LIGHT) ? 15 : 0;
	}
	
	public int getComparatorOutput() {
		
		if (! isUpgradeInstalled(Upgrade.COMPARATOR)) {
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
		int a = 0;
		for (int i = 0; i < getSizeInventory(); i++) {
			if (getStackInSlot(i) != null) {
				a++;
			}
		}
		int b = 0;
		ItemStack[] item = new ItemStack[a];
		for (int i = 0; i < getSizeInventory(); i++) {
			if (getStackInSlot(i) != null) {
				item[b++] = getStackInSlot(i);
			}
		}
		return item;
	}
	
	@Override
	public boolean hasEnergy() {
		return isUpgradeInstalled(Upgrade.ENERGY);
	}
	
	@Override
	public double getXPos() {
		return xCoord + 0.5F;
	}
	
	@Override
	public double getYPos() {
		return yCoord + 0.5F;
	}
	
	@Override
	public double getZPos() {
		return zCoord + 0.5F;
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
	
	@SuppressWarnings("rawtypes")
	private void doNormalChestUpdate() {
		if (worldObj != null && ! worldObj.isRemote && numUsingPlayers > 0
			&& (ticksSinceSync + xCoord + yCoord + zCoord) % 200 == 0)
		{
			numUsingPlayers = 0;
			float var1 = 5.0F;
			List var2 = worldObj.getEntitiesWithinAABB(
				EntityPlayer.class,
				AxisAlignedBB.getAABBPool().getAABB(xCoord - var1,
					yCoord - var1, zCoord - var1,
					xCoord + 1 + var1,
					yCoord + 1 + var1,
					zCoord + 1 + var1));
			Iterator var3 = var2.iterator();
			
			while (var3.hasNext())
			{
				EntityPlayer var4 = (EntityPlayer) var3.next();
				
				if (var4.openContainer instanceof ContainerBasic)
				{
					++numUsingPlayers;
				}
			}
		}
		
		if (worldObj != null && ! worldObj.isRemote && ticksSinceSync < 0)
		{
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, BetterChests.chest.blockID, 1,
				numUsingPlayers);
		}
		
		ticksSinceSync++;
		prevLidAngle = lidAngle;
		float f = 0.1F;
		if (numUsingPlayers > 0 && lidAngle == 0.0F)
		{
			worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D,
				"random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}
		if (numUsingPlayers <= 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F)
		{
			float f1 = lidAngle;
			if (numUsingPlayers > 0)
			{
				lidAngle += f;
			}
			else
			{
				lidAngle -= f;
			}
			if (lidAngle > 1.0F)
			{
				lidAngle = 1.0F;
			}
			float f2 = 0.5F;
			if (lidAngle < f2 && f1 >= f2)
			{
				worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D,
					"random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
			if (lidAngle < 0.0F)
			{
				lidAngle = 0.0F;
			}
		}
	}
	
	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
	public ItemStack getStackInSlotOnClosing(int i) {
		return items[i];
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		items[i] = itemstack;
	}
	
	@Override
	public boolean isInvNameLocalized() {
		return false;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public void openChest() {
		numUsingPlayers++;
	}
	
	@Override
	public void closeChest() {
		numUsingPlayers--;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	
	@Override
	public boolean receiveClientEvent(int par1, int par2)
	{
		if (par1 == 1)
		{
			numUsingPlayers = par2;
			return true;
		}
		else
		{
			return super.receiveClientEvent(par1, par2);
		}
	}
	
	@Override
	public int getAmountUpgrade(Upgrade upgrade) {
		Integer c = upgrades.get(upgrade);
		if (c != null) {
			return c;
		}
		return 0;
	}
	
	@Override
	public boolean isUpgradeInstalled(Upgrade upgrade) {
		return getAmountUpgrade(upgrade) > 0;
	}
	
	@Override
	public void setAmountUpgrade(Upgrade upgrade, int amount) {
		if (upgrades.containsKey(upgrade)) {
			upgrades.remove(upgrade);
		}
		upgrades.put(upgrade, new Integer(amount));
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
		return new ContainerBasic(player.inventory, this);
	}
	
}
