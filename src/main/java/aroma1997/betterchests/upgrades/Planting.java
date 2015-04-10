package aroma1997.betterchests.upgrades;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import aroma1997.betterchests.InventoryFilter.BCFilterFilter;
import aroma1997.betterchests.Reference;
import aroma1997.betterchests.Upgrade;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

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
		doBlock(chest, tick, world, item, xcoord, ycoord - 1, zcoord, slot);
	}

	private static void doBlock(IBetterChest chest, int tick, World world,
			ItemStack item, int xcoord, int ycoord, int zcoord, int slot) {
		if (slot == -1) {
			return;
		}
		Block block = world.getBlock(xcoord, ycoord, zcoord);
		if (block == null) {
			return;
		}
		ItemStack itemPlant = chest.getStackInSlot(slot);
		Item pl = itemPlant.getItem();
		boolean hydrate = chest.isUpgradeInstalled(Upgrade.RAIN.getItem())
				&& InvUtil.getFirstItem(chest,
						new ItemStack(Items.water_bucket), true) != null;
		if (block == Blocks.dirt || block == Blocks.grass) {
			Block blockAbove = world.getBlock(xcoord, ycoord + 1, zcoord);
			if (blockAbove == null
					|| blockAbove == Blocks.air
					|| blockAbove instanceof BlockTallGrass
					|| blockAbove instanceof BlockFlower
					|| blockAbove.isReplaceable(world, xcoord, ycoord + 1,
							zcoord)) {
				world.setBlock(xcoord, ycoord, zcoord, Blocks.farmland,
						hydrate ? 10 : 0, 3);
				world.setBlockToAir(xcoord, ycoord + 1, zcoord);
			}
		}
		block = world.getBlock(xcoord, ycoord, zcoord);
		if (block instanceof BlockFarmland) {
			if (world.getBlockMetadata(xcoord, ycoord, zcoord) <= 2 && hydrate) {
				world.setBlock(xcoord, ycoord, zcoord, block, 5, 2);
			}
		}
		if (block.canSustainPlant(world, xcoord, ycoord, zcoord,
				ForgeDirection.UP, (IPlantable) pl)) {

			Block blockAboveNew = world.getBlock(xcoord, ycoord + 1, zcoord);
			if (blockAboveNew == null
					|| blockAboveNew == Blocks.air
					|| blockAboveNew.isReplaceable(world, xcoord, ycoord,
							zcoord)) {
				if (itemPlant == null || itemPlant.stackSize <= 0) {
					return;
				}
				if (Block.getBlockFromItem(pl).canPlaceBlockAt(world, xcoord,
						ycoord + 1, zcoord)) {
					world.setBlock(xcoord, ycoord + 1, zcoord,
							((IPlantable) pl).getPlant(world, xcoord,
									ycoord + 1, zcoord), itemPlant.getItemDamage(), 3);
					chest.decrStackSize(slot, 1);
				}
			}
		}
	}

}
