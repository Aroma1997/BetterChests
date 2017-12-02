package aroma1997.betterchests.upgrades.impl;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.api.planter.IHarvestHandler;
import aroma1997.betterchests.api.planter.PlantHarvestRegistry;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradeHarvesting extends BasicUpgrade {

	public UpgradeHarvesting() {
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
		int yOffset = states / range - range / 2;

		BlockPos pos = chest.getPosition().add(xOffset, 0, yOffset);
		harvestBlock((IBetterChest) chest, pos);
	}

	private void harvestBlock(IBetterChest chest, BlockPos pos) {
		World world = chest.getWorldObj();
		IBlockState state = world.getBlockState(pos);
		for (IHarvestHandler handler : PlantHarvestRegistry.INSTANCE.getHarvestHandlers()) {
			if (handler.canHandleHarvest(state, world, pos)) {
				if (handler.handleHarvest(chest, state, world, pos)) {
					drawUpgradeOperationCode(chest);
				}
				break;
			}
		}
	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energyHarvesting;
	}
}
