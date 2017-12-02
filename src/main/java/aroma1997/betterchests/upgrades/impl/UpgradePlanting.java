package aroma1997.betterchests.upgrades.impl;

import java.util.Collection;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import aroma1997.core.inventory.InvUtil;
import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.api.planter.IPlantHandler;
import aroma1997.betterchests.api.planter.PlantHarvestRegistry;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradePlanting extends BasicUpgrade {
	public UpgradePlanting() {
		super(true, 5, UpgradableBlockType.CHEST.array);
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (!UpgradeHelper.INSTANCE.isFirstUpgrade(chest, stack) || chest.getWorldObj().isRemote || !hasUpgradeOperationCost(chest)) {
			return;
		}
		int range = chest.getAmountUpgrades(stack) * 4 + 1;
		int states = UpgradeHelper.INSTANCE.getFrequencyTick(chest, stack, range * range);
		int xOffset = states % range - range / 2;
		int zOffset = states / range - range / 2;

		World world = chest.getWorldObj();
		BlockPos pos = chest.getPosition().add(xOffset, 0, zOffset);
		if (pos.equals(chest.getPosition())) {
			return;
		}
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock().isReplaceable(world, pos)) {
			world.setBlockToAir(pos);
		}
		plantBlock((IBetterChest) chest, pos, stack);
	}

	private void plantBlock(IBetterChest chest, BlockPos pos, ItemStack upgrade) {
		World world = chest.getWorldObj();
		IBlockState state = world.getBlockState(pos);

		IFilter filter = chest.getFilterFor(upgrade);
		Collection<ItemStack> availableItems = Lists.newArrayList(InvUtil.getInvStream(chest).filter(filter::matchesStack)::iterator);
		for (IPlantHandler handler : PlantHarvestRegistry.INSTANCE.getPlantHandlers()) {
			if (handler.canHandlePlant(availableItems, world, pos, state)) {
				if (handler.handlePlant(chest, availableItems, world, pos)) {
					drawUpgradeOperationCode(chest);
				}
				return;
			}
		}
	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energyPlanting;
	}
}
