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
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IInventoryFilter;
import aroma1997.core.client.inventories.SpecialImagesBase.EnergyBar;
import aroma1997.core.inventories.IProgressable;
import aroma1997.core.items.inventory.InventoryItem;
import aroma1997.core.misc.EnergyObject;
import aroma1997.core.util.ItemUtil;
import aroma1997.core.util.ItemUtil.ItemMatchCriteria;

public class BagInventory extends InventoryItem implements IBetterChest,
		IProgressable {

	public BagInventory(ItemStack item) {
		super(item);
	}

	private EnergyObject energy;

	private Entity e;

	private ArrayList<ItemStack> upgrades;

	private long longTick;

	@Override
	protected void init() {
		upgrades = new ArrayList<ItemStack>();
	}

	public void setEntity(Entity e) {
		this.e = e;
	}

	public Entity getEntity() {
		return e;
	}

	@Override
	public int getSizeInventory() {
		return getAmountUpgrade(Upgrade.SLOT.getItem()) * 9 + 27;
	}

	@Override
	public String getName() {
		return item.getDisplayName();
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getProgress(Object object) {

		if (object instanceof EnergyBar) {
			return (float) getEnergyObject().getCurrent()
					/ (float) getEnergyObject().getMax();
		}

		return getProgress();
	}

	@Override
	public float getProgress() {
		return 0;
	}

	@Override
	public double getXPos() {
		return e.posX;
	}

	@Override
	public double getYPos() {
		return e.posY;
	}

	@Override
	public double getZPos() {
		return e.posZ;
	}

	@Override
	public int getXCoord() {
		return e.getPosition().getX();
	}

	@Override
	public int getYCoord() {
		return e.getPosition().getY();
	}

	@Override
	public int getZCoord() {
		return e.getPosition().getZ();
	}

	@Override
	public int getAmountUpgrade(ItemStack upgrade) {
		if (!UpgradeHelper.isUpgrade(upgrade)) {
			return 0;
		}
		for (ItemStack item : upgrades) {
			if (ItemUtil.areItemsSameMatching(item, upgrade,
					ItemMatchCriteria.ID, ItemMatchCriteria.DAMAGE)) {
				return item.stackSize;
			}
		}
		return 0;
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
	public boolean isUpgradeInstalled(ItemStack upgrade) {
		return getAmountUpgrade(upgrade) > 0;
	}

	@Override
	public void setAmountUpgrade(ItemStack upgrade, int amount) {
		setAmountUpgradeWithoutNotify(upgrade, amount);
		markDirty();
	}

	public void setAmountUpgradeWithoutNotify(ItemStack upgrade, int amount) {
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
	public boolean hasEnergy() {
		return false;
	}

	@Override
	public ArrayList<ItemStack> getUpgrades() {
		return upgrades;
	}

	@Override
	public long getLongTick() {
		return longTick;
	}

	@Override
	public boolean isUpgradeDisabled(ItemStack stack) {
		for (ItemStack item : upgrades) {
			if (ItemUtil.areItemsSameMatching(item, stack,
					ItemMatchCriteria.ID, ItemMatchCriteria.DAMAGE)) {
				return item.hasTagCompound()
						&& item.getTagCompound().hasKey("disabled");
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
						item.getTagCompound().setBoolean("disabled", true);
						markDirty();
						return;
					} else {
						if (item.hasTagCompound()
								&& item.getTagCompound().hasKey("disabled")) {
							item.getTagCompound().removeTag("disabled");
							if (item.getTagCompound().hasNoTags()) {
								item.setTagCompound(null);
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
	public List<IInventoryFilter> getFiltersForUpgrade(ItemStack item) {
		ItemStack filter = new ItemStack(BetterChestsItems.filter, 1, 0);
		List<IInventoryFilter> filterlist = new ArrayList<IInventoryFilter>();
		for (ItemStack upgrade : upgrades) {
			if (ItemUtil.areItemsSameMatching(upgrade, filter,
					ItemMatchCriteria.ID, ItemMatchCriteria.DAMAGE)) {
				InventoryFilter inv = ItemFilter.getInventory(upgrade);
				if (inv.matchesUpgrade(item)) {
					filterlist.add(inv);
				}
			}
		}
		return filterlist;
	}

	@Override
	public EnergyObject getEnergyObject() {
		if (energy == null) {
			energy = TileEntityBChest.generateNewEObject();
		}
		return energy;
	}

	public void onUpdate(EntityPlayer player) {
		if (player.worldObj.isRemote) {
			// readFromNBT(item.getTagCompound());
		}
		longTick += 1;
		e = player;
		UpgradeHelper.updateChest(this, (int) (longTick % 64), player.worldObj);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList("upgrades",
				new NBTTagCompound().getId());
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound upgradenbt = list.getCompoundTagAt(i);
			ItemStack item = ItemStack.loadItemStackFromNBT(upgradenbt);
			upgrades.add(item);
		}
		getEnergyObject().readFromNBT(nbt);
		super.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();
		for (ItemStack item : upgrades) {
			NBTTagCompound upgradesbt = new NBTTagCompound();
			item.writeToNBT(upgradesbt);
			list.appendTag(upgradesbt);
		}
		getEnergyObject().saveToNBT(nbt);
		nbt.setTag("upgrades", list);
	}

}
