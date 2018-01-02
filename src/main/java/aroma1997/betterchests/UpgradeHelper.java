package aroma1997.betterchests;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import aroma1997.core.util.ItemUtil;
import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.betterchests.bag.ItemBBag;
import aroma1997.betterchests.bag.ItemBPortableBarrel;

public enum UpgradeHelper implements aroma1997.betterchests.api.UpgradeHelper.IUpgradeHelper {
	INSTANCE;

	public int intSumInBounds(IUpgradableBlock block, ChestModifier modifier, int lower, int higher) {
		return inBounds(intSum(block, modifier), lower, higher);
	}

	public int intSum(IUpgradableBlock block, ChestModifier modifier) {
		int i = 0;
		for (ItemStack stack : block.getActiveUpgrades()) {
			Number num = ((IUpgrade)stack.getItem()).getChestModifier(block, modifier, stack);
			if (num != null) {
				i += num.intValue();
			}
		}
		return i;
	}

	public int getPowerCapacity(IUpgradableBlock block) {
		return intSum(block, ChestModifier.ENERGY_CAPACITY) + 50000;
	}

	public int intSumCapped(IUpgradableBlock block, ChestModifier modifier, int min, int max) {
		int i = inBounds(0, min, max);
		for (ItemStack stack : block.getActiveUpgrades()) {
			Number num = ((IUpgrade)stack.getItem()).getChestModifier(block, modifier, stack);
			if (num != null) {
				i = inBounds(i + num.intValue(), min, max);
			}
		}
		return i;
	}

	public int intSumFirst(IUpgradableBlock block, ChestModifier modifier, int defaultValue) {
		for (ItemStack stack : block.getActiveUpgrades()) {
			Number num = ((IUpgrade)stack.getItem()).getChestModifier(block, modifier, stack);
			if (num != null) {
				return num.intValue();
			}
		}
		return defaultValue;
	}

	private int inBounds(int current, int lower, int upper) {
		return Math.max(Math.min(upper, current), lower);
	}

	public boolean booleanSum(IUpgradableBlock block, ChestModifier modifier, boolean defaultValue) {
		for (ItemStack stack : block.getActiveUpgrades()) {
			Number num = ((IUpgrade)stack.getItem()).getChestModifier(block, modifier, stack);
			if (num != null) {
				return num.doubleValue() > 0;
			}
		}
		return defaultValue;
	}

	@Override
	public boolean isFirstUpgrade(IUpgradableBlock block, ItemStack stack) {
		for (ItemStack current : block.getActiveUpgrades()) {
			if (stack == current) {
				return true;
			} else if (ItemUtil.areItemsSameMatchingIdDamage(current, stack)) {
				return false;
			}
		}
		return false;
	}

	@Override
	public int getFrequencyTick(IUpgradableBlock block, ItemStack stack, int frequency) {
		int num = stack.getItem().hashCode() + stack.getMetadata() + block.getTickCount();
		num = num % frequency;
		if (num < 0) {
			num += frequency;
		}
		return num;
	}

	@Override
	public IUpgradableBlock getInventory(ItemStack stack, Entity containerEntity) {
		if (stack.getItem() instanceof ItemBBag) {
			return BlocksItemsBetterChests.betterbag.getInventoryFor(stack, containerEntity);
		} else if (stack.getItem() instanceof ItemBPortableBarrel) {
			return BlocksItemsBetterChests.betterportablebarrel.getInventoryFor(stack, containerEntity);
		} else {
			return null;
		}
	}

	@Override
	public IUpgradableBlock getInventory(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof IUpgradableBlock) {
			return (IUpgradableBlock) te;
		}
		return null;
	}
}
