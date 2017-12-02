package aroma1997.betterchests.inventories;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.StreamSupport;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.FMLCommonHandler;

import aroma1997.core.container.ContainerBase;
import aroma1997.core.inventory.IInventoryPartContainer;
import aroma1997.core.inventory.inventorypart.InventoryPartBase;
import aroma1997.core.network.AutoEncode;
import aroma1997.core.network.packets.PacketTeUpdate;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.betterchests.container.ContainerUpgrades;

public class InventoryPartUpgrades extends InventoryPartBase {

	private static final int size = 16;
	@AutoEncode
	private boolean[] disabled = new boolean[size];

	public InventoryPartUpgrades(IInventoryPartContainer container) {
		super(container, "betterchests:inventorypart.upgrades", size, false, false);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return stack.getItem() instanceof IUpgrade && ((IUpgrade)stack.getItem()).canBePutInChest(getChest(), stack)
				&& ((IUpgrade)stack.getItem()).areRequirementsMet(getChest(), stack);
	}

	private int getIndex(ItemStack stack) {
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack currentStack = getStackInSlot(i);
			if (currentStack == stack) {
				return i;
			}
		}
		throw new IllegalArgumentException("cannot get index of stack, if stack is not in the inventory");
	}

	public boolean isUpgradeDisabled(ItemStack stack) {
		return disabled[getIndex(stack)];
	}

	public void setUpgradeDisabled(ItemStack stack, boolean value) {
		if (((IUpgrade)stack.getItem()).canBeDisabled(stack)) {
			disabled[getIndex(stack)] = value;
			markDirty();
		}
	}

	protected IUpgradableBlockInternal getChest() {
		return (IUpgradableBlockInternal) container;
	}

	@Override
	public void markDirty() {
		super.markDirty();

		for (int i = 0; i < getSizeInventory(); i++) {
			if (getStackInSlot(i).isEmpty()) {
				disabled[i] = false;
			}
		}

		if (getChest().getWorldObj().isRemote) return;
		for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
			if (player.openContainer != null && player.openContainer instanceof ContainerBase && ((ContainerBase<?>)player.openContainer).inventory == container && !(player.openContainer instanceof ContainerUpgrades)) {
				player.closeScreen();
			}
		}

		if (getChest() instanceof TileEntity) {
			new PacketTeUpdate((TileEntity)getChest()).sendPacket();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	public Iterator<ItemStack> getActiveUpgrades() {
		return StreamSupport.stream(spliterator(), false).filter(stack -> !isUpgradeDisabled(stack)).iterator();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		Arrays.fill(disabled, false);
		NBTTagList list = nbt.getTagList("disabled", NBT.TAG_INT);
		for (int i = 0; i < list.tagCount(); i++) {
			disabled[((NBTTagInt)list.get(i)).getInt()] = true;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);
		NBTTagList disabledList = new NBTTagList();
		for (int i = 0; i < disabled.length; i++) {
			if (disabled[i]) {
				disabledList.appendTag(new NBTTagInt(i));
			}
		}
		nbt.setTag("disabled", disabledList);
		return nbt;
	}

	public void tick() {
		for (ItemStack upgrade : (Iterable<ItemStack>)this::getActiveUpgrades) {
			try {
				((IUpgrade)upgrade.getItem()).update((IUpgradableBlock) container, upgrade);
			} catch (Exception e) {
				e.printStackTrace();
				setUpgradeDisabled(upgrade, true);
			}
		}
	}
}
