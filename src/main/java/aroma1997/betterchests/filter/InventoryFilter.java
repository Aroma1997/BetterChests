package aroma1997.betterchests.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.inventory.ItemInventory;
import aroma1997.core.inventory.inventorypart.InventoryPartBase;
import aroma1997.core.inventory.inventorypart.InventoryPartClass;
import aroma1997.core.network.ClientSettable;
import aroma1997.core.util.ItemUtil;
import aroma1997.core.util.ItemUtil.IItemMatchCriteria;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.betterchests.client.gui.GuiFilter;
import aroma1997.betterchests.container.ContainerFilter;

public class InventoryFilter extends ItemInventory {

	public final InventoryPartBase filterInv;
	public final InventoryPartBase upgradeInv;

	@ClientSettable
	private boolean checknbt;
	@ClientSettable
	private boolean checkdamage;
	@ClientSettable
	boolean isBlacklist;

	public InventoryFilter(ItemStack stack) {
		super(stack);
		filterInv = new InventoryPartBase(this, "betterchests:inventorypart.filter", 9, false, false);
		upgradeInv = new InventoryPartClass(this, "betterchests:inventorypart.upgrade", 1, false, false, IUpgrade.class);
		load();
	}

	@Override
	public Container getContainer(EntityPlayer player, short id) {
		return new ContainerFilter(this, player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Gui getGui(EntityPlayer player, short id) {
		return new GuiFilter(new ContainerFilter(this, player));
	}

	public boolean appliesToUpgrade(ItemStack stack) {
		return upgradeInv.isEmpty() || ItemUtil.areItemsSameMatchingIdDamage(stack, upgradeInv.get());
	}

	public boolean isAffected(ItemStack stack) {
		IItemMatchCriteria[] crits = getMatchCriteria();
		return StreamSupport.stream(filterInv.spliterator(), false).anyMatch(other -> ItemUtil.areItemsSameMatching(stack, other, crits));
	}

	IItemMatchCriteria[] getMatchCriteria() {
		List<IItemMatchCriteria> criteria = new ArrayList<>(3);
		criteria.add(IItemMatchCriteria.ID);
		if (checkdamage) {
			criteria.add(IItemMatchCriteria.DAMAGE);
		}
		if (checknbt) {
			criteria.add(IItemMatchCriteria.NBT);
		}
		return criteria.toArray(new ItemUtil.IItemMatchCriteria[0]);
	}

	public boolean isBlacklist() {
		return isBlacklist;
	}

	@Override
	protected void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		checkdamage = compound.getBoolean("checkdamage");
		checknbt = compound.getBoolean("checknbt");
		isBlacklist = compound.getBoolean("isBlacklist");
	}

	@Override
	protected NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setBoolean("checknbt", checknbt);
		compound.setBoolean("checkdamage", checkdamage);
		compound.setBoolean("isBlacklist", isBlacklist);
		return compound;
	}
}
