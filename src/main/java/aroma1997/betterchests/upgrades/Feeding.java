package aroma1997.betterchests.upgrades;

import java.util.List;

import aroma1997.betterchests.Reference;
import aroma1997.betterchests.api.IBetterChest;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class Feeding extends BasicUpgrade {

	@SuppressWarnings("rawtypes")
	@Override
	public void updateChest(IBetterChest inv, int tick, World world,
			ItemStack item) {
		if (tick != 16 || world.isRemote) {
			return;
		}
		AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(inv.getXPos()
				- Reference.Conf.FEED_RADIUS / 2, inv.getYPos()
				- Reference.Conf.FEED_HEIGHT / 2, inv.getZPos()
				- Reference.Conf.FEED_RADIUS / 2, inv.getXPos()
				+ Reference.Conf.FEED_RADIUS / 2, inv.getYPos()
				+ Reference.Conf.FEED_HEIGHT / 2, inv.getZPos()
				+ Reference.Conf.FEED_RADIUS / 2);
		List list = world.getEntitiesWithinAABB(EntityAnimal.class, bounds);
		if (list == null || list.size() >= Reference.Conf.FEED_ENTITIES_TO_STOP) {
			return;
		}
		for (Object obj : list) {
			if (obj == null || !(obj instanceof EntityAnimal)) {
				continue;
			}
			EntityAnimal e = (EntityAnimal) obj;
			if (!e.isEntityAlive() || e.isChild()) {
				continue;
			}
			int slot = -1;
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack feedingItem = inv.getStackInSlot(i);
				if (feedingItem == null) {
					continue;
				}
				if (e.isBreedingItem(feedingItem)) {
					slot = i;
					break;
				}
			}
			if (slot == -1) {
				continue;
			}
			if (e.getGrowingAge() == 0 && !e.isInLove()) {
				e.func_146082_f(null);

				inv.decrStackSize(slot, 1);
			}
		}
	}

}
