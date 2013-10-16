package aroma1997.betterchests.inventory;

import aroma1997.core.inventories.ISpecialInventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;


public class Chest {
	
	public Chest(Type type, IInventory inv) {
		this.type = type;
		this.sub = new SubChest(type, inv);
		this.chest = new MainChest(type, this, inv);
	}
	
	private Type type;
	
	private SubChest sub;
	
	private MainChest chest;
	
	public SubChest getSub() {
		return sub;
	}
	
	public MainChest getChest() {
		return chest;
	}
	
	Type getType() {
		return type;
	}
	
	public void update() {
		sub.update();
		chest.update();
	}

	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagCompound subnbt = nbt.getCompoundTag("sub");
		NBTTagCompound chestnbt = nbt.getCompoundTag("chest");
		sub.readFromNBT(subnbt);
		chest.readFromNBT(chestnbt);
	}

	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound subnbt = new NBTTagCompound();
		NBTTagCompound chestnbt = new NBTTagCompound();
		sub.writeToNBT(subnbt);
		chest.writeToNBT(chestnbt);
		nbt.setCompoundTag("sub", subnbt);
		nbt.setCompoundTag("chest", chestnbt);
	}
	
	public void onInventoryChanged() {
		sub.onInventoryChanged();
		chest.onInventoryChanged();
	}

	public ISpecialInventory getInventory(EntityPlayer player, int id) {
		return chest;
	}

	public void openChest() {
		sub.openChest();
		chest.openChest();
	}

	public void closeChest() {
		sub.closeChest();
		chest.closeChest();
	}
	
	public void validate() {
		sub.validate();
		chest.validate();
	}
	
}
