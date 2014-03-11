package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.common.IPlantable;

public class Planting extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		int range = 2 * item.stackSize;
		if (tick == 37) {
			int slot = InvUtil.getFirstItem(chest, IPlantable.class);
			if (slot == -1)
				return;
			for (int x = -range; x <= range; x++) {
				for (int z = -range; z <= range; z++) {
					for (int y = -3; y < 3; y++) {
						int xcoord = (int) chest.getXPos() + x;
						int ycoord = (int) chest.getYPos() + y;
						int zcoord = (int) chest.getZPos() + z;
						Block block = world.getBlock(xcoord, ycoord, zcoord);
						if (block == null)
							continue;
						if (block == Blocks.dirt || block == Blocks.grass) {
							Block blockAbove = world.getBlock(xcoord,
									ycoord + 1, zcoord);
							if (blockAbove == null || blockAbove == Blocks.air
									|| blockAbove instanceof BlockTallGrass
									|| blockAbove instanceof BlockFlower) {
								world.setBlock(xcoord, ycoord, zcoord,
										Blocks.farmland);
								world.setBlockToAir(xcoord, ycoord + 1, zcoord);
							}
						} else if (block instanceof BlockFarmland) {
							Block blockAbove = world.getBlock(xcoord,
									ycoord + 1, zcoord);
							if (blockAbove == null || blockAbove == Blocks.air
									|| blockAbove instanceof BlockTallGrass
									|| blockAbove instanceof BlockFlower) {
								ItemStack itemPlant = chest
										.getStackInSlot(slot);
								if (itemPlant == null
										|| itemPlant.stackSize <= 0)
									return;
								IPlantable pl = (IPlantable) itemPlant
										.getItem();
								if (Block.getBlockFromItem((Item) pl)
										.canPlaceBlockAt(world, xcoord,
												ycoord + 1, zcoord)) {
									world.setBlock(xcoord, ycoord + 1, zcoord,
											pl.getPlant(world, xcoord,
													ycoord + 1, zcoord), pl
													.getPlantMetadata(world,
															xcoord, ycoord,
															zcoord), 3);
									chest.decrStackSize(slot, 1);
								}
							}
						}
					}
				}
			}
		}
	}

}
