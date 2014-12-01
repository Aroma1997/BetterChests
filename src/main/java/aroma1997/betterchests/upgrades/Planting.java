package aroma1997.betterchests.upgrades;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import aroma1997.betterchests.InventoryFilter.BCFilterFilter;
import aroma1997.betterchests.Reference;
import aroma1997.betterchests.Upgrade;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;
import aroma1997.core.util.WorldUtil;

public class Planting extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		int range = Reference.Conf.PLANTS_RANGE_MULTIPLIER * item.stackSize;
		int doubleRange = range * 2 + 1;

		int num = (int) (chest.getLongTick() % (doubleRange * doubleRange * 2));
		if (num >= doubleRange * doubleRange)
			return;
		int xcoord = chest.getXCoord() + num / doubleRange - range;
		int zcoord = chest.getZCoord() + num % doubleRange - range;
		int ycoord = chest.getYCoord();
		int slot = InvUtil.getFirstItem(chest, IPlantable.class, null, new BCFilterFilter(chest.getFiltersForUpgrade(item)));
		doBlock(chest, tick, world, item, new BlockPos(xcoord, ycoord - 1, zcoord), slot);
	}

	private static void doBlock(IBetterChest chest, int tick, World world,
			ItemStack item, BlockPos pos, int slot) {
		if (slot == -1) {
			return;
		}
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block == null) {
			return;
		}
		BlockPos posUp = pos.offset(EnumFacing.UP);
		ItemStack itemPlant = chest.getStackInSlot(slot);
		Item pl = itemPlant.getItem();
		boolean hydrate = chest.isUpgradeInstalled(Upgrade.RAIN.getItem())
				&& InvUtil.getFirstItem(chest,
						new ItemStack(Items.water_bucket), true) != null;
		if (block == Blocks.dirt || block == Blocks.grass) {
			IBlockState blockAbove = world.getBlockState(posUp);
			if (WorldUtil.isBlockAir(world, posUp)
					|| blockAbove instanceof BlockTallGrass
					|| blockAbove instanceof BlockFlower
					|| blockAbove.getBlock().isReplaceable(world, posUp)) {
				world.setBlockState(pos, Blocks.farmland.getStateFromMeta(hydrate ? 10 : 0));
				world.setBlockToAir(posUp);
			}
		}
		state = world.getBlockState(pos);
		block = state.getBlock();
		if (block instanceof BlockFarmland) {
			if (Blocks.farmland.getMetaFromState(state) <= 2 && hydrate) {
				world.setBlockState(pos, Blocks.farmland.getStateFromMeta(10), 2);
			}
		}
		if (block.canSustainPlant(world, pos,
				EnumFacing.UP, (IPlantable) pl)) {
			
			IBlockState stateNew = world.getBlockState(posUp);
			Block blockAboveNew = stateNew.getBlock();
			if (WorldUtil.isBlockAir(blockAboveNew)
					|| blockAboveNew.isReplaceable(world, posUp)) {
				if (itemPlant == null || itemPlant.stackSize <= 0) {
					return;
				}
				IBlockState plant = ((IPlantable)pl).getPlant(world, posUp);
				if (plant.getBlock().canPlaceBlockAt(world, posUp)) {
					world.setBlockState(posUp,
							plant, 3);
					chest.decrStackSize(slot, 1);
				}
			}
		}
	}

}
