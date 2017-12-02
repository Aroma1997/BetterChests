package aroma1997.betterchests.integration.ic2;

import java.util.Collection;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import aroma1997.core.inventory.InvUtil;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.planter.IHarvestHandler;
import aroma1997.betterchests.api.planter.IPlantHandler;
import aroma1997.betterchests.upgrades.DummyUpgradeType;

import ic2.api.crops.BaseSeed;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.api.crops.ICropSeed;
import ic2.api.crops.ICropTile;

public class Ic2CropHandler implements IPlantHandler, IHarvestHandler {
	@Override
	public boolean canHandleHarvest(IBlockState state, World world, BlockPos pos) {
		return world.getTileEntity(pos) instanceof ICropTile;
	}

	@Override
	public boolean handleHarvest(IBetterChest chest, IBlockState state, World world, BlockPos pos) {
		ICropTile tile = (ICropTile) world.getTileEntity(pos);
		CropCard cropcard = tile.getCrop();
		if (cropcard == null) {
			return false;
		}
		int harvestSize;
		if (chest.isUpgradeInstalled(DummyUpgradeType.AI.getStack())) {
			harvestSize = cropcard.getOptimalHarvestSize(tile);
		} else {
			harvestSize = cropcard.getMaxSize();
		}

		if (tile.getCurrentSize() >= harvestSize) {
			boolean ret = false;
			for (ItemStack current : tile.performHarvest()) {
				if (InvUtil.putStackInInventoryInternal(current, chest, false).getCount() != current.getCount()) {
					ret = true;
				}
			}
			return ret;
		}

		return false;
	}

	private boolean isValidSeed(ItemStack stack) {
		return Crops.instance.getBaseSeed(stack) != null || stack.getItem() instanceof ICropSeed;
	}

	@Override
	public boolean canHandlePlant(Collection<ItemStack> items, World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof ICropTile)) {
			return false;
		}
		ICropTile crop = (ICropTile) te;
		return crop.getCrop() == null && items.stream().anyMatch(this::isValidSeed);
	}

	@Override
	public boolean handlePlant(IBetterChest chest, Collection<ItemStack> items, World world, BlockPos pos) {
		ICropTile tile = (ICropTile) world.getTileEntity(pos);
		CropCard card;
		int size, growth, gain, resistance, scanned;

		ItemStack stack = items.stream().filter(this::isValidSeed).findFirst().get();

		BaseSeed seed = Crops.instance.getBaseSeed(stack);
		if (seed != null) {
			card = seed.crop;
			size = seed.size;
			growth = seed.statGrowth;
			gain = seed.statGain;
			resistance = seed.statResistance;
			scanned = 0;
		} else if (stack.getItem() instanceof ICropSeed) {
			ICropSeed cropSeed = (ICropSeed) stack.getItem();
			card = cropSeed.getCropFromStack(stack);
			size = 1;
			growth = cropSeed.getGrowthFromStack(stack);
			gain = cropSeed.getGainFromStack(stack);
			resistance = cropSeed.getResistanceFromStack(stack);
			scanned = cropSeed.getScannedFromStack(stack);
		} else {
			throw new IllegalArgumentException();
		}
		tile.reset();
		tile.setCrop(card);
		tile.setCurrentSize(size);
		tile.setStatGrowth(growth);
		tile.setStatGain(gain);
		tile.setStatResistance(resistance);
		tile.setScanLevel(scanned);
		stack.setCount(stack.getCount() - 1);
		return true;
	}
}
