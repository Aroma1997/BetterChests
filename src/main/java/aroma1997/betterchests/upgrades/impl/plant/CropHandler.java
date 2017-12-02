package aroma1997.betterchests.upgrades.impl.plant;

import java.util.Collection;
import java.util.stream.Stream;

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.IPlantable;

import aroma1997.core.inventory.InvUtil;
import aroma1997.core.util.WorldUtil;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.planter.IHarvestHandler;
import aroma1997.betterchests.api.planter.IPlantHandler;

public class CropHandler implements IPlantHandler, IHarvestHandler {
	@Override
	public boolean canHandleHarvest(IBlockState state, World world, BlockPos pos) {
		return state.getBlock() instanceof IGrowable;
	}

	@Override
	public boolean handleHarvest(IBetterChest chest, IBlockState state, World world, BlockPos pos) {
		IGrowable growable = (IGrowable) state.getBlock();
		if (!growable.canGrow(world, pos, state, chest.getWorldObj().isRemote)) {
			PlantHarvestHelper.breakBlockHandleDrop(world, pos, state, chest);
			return true;
		}
		return false;
	}

	@Override
	public boolean canHandlePlant(Collection<ItemStack> items, World world, BlockPos pos, IBlockState state) {
		if (WorldUtil.isBlockAir(state)) {
			return !getStackToPlant(items.stream(), world, pos).isEmpty();
		}
		return false;
	}

	@Override
	public boolean handlePlant(IBetterChest chest, Collection<ItemStack> items, World world, BlockPos pos) {
		ItemStack toPlant = getStackToPlant(items.stream(), world, pos);
		IPlantable plantable = (IPlantable) toPlant.getItem();
		IBlockState current = world.getBlockState(pos.down());
		if (current.getBlock().canSustainPlant(current, world, pos, EnumFacing.UP, plantable) || makeFarmland(world, pos, chest, false)) {

			world.setBlockState(pos, plantable.getPlant(world, pos));
			toPlant.setCount(toPlant.getCount() - 1);
			return true;
		}
		return false;
	}

	private ItemStack getStackToPlant(Stream<ItemStack> items, World world, BlockPos pos) {
		BlockPos posToCheck = pos.down();
		IBlockState other = world.getBlockState(posToCheck);
		return items.filter(stack -> stack.getItem() instanceof IPlantable).filter(stack -> {
			IPlantable plant = (IPlantable) stack.getItem();
			return other.getBlock().canSustainPlant(other, world, posToCheck, EnumFacing.UP, plant)
					|| Blocks.FARMLAND.canSustainPlant(Blocks.FARMLAND.getDefaultState(), world, posToCheck, EnumFacing.UP, plant);
		}).findFirst().orElse(ItemStack.EMPTY);
	}

	static boolean makeFarmland(World world, BlockPos pos, IBetterChest chest, boolean simulate) {
		BlockPos below = pos.down();
		IBlockState blockBelow = world.getBlockState(below);
		boolean farmland = false;
		if (blockBelow.getBlock() == Blocks.DIRT || blockBelow.getBlock() == Blocks.GRASS) {
			int hoe = InvUtil.findInInvInternal(chest, null, test -> test.getItem() instanceof ItemHoe);
			if (hoe != -1) {
				farmland = true;
				if (!simulate) {
					ItemStack tool = chest.getStackInSlot(hoe);
					tool.attemptDamageItem(1, world.rand, null);
					if (tool.getItemDamage() > tool.getMaxDamage()) {
						tool.setItemDamage(0);
						tool.setCount(tool.getCount() - 1);
					}

					world.setBlockState(below, Blocks.FARMLAND.getDefaultState());
				}
			}
		}

		return farmland;
	}

	@Override
	public int getHarvestPriority() {
		return -20;
	}

	@Override
	public int getPlantPriority() {
		return -20;
	}
}
