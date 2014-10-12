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

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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
import aroma1997.core.client.inventories.GUIContainer;
import aroma1997.core.inventories.AromaContainer;
import aroma1997.core.inventories.AromaSlot;
import aroma1997.core.inventories.ContainerBasic;
import aroma1997.core.inventories.IAdvancedInventory;
import aroma1997.core.inventories.ISpecialInventory;
import aroma1997.core.inventories.Inventories;
import aroma1997.core.items.wrench.IAromaWrenchable;
import aroma1997.core.util.FileUtil;
import aroma1997.core.util.ItemUtil;
import aroma1997.core.util.ItemUtil.ItemMatchCriteria;
import aroma1997.core.util.ServerUtil;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityBChest extends TileEntity implements IBetterChest,
		ISpecialInventory, IAromaWrenchable, IAdvancedInventory,
		ISidedInventory {

	private String player;

	private int tick;

	private long longTick;

	public EntityPlayer fplayer;

	private int ticksSinceSync = -1;

	private ArrayList<ItemStack> upgrades = new ArrayList<ItemStack>();

	public TileEntityBChest() {
		tick = new Random().nextInt(64);
		items = new ItemStack[27];
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot < 0 || slot >= items.length) {
			return null;
		}
		return items[slot];
	}

	private boolean firstTick = true;

	private int numUsingPlayers;

	public float prevLidAngle;

	public float lidAngle;

	private ItemStack[] items;

	@Override
	public void updateEntity() {
		super.updateEntity();
		doNormalChestUpdate();
		if (firstTick) {
			if (!worldObj.isRemote) {
				fplayer = FakePlayerFactory.get((WorldServer) worldObj,
						new GameProfile(null, "Aroma1997BetterChests"));
				fplayer.posX = xCoord;
				fplayer.posY = yCoord;
				fplayer.posZ = zCoord;
			}
			firstTick = false;
		}
		if (worldObj.isRemote) {
			return;
		}
		UpgradeHelper.updateChest(this, tick, worldObj);

		longTick++;
		if (tick-- <= 0) {
			tick = 64;
			markDirty();
		}
		;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(
				xCoord, yCoord, zCoord, worldObj.provider.dimensionId, nbt);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net,
			S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
	}

	@Override
	public int getSizeInventory() {
		return getAmountUpgrade(Upgrade.SLOT.getItem()) * 9 + 27;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		if (!isUpgradeInstalled(Upgrade.PLAYER.getItem())
				|| par1EntityPlayer == null) {
			return true;
		}
		if (par1EntityPlayer.worldObj.isRemote) {
			return false;
		}

		if (!MinecraftServer.getServer().isDedicatedServer()
				&& par1EntityPlayer.getCommandSenderName().equalsIgnoreCase(
						Minecraft.getMinecraft().thePlayer
								.getCommandSenderName())) {
			return true;
		}
		if (ServerUtil.isPlayerAdmin(par1EntityPlayer) || player != null
				&& player.equals(par1EntityPlayer.getUniqueID())) {
			return true;
		}
		return false;

	}

	public boolean upgrade(EntityPlayer player) {
		if (player == null || !isUseableByPlayer(player) || numUsingPlayers > 0) {
			return false;
		}

		ItemStack itemUpgrade = player.getHeldItem();
		if (itemUpgrade == null || !UpgradeHelper.isUpgrade(itemUpgrade)) {
			return false;
		}

		if (!UpgradeHelper.areRequirementsInstalled(this, itemUpgrade)) {
			return false;
		}

		IUpgrade upgrade = (IUpgrade) itemUpgrade.getItem();

		if (UpgradeHelper.canUpgradeGoInChest(this, itemUpgrade)) {
			setAmountUpgrade(itemUpgrade, getAmountUpgradeExact(itemUpgrade) + 1);
			if (ItemUtil.areItemsSameMatching(itemUpgrade,
					Upgrade.PLAYER.getItem(), ItemMatchCriteria.ID,
					ItemMatchCriteria.DAMAGE)) {
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
		NBTTagList list = nbt.getTagList("upgrades",
				new NBTTagCompound().getId());
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound upgradenbt = list.getCompoundTagAt(i);
			ItemStack item = ItemStack.loadItemStackFromNBT(upgradenbt);
			upgrades.add(item);
		}
		items = new ItemStack[getSizeInventory()];
		FileUtil.readFromNBT(this, nbt);
		if (nbt.getTag("player") != null
				&& nbt.getTag("player") instanceof NBTTagString) {
			player = nbt.getString("player");
		}
		super.readFromNBT(nbt);
		if (worldObj != null && worldObj.isRemote) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (player != null) {
			nbt.setString("playerUUID", player.toString());
		}
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
		if (!player.capabilities.isCreativeMode) {
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

		if (!isUpgradeInstalled(Upgrade.COMPARATOR.getItem())) {
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
		return isUpgradeInstalled(Upgrade.REDSTONE.getItem()) ? MathHelper
				.clamp_int(numUsingPlayers, 0, 15) : 0;
	}

	@SuppressWarnings("rawtypes")
	private void doNormalChestUpdate() {
		++ticksSinceSync;
		float f;

		if (!worldObj.isRemote && numUsingPlayers != 0
				&& (ticksSinceSync + xCoord + yCoord + zCoord) % 200 == 0) {
			numUsingPlayers = 0;
			f = 5.0F;
			List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class,
					AxisAlignedBB
							.getBoundingBox(xCoord - f, yCoord - f, zCoord - f,
									xCoord + 1 + f, yCoord + 1 + f, zCoord + 1
											+ f));
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				EntityPlayer entityplayer = (EntityPlayer) iterator.next();

				if (entityplayer.openContainer instanceof ContainerBasic) {
					IInventory iinventory = ((ContainerBasic) entityplayer.openContainer).inv;

					if (iinventory == this) {
						++numUsingPlayers;
					}
				}
			}
		}

		prevLidAngle = lidAngle;
		f = 0.1F;
		double d2;

		if (numUsingPlayers > 0 && lidAngle == 0.0F) {
			double d1 = xCoord + 0.5D;
			d2 = zCoord + 0.5D;

			worldObj.playSoundEffect(d1, yCoord + 0.5D, d2,
					"betterchests:chest.bchestopen", 0.5F,
					worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0
				&& lidAngle < 1.0F) {
			float f1 = lidAngle;

			if (numUsingPlayers > 0) {
				lidAngle += f;
			} else {
				lidAngle -= f;
			}

			if (lidAngle > 1.0F) {
				lidAngle = 1.0F;
			}

			float f2 = 0.5F;

			if (lidAngle < f2 && f1 >= f2) {
				d2 = xCoord + 0.5D;
				double d0 = zCoord + 0.5D;

				worldObj.playSoundEffect(d2, yCoord + 0.5D, d0,
						"betterchests:chest.bchestclose", 0.5F,
						worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (lidAngle < 0.0F) {
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
	public ItemStack decrStackSize(int par1, int par2) {
		if (items[par1] != null) {
			ItemStack itemstack;

			if (items[par1].stackSize <= par2) {
				itemstack = items[par1];
				items[par1] = null;
				markDirty();
				return itemstack;
			} else {
				itemstack = items[par1].splitStack(par2);

				if (items[par1].stackSize == 0) {
					items[par1] = null;
				}

				markDirty();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (i < 0 || i >= items.length
				|| isUpgradeInstalled(Upgrade.VOID.getItem())) {
			return null;
		}
		return items[i];
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		setStackInSlotWithoutNotify(i, itemstack);
		markDirty();
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
	public boolean receiveClientEvent(int par1, int par2) {
		if (par1 == 1) {
			numUsingPlayers = par2;
			return true;
		} else {
			return super.receiveClientEvent(par1, par2);
		}
	}

	@Override
	public int getAmountUpgrade(ItemStack upgrade) {
		if (!UpgradeHelper.isUpgrade(upgrade)) {
			return 0;
		}
		int num = 0;
		for (ItemStack item : upgrades) {
			if (ItemUtil.areItemsSameMatching(item, upgrade,
					ItemMatchCriteria.ID, ItemMatchCriteria.DAMAGE)) {
				num += item.stackSize;
			}
		}
		return num;
	}

	@Override
	public boolean isUpgradeInstalled(ItemStack upgrade) {
		return getAmountUpgrade(upgrade) > 0;
	}

	@Override
	public void setAmountUpgrade(ItemStack upgrade, int amount) {
		for (ItemStack item : upgrades) {
			if (ItemUtil.areItemsSameMatching(item, upgrade,
					ItemMatchCriteria.ID, ItemMatchCriteria.DAMAGE,
					UpgradeHelper.BC_NBT)) {
				if (amount <= 0) {
					upgrades.remove(item);
					return;
				} else {
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
		return new AromaSlot(this, index, x, y);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawGuiContainerForegroundLayer(GUIContainer gui,
			ContainerBasic container, int par1, int par2) {
		for (ItemStack item : upgrades) {
			if (!UpgradeHelper.isUpgrade(item)) {
				continue;
			}
			((IUpgrade) item.getItem()).drawGuiContainerForegroundLayer(gui,
					container, par1, par2, item);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawGuiContainerBackgroundLayer(GUIContainer gui,
			ContainerBasic container, float f, int i, int j) {

	}

	public ItemStack getDroppedFullItem() {
		ItemStack item = new ItemStack(BetterChestsItems.chest);
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
		} else {
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
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1,
				numUsingPlayers);
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord,
				getBlockType());
	}

	@Override
	public void closeInventory() {
		numUsingPlayers--;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1,
				numUsingPlayers);
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord,
				getBlockType());
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord,
				getBlockType());
	}

	@Override
	public boolean onWrenchUsed(ItemStack wrench, EntityPlayer player,
			ForgeDirection side) {
		Inventories.openContainerTileEntity(player, this, false);
		return true;
	}

	@Override
	public boolean canPickup(ItemStack wrench, EntityPlayer player,
			ForgeDirection side) {
		return true;
	}

	@Override
	public int getAmountUpgradeExact(ItemStack upgrade) {
		if (!UpgradeHelper.isUpgrade(upgrade)) {
			return 0;
		}
		for (ItemStack item : upgrades) {
			if (ItemUtil.areItemsSameMatching(item, upgrade,
					ItemMatchCriteria.ID, ItemMatchCriteria.DAMAGE,
					UpgradeHelper.BC_NBT)) {
				return item.stackSize;
			}
		}
		return 0;
	}

	@Override
	public long getLongTick() {
		return longTick;
	}

	@Override
	public int getXCoord() {
		return xCoord;
	}

	@Override
	public int getYCoord() {
		return yCoord;
	}

	@Override
	public int getZCoord() {
		return zCoord;
	}

	@Override
	public ItemStack shouldBeExact() {
		return null;
	}

	@Override
	public boolean isUpgradeDisabled(ItemStack stack) {
		for (ItemStack item : upgrades) {
			if (ItemUtil.areItemsSameMatching(item, stack,
					ItemMatchCriteria.ID, ItemMatchCriteria.DAMAGE)) {
				return item.hasTagCompound()
						&& item.stackTagCompound.hasKey("disabled");
			}
		}
		return false;
	}

	@Override
	public void setUpgradeDisabled(ItemStack stack, boolean value) {
		if (isUpgradeInstalled(stack)) {
			for (ItemStack item : upgrades) {
				if (ItemUtil.areItemsSameMatching(item, stack,
						ItemMatchCriteria.ID, ItemMatchCriteria.DAMAGE)) {
					if (value) {
						if (!item.hasTagCompound()) {
							item.setTagCompound(new NBTTagCompound());
						}
						item.stackTagCompound.setBoolean("disabled", true);
						markDirty();
						return;
					} else {
						if (item.hasTagCompound()
								&& item.stackTagCompound.hasKey("disabled")) {
							item.stackTagCompound.removeTag("disabled");
							if (item.stackTagCompound.hasNoTags()) {
								item.stackTagCompound = null;
							}
						}
						markDirty();
						return;
					}
				}
			}
		}
	}

	@Override
	public void setStackInSlotWithoutNotify(int i, ItemStack itemstack) {

		if (i < 0 || i >= items.length
				|| isUpgradeInstalled(Upgrade.VOID.getItem())) {
			return;
		}
		items[i] = itemstack;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		int red = getAmountUpgrade(Upgrade.BLOCKER.getItem()) * 9;
		int[] slots = new int[getSizeInventory() - red];
		for (int i = 0; i < slots.length; i++) {
			slots[i] = i + red;
		}
		return slots;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return slot >= getAmountUpgrade(Upgrade.BLOCKER.getItem()) * 9;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return canInsertItem(slot, stack, side);
	}

	@Override
	public List<InventoryFilter> getFiltersForUpgrade(ItemStack item) {
		ItemStack filter = new ItemStack(BetterChestsItems.filter, 1, 0);
		List<InventoryFilter> filterlist = new ArrayList<InventoryFilter>();
		for (ItemStack upgrade : upgrades) {
			if (ItemUtil.areItemsSameMatching(upgrade, filter,
					ItemMatchCriteria.ID)) {
				InventoryFilter inv = ItemFilter.getInventory(upgrade);
				if (inv.matchesUpgrade(item)) {
					filterlist.add(inv);
				}
			}
		}
		return filterlist;
	}

}
