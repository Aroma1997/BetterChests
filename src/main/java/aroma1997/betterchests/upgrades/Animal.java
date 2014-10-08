package aroma1997.betterchests.upgrades;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import aroma1997.betterchests.Reference;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

public class Animal extends BasicUpgrade {

	@SuppressWarnings("unchecked")
	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		if (tick == 63) {
			AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(chest.getXPos()
					- Reference.Conf.FEED_RADIUS / 2, chest.getYPos()
					- Reference.Conf.FEED_HEIGHT / 2, chest.getZPos()
					- Reference.Conf.FEED_RADIUS / 2, chest.getXPos()
					+ Reference.Conf.FEED_RADIUS / 2, chest.getYPos()
					+ Reference.Conf.FEED_HEIGHT / 2, chest.getZPos()
					+ Reference.Conf.FEED_RADIUS / 2);
			List<EntityAnimal> list = world.getEntitiesWithinAABB(
					EntityAnimal.class, bounds);

			for (EntityAnimal entity : list) {
				if (entity == null || !entity.isEntityAlive()
						|| entity.isChild())
					continue;
				if (entity instanceof IShearable
						&& !(entity instanceof EntityMooshroom)) {
					IShearable sheep = (IShearable) entity;
					if (sheep.isShearable(null, world, (int) entity.posX,
							(int) entity.posY, (int) entity.posZ)) {
						// If I ever implement that it uses Shears, then this
						// would be the place to damage it.
						ArrayList<ItemStack> items = sheep.onSheared(null,
								world, (int) entity.posX, (int) entity.posY,
								(int) entity.posZ, 1);
						for (ItemStack wool : items) {
							if (InvUtil.putIntoFirstSlot(chest, wool, false) != null)
								break;
						}
					}
				} else if (entity instanceof EntityCow) {
					ItemStack bucket = InvUtil.getFirstItem(chest,
							new ItemStack(Items.bucket), true);
					if (bucket != null) {
						if (InvUtil.putIntoFirstSlot(chest, new ItemStack(
								Items.milk_bucket), false) == null) {
							InvUtil.getFirstItem(chest, new ItemStack(
									Items.bucket), false);
						}
					}
					if (entity instanceof EntityMooshroom) {
						ItemStack bowl = InvUtil.getFirstItem(chest,
								new ItemStack(Items.bowl), true);
						if (bowl != null) {
							if (InvUtil.putIntoFirstSlot(chest, new ItemStack(
									Items.mushroom_stew), false) == null) {
								InvUtil.getFirstItem(chest, new ItemStack(
										Items.bowl), false);
							}
						}
					}
				}
			}
		}
	}

}
