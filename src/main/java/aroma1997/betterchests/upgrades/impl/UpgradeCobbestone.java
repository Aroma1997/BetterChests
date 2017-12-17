package aroma1997.betterchests.upgrades.impl;

import java.util.function.Predicate;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import aroma1997.core.inventory.InvUtil;
import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradeCobbestone extends BasicUpgrade {

	public static final Predicate<ItemStack> water = getFluidPredicate(FluidRegistry.WATER);
	public static final Predicate<ItemStack> lava = getFluidPredicate(FluidRegistry.LAVA);

	public UpgradeCobbestone() {
		super(true, 1, UpgradableBlockType.NORMAL_INVENTORIES);
	}

	@Override
	public void update(IUpgradableBlock block, ItemStack stack) {
		if (UpgradeHelper.INSTANCE.getFrequencyTick(block, stack, 8) != 0) {
			return;
		}
		IBetterChest chest = (IBetterChest) block;
		if (hasUpgradeOperationCost(chest) && InvUtil.getInvStream(chest).anyMatch(water) && InvUtil.getInvStream(chest).anyMatch(lava)) {
			if (InvUtil.putStackInInventoryFirst(new ItemStack(Blocks.COBBLESTONE), chest, true, false, false, null).isEmpty()) {
				drawUpgradeOperationCode(chest);
				chest.markDirty();
			}
		}
	}

	public static Predicate<ItemStack> getFluidPredicate(Fluid fluid) {
		return new Predicate<ItemStack>() {
			@Override
			public boolean test(ItemStack itemStack) {
				IFluidHandlerItem handler = FluidUtil.getFluidHandler(itemStack);
				if (handler == null) {
					return false;
				}
				for (IFluidTankProperties properties : handler.getTankProperties()) {
					FluidStack stack = properties.getContents();
					if (stack != null && stack.getFluid() == fluid) {
						return true;
					}
				}
				return false;
			}
		};
	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energyCobblestone;
	}
}
