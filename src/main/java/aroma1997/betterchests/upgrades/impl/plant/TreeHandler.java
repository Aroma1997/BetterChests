package aroma1997.betterchests.upgrades.impl.plant;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

import net.minecraftforge.oredict.OreDictionary;

import aroma1997.core.util.ItemUtil;
import aroma1997.core.util.ItemUtil.IItemMatchCriteria;
import aroma1997.core.util.LazyInitializer;
import aroma1997.core.util.WorldUtil;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.planter.IHarvestHandler;
import aroma1997.betterchests.api.planter.IPlantHandler;

public class TreeHandler implements IHarvestHandler, IPlantHandler {

	private static final int MAX_DEPTH = 40;

	private Supplier<Set<Block>> acceptedBlocks = new LazyInitializer<>(() -> {
		Set<Block> blocks = new HashSet<>();
		blocks.addAll(getBlocksMatching("logWood"));
		blocks.addAll(getBlocksMatching("treeLeaves"));
		return blocks;
	});

	protected static Set<Block> getBlocksMatching(String name) {
		Set<Block> set = new HashSet<>();
		for (ItemStack stack : OreDictionary.getOres(name)) {
			Block block = Block.getBlockFromItem(stack.getItem());
			if (block != Blocks.AIR) {
				set.add(block);
			}
		}
		return set;
	}

	@Override
	public boolean canHandleHarvest(IBlockState state, World world, BlockPos pos) {
		return canBreak(world, pos);
	}

	@Override
	public boolean handleHarvest(IBetterChest chest, IBlockState state, World world, BlockPos pos) {
		MutableBlockPos start = new MutableBlockPos(pos);
		while (canBreak(world, start)) {
			start.move(EnumFacing.UP);
		}
		start.move(EnumFacing.DOWN);

		if (start.getY() >= pos.getY()) {
			BlockPos target = search(world, pos);
			IBlockState targetState = world.getBlockState(target);
			targetState.getBlock().breakBlock(world, pos, state);
			PlantHarvestHelper.breakBlockHandleDrop(world, target, targetState, chest);
			return true;
		}
		return false;
	}

	protected BlockPos search(World world, BlockPos pos) {
		HashSet<BlockPos> visited = new HashSet<>();
		visited.add(pos);

		BlockPos currentPos = pos;
		for (int i = 0; i < MAX_DEPTH; i++) {
			boolean isMatching = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (dir == EnumFacing.DOWN) {
					//If we allow downwards, we will end at tree roots.
					continue;
				}
				BlockPos newPos = currentPos.offset(dir);
				if (!visited.contains(newPos) && canBreak(world, newPos)) {
					isMatching = true;
					visited.add(newPos);
					currentPos = newPos;
					break;
				}
			}
			if (!isMatching) {
				break;
			}
		}

		return currentPos;
	}

	protected boolean canBreak(World world, BlockPos pos) {
		return acceptedBlocks.get().contains(world.getBlockState(pos).getBlock());
	}

	protected boolean isAcceptedSapling(ItemStack stack) {
		for (ItemStack other : OreDictionary.getOres("treeSapling")) {
			if (ItemUtil.areItemsSameMatching(stack, other, IItemMatchCriteria.ID, IItemMatchCriteria.WILDCARD)) {
				return true;
			}
		}
		return false;

	}

	@Override
	public boolean canHandlePlant(Collection<ItemStack> items, World world, BlockPos pos, IBlockState state) {
		if (!WorldUtil.isBlockAir(state)) {
			return false;
		}
		Optional<ItemStack> sapling = items.stream().filter(this::isAcceptedSapling).filter(stack -> world.mayPlace(Block.getBlockFromItem(stack.getItem()), pos, true, EnumFacing.UP, null)).findFirst();
		return sapling.isPresent();
	}

	@Override
	public boolean handlePlant(IBetterChest chest, Collection<ItemStack> items, World world, BlockPos pos) {
		ItemStack sapling = items.stream().filter(this::isAcceptedSapling).filter(stack -> world.mayPlace(Block.getBlockFromItem(stack.getItem()), pos, true, EnumFacing.UP, null)).findFirst().get();

		world.setBlockState(pos, Block.getBlockFromItem(sapling.getItem()).getStateForPlacement(world, pos, EnumFacing.UP, 0, 0, 0, sapling.getMetadata(), null));
		sapling.setCount(sapling.getCount() - 1);
		return true;
	}
}
