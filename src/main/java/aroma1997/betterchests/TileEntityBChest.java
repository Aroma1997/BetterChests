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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.betterchests.client.EventListenerClient;
import aroma1997.core.client.inventories.GUIContainer;
import aroma1997.core.inventories.AromaContainer;
import aroma1997.core.inventories.ContainerBasic;
import aroma1997.core.inventories.ISpecialInventory;
import aroma1997.core.inventories.Inventories;
import aroma1997.core.items.wrench.IAromaWrenchable;
import aroma1997.core.util.FileUtil;
import aroma1997.core.util.ItemUtil;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityBChest extends TileEntity implements IBetterChest, ISpecialInventory, IAromaWrenchable {
	
	String player;
	
	private int tick;
	
	private EntityPlayer fplayer;
	
	private int ticksSinceSync = - 1;
	
	boolean pickedUp = false;
	
	private ArrayList<ItemStack> upgrades = new ArrayList<ItemStack>();
	
	public TileEntityBChest() {
		player = "";
		tick = new Random().nextInt(64);
		items = new ItemStack[9];
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
			fplayer = FakePlayerFactory.get((WorldServer) worldObj, new GameProfile("", "Aroma1997BetterChests"));
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
		UpgradeHelper.updateChest(this, tick, worldObj);
		
		if (tick-- <= 0) {
			tick = 64;
			markDirty();
		};
		
		if (isUpgradeInstalled(Upgrade.TICKING.getItem()) && tick % 8 == 0) {
			for (int i = 0; i < getSizeInventory(); i++) {
				ItemStack item = getStackInSlot(i);
				if (item == null || item.getItem() == null) {
					continue;
				}
				fplayer.inventory.mainInventory[0] = getStackInSlot(i);
				fplayer.inventory.markDirty();
				item.getItem().onUpdate(item, worldObj, fplayer, 0, false);
				markDirty();
			}
		}
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, worldObj.provider.dimensionId, nbt);
		return packet;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
	}
	
	@Override
	public int getSizeInventory() {
		return getAmountUpgrade(Upgrade.SLOT.getItem()) * 9 + 27;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		if (! isUpgradeInstalled(Upgrade.PLAYER.getItem()) || par1EntityPlayer == null) {
			return true;
		}
		if (par1EntityPlayer.worldObj.isRemote) {
			return false;
		}
		
		if (! MinecraftServer.getServer().isDedicatedServer()
			&& par1EntityPlayer.getCommandSenderName().equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getCommandSenderName())) {
			return true;
		}
		if (MinecraftServer.getServer().getConfigurationManager().getOps().contains(
			par1EntityPlayer.getCommandSenderName().toLowerCase())
			|| player.equalsIgnoreCase(par1EntityPlayer.getCommandSenderName())) {
			return true;
		}
		return false;
		
	}
	
	public boolean upgrade(EntityPlayer player) {
		if (player == null || ! isUseableByPlayer(player)) {
			return false;
		}
		
		ItemStack itemUpgrade = player.getHeldItem();
		if (itemUpgrade == null || ! UpgradeHelper.isUpgrade(itemUpgrade)) {
			return false;
		}
		
		if (! UpgradeHelper.areRequirementsInstalled(this, itemUpgrade)) {
			return false;
		}
		
		IUpgrade upgrade = (IUpgrade) itemUpgrade.getItem();
		
		if (upgrade.canChestTakeUpgrade(itemUpgrade)
			&& UpgradeHelper.areRequirementsInstalled(this, itemUpgrade)
			&& upgrade.getMaxUpgrades(itemUpgrade) > getAmountUpgrade(itemUpgrade)) {
			setAmountUpgrade(itemUpgrade, getAmountUpgrade(itemUpgrade) + 1);
			if (ItemUtil.areItemsSame(itemUpgrade, Upgrade.PLAYER.getItem())) {
				this.player = player.getCommandSenderName();
			}
			onUpgradeInserted(player);
			return true;
		}
		return false;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		upgrades.clear();
		for (Upgrade upgrade : Upgrade.values()) {
			int amount = nbt.getInteger(upgrade.toString());
			if (amount == 0) {
				continue;
			}
			setAmountUpgrade(upgrade.getItem(), amount);
		}
		NBTTagList list = nbt.getTagList("upgrades", new NBTTagCompound().getId());
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound upgradenbt = (NBTTagCompound) list.getCompoundTagAt(i);
			ItemStack item = ItemStack.loadItemStackFromNBT(upgradenbt);
			upgrades.add(item);
		}
		items = new ItemStack[getSizeInventory()];
		FileUtil.readFromNBT(this, nbt);
		player = nbt.getString("player");
		super.readFromNBT(nbt);
		if (worldObj != null && worldObj.isRemote) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("player", player);
		FileUtil.writeToNBT(this, nbt);
		NBTTagList list = new NBTTagList();
		for (ItemStack item : upgrades) {
			NBTTagCompound upgradesbt = new NBTTagCompound();
			item.writeToNBT(upgradesbt);
			list.appendTag(upgradesbt);
		}
		nbt.setTag("upgrades", list);
	}
	
	private void onUpgradeInserted(EntityPlayer player) {
		if (! player.capabilities.isCreativeMode) {
			player.inventory.mainInventory[player.inventory.currentItem].stackSize -= 1;
		}
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		readFromNBT(nbttagcompound);
		markDirty();
		
	}
	
	public int getLightValue() {
		return isUpgradeInstalled(Upgrade.LIGHT.getItem()) ? 15 : 0;
	}
	
	public int getComparatorOutput() {
		
		if (! isUpgradeInstalled(Upgrade.COMPARATOR.getItem())) {
			return 0;
		}
		return Container.calcRedstoneFromInventory(this);
	}
	
	public ItemStack[] getItems() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for (int i = 0; i < getSizeInventory(); i++) {
			if (getStackInSlot(i) != null) {
				list.add(getStackInSlot(i));
			}
		}
		return list.toArray(new ItemStack[list.size()]);
	}
	
	@Override
	public boolean hasEnergy() {
		return isUpgradeInstalled(Upgrade.ENERGY.getItem());
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
		return MathHelper.clamp_int(numUsingPlayers, 0, 15);
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
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, BetterChests.chest, 1,
				numUsingPlayers);
		}
		
		ticksSinceSync++;
		prevLidAngle = lidAngle;
		float f = 0.1F;
		if (numUsingPlayers > 0 && lidAngle == 0.0F)
		{
			worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "betterchests:chest.bchestclose" , 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
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
					"betterchests:chest.bchestopen", 0.5F,
					worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
			if (lidAngle < 0.0F)
			{
				lidAngle = 0.0F;
			}
		}
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
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
	public ItemStack getStackInSlotOnClosing(int i) {
		if (i < 0 || i >= items.length || isUpgradeInstalled(Upgrade.VOID.getItem())) {
			return null;
		}
		return items[i];
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (i < 0 || i >= items.length || isUpgradeInstalled(Upgrade.VOID.getItem())) {
			return;
		}
		items[i] = itemstack;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
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
	public Slot getSlot(int slot, int index, int x, int y) {
		return new Slot(this, index, x, y);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void drawGuiContainerForegroundLayer(GUIContainer gui, ContainerBasic container,
		int par1, int par2) {
		for (ItemStack item : upgrades) {
			if (!UpgradeHelper.isUpgrade(item)) {
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
	
	public ItemStack getDroppedFullItem() {
		ItemStack item = new ItemStack(BetterChests.chest);
		item.setTagCompound(new NBTTagCompound());
		writeToNBT(item.stackTagCompound);
		return item;
	}
	
	public static TileEntityBChest loadTEFromNBT(NBTTagCompound nbt) {
		TileEntityBChest te = new TileEntityBChest();
		te.readFromNBT(nbt);
		return te;
	}
	
	@Override
	public AromaContainer getContainer(EntityPlayer player, int i) {
		if (i == Inventories.ID_GUI_BLOCK) {
			return new ContainerBasic(player.inventory, this);
		}
		else {
			return new ContainerUpgrades(this, player);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<ItemStack> getUpgrades() {
		return (ArrayList<ItemStack>) upgrades.clone();
	}

	@Override
	public String getInventoryName() {
		return "inv.betterchests:chest.name";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void openInventory() {
		numUsingPlayers++;
	}

	@Override
	public void closeInventory() {
		numUsingPlayers--;
	}

	@Override
	public boolean onWrenchUsed(ItemStack wrench, EntityPlayer player,
			ForgeDirection side) {
		Inventories.openContainerTileEntity(player, this, false);
		return false;
	}

	@Override
	public boolean canPickup(ItemStack wrench, EntityPlayer player,
			ForgeDirection side) {
		return true;
	}
	
}
