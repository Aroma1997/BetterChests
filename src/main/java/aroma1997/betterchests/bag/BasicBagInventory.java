package aroma1997.betterchests.bag;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import aroma1997.core.inventory.ItemInventory;
import aroma1997.core.network.AutoEncode.GuiEncode;
import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.IMobileUpgradableBlock;
import aroma1997.betterchests.inventories.IUpgradableBlockInternal;
import aroma1997.betterchests.inventories.InventoryPartFilter;
import aroma1997.betterchests.inventories.InventoryPartUpgrades;

public abstract class BasicBagInventory extends ItemInventory implements IMobileUpgradableBlock, IUpgradableBlockInternal, IEnergyStorage {

	private final InventoryPartUpgrades upgradeInv;
	private final InventoryPartFilter filterInv;

	private final IEnergyStorage external = new IEnergyStorage() {
		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return BasicBagInventory.this.receiveEnergy(maxReceive, simulate);
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			return 0;
		}

		@Override
		public int getEnergyStored() {
			return energy;
		}

		@Override
		public int getMaxEnergyStored() {
			return BasicBagInventory.this.getMaxEnergyStored();
		}

		@Override
		public boolean canExtract() {
			return false;
		}

		@Override
		public boolean canReceive() {
			return true;
		}
	};

	@GuiEncode
	private int energy;

	public BasicBagInventory(ItemStack stack) {
		super(stack);
		upgradeInv = new InventoryPartUpgrades(this);
		filterInv = new InventoryPartFilter(this);
	}

	@Override
	public Vec3d getPositionPrecise() {
		if (getEntity() != null) {
			return getEntity().getPositionVector();
		}
		return null;
	}

	@Override
	public BlockPos getPosition() {
		if (getEntity() != null) {
			return getEntity().getPosition();
		}
		return null;
	}

	@Override
	public World getWorldObj() {
		if (getEntity() != null) {
			return getEntity().world;
		}
		return null;
	}

	@Override
	public InventoryPartUpgrades getUpgradePart() {
		return upgradeInv;
	}

	@Override
	public InventoryPartFilter getFilterPart() {
		return filterInv;
	}

	@Override
	public IEnergyStorage getEnergyStorage() {
		return this;
	}

	@Override
	public int getTickCount() {
		return (int) getWorldObj().getWorldTime();
	}

	@Override
	public EntityPlayerMP getFakePlayer() {
		return null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return (T) external;
		}
		return null;
	}

	@Override
	protected void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		energy = compound.getInteger("energy");
	}

	@Override
	protected NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setInteger("energy", energy);
		return compound;
	}

	//ENERGY

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int ret = Math.max(Math.min(getMaxEnergyStored() - energy, maxReceive), 0);

		if (!simulate) {
			energy += ret;
			markDirty();
		}

		return ret;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int ret = Math.max(Math.min(energy, maxExtract), 0);
		if (!simulate) {
			energy -= ret;
			markDirty();
		}
		return ret;
	}

	@Override
	public int getEnergyStored() {
		return Math.min(energy, getMaxEnergyStored());
	}

	@Override
	public int getMaxEnergyStored() {
		return UpgradeHelper.INSTANCE.getPowerCapacity(this);
	}

	@Override
	public boolean canExtract() {
		return true;
	}

	@Override
	public boolean canReceive() {
		return true;
	}
}
