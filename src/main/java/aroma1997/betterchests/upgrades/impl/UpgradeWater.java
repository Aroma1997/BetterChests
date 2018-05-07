package aroma1997.betterchests.upgrades.impl;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import aroma1997.core.util.ItemUtil;
import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IBetterTank;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradeWater extends BasicUpgrade {

	private final FluidStack water = new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);

	public UpgradeWater() {
		super(true, 1, new UpgradableBlockType[]{ UpgradableBlockType.CHEST, UpgradableBlockType.BAG, UpgradableBlockType.TANK});
	}


	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (UpgradeHelper.INSTANCE.getFrequencyTick(chest, stack, 16) != 0 || !hasUpgradeOperationCost(chest)) {
			return;
		}

		if (chest instanceof IBetterChest) {
			IBetterChest inv = (IBetterChest) chest;
			int fluidGotten = 0;
			int containerSlot = -1;
			for (int slot : inv.getSlotsForFace(null)) {
				ItemStack current = inv.getStackInSlot(slot);
				current = current.copy();
				current.setCount(1);

				IFluidHandlerItem handler = FluidUtil.getFluidHandler(current);
				if (handler != null) {
					FluidStack gotten = handler.drain(water, false);
					if (gotten != null) {
						assert gotten.getFluid() == water.getFluid();
						fluidGotten += gotten.amount;
					} else {
						if (containerSlot == -1 && handler.fill(water, false) >= 1000) {
							containerSlot = slot;
						}
					}
				}
			}

			if (fluidGotten >= 2 * Fluid.BUCKET_VOLUME && containerSlot != -1) {
				ItemStack current = inv.getStackInSlot(containerSlot);
				current = current.copy();
				current.setCount(1);
				IFluidHandlerItem handler = FluidUtil.getFluidHandler(current);
				handler.fill(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), true);
				current = handler.getContainer();

				int maxStackSize = Math.min(inv.getInventoryStackLimit(), current.getMaxStackSize());

				for (int slot : inv.getSlotsForFace(null)) {
					ItemStack other = inv.getStackInSlot(slot);
					if (other.isEmpty()) {
						inv.setInventorySlotContents(slot, current);
					} else if (other.getCount() + 1 <= maxStackSize && ItemUtil.areItemsSameMatchingIdDamageNbt(other, current)) {
						other.setCount(other.getCount() + 1);
					} else {
						continue;
					}
					inv.decrStackSize(containerSlot, 1);
					drawUpgradeOperationCode(chest);
					inv.markDirty();
					break;
				}
			}
		} else if (chest instanceof IBetterTank) {
			FluidTank tank = ((IBetterTank)chest).getTank();
			if (tank.getFluidAmount() >= 2000 && tank.getFluid().getFluid() == FluidRegistry.WATER) {
				tank.fill(new FluidStack(FluidRegistry.WATER, 1000), true);
			}
		}

	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energyWater;
	}
}
