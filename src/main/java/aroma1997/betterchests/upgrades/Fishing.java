package aroma1997.betterchests.upgrades;

import java.util.Random;

import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;
import aroma1997.betterchests.Reference;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

public class Fishing extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		if (chest.getLongTick() % 1024 == 579) {
			int f = InvUtil.getFirstItem(chest, ItemFishingRod.class);
			if (f != -1) {
				for (int x = -Reference.Conf.FISHING_RANGE; x <= Reference.Conf.FISHING_RANGE; x++) {
					for (int z = -Reference.Conf.FISHING_RANGE; z <= Reference.Conf.FISHING_RANGE; z++) {
						if (world.getBlock(chest.getXCoord() + x, chest.getYCoord() - 1, chest.getZCoord() + z) != Blocks.water) return;
					}
				}
				ItemStack fish = ((WeightedRandomFishable)WeightedRandom.getRandomItem(new Random(), EntityFishHook.field_146036_f)).func_150708_a(new Random());
				
				if (fish != null && InvUtil.putIntoFirstSlot(chest, fish, true) == null) {
					InvUtil.putIntoFirstSlot(chest, fish, false);
					ItemStack fishing = chest.getStackInSlot(f);
					fishing.setItemDamage(fishing.getItemDamage() + 1);
					if (fishing.getItemDamage()  > fishing.getMaxDamage()) {
						chest.setInventorySlotContents(f, null);
					}
				}
			}
		}
	}

}
