package aroma1997.betterchests.upgrades.impl.plant;

import java.util.Collection;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import aroma1997.betterchests.api.IBetterChest;

public class PumpkinHandler extends CropHandler {
	@Override
	public boolean canHandleHarvest(IBlockState state, World world, BlockPos pos) {
		return state.getBlock() == Blocks.MELON_BLOCK || state.getBlock() == Blocks.PUMPKIN;
	}

	@Override
	public boolean handleHarvest(IBetterChest chest, IBlockState state, World world, BlockPos pos) {
		PlantHarvestHelper.breakBlockHandleDrop(world, pos, state, chest);
		return true;
	}

	private boolean matches(ItemStack stack) {
		return stack.getItem() == Items.PUMPKIN_SEEDS || stack.getItem() == Items.MELON_SEEDS;
	}

	@Override
	public boolean canHandlePlant(Collection<ItemStack> items, World world, BlockPos pos, IBlockState state) {
		return items.stream().anyMatch(this::matches) && super.canHandlePlant(Lists.newArrayList(items.stream().filter(this::matches).iterator()), world, pos, state);
	}

	@Override
	public boolean handlePlant(IBetterChest chest, Collection<ItemStack> items, World world, BlockPos pos) {
		BlockPos diff = pos.subtract(chest.getPosition());
		if ((diff.getX() - diff.getZ()) % 2 == 0) {
			super.handlePlant(chest, Lists.newArrayList(items.stream().filter(this::matches).iterator()), world, pos);
			return true;
		}
		return false;
	}

	@Override
	public int getPlantPriority() {
		return 10;
	}
}
