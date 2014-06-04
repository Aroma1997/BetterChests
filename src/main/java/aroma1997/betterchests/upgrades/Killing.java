package aroma1997.betterchests.upgrades;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import aroma1997.betterchests.Reference;
import aroma1997.betterchests.api.IBetterChest;

public class Killing extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		if (chest.getLongTick() % 128 == 100) {
			AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(chest.getXPos()
			        - Reference.Conf.FEED_RADIUS / 2, chest.getYPos() - Reference.Conf.FEED_HEIGHT / 2,
			        chest.getZPos() - Reference.Conf.FEED_RADIUS / 2, chest.getXPos()
			                + Reference.Conf.FEED_RADIUS / 2, chest.getYPos()
			                + Reference.Conf.FEED_HEIGHT / 2, chest.getZPos()
			                + Reference.Conf.FEED_RADIUS / 2);
			List<EntityLiving> entities = world.getEntitiesWithinAABB(EntityLiving.class, bounds);
			Map<Class<? extends EntityLiving>, Integer> map = new HashMap<Class<? extends EntityLiving>, Integer>();
			for (EntityLiving e : entities) {
				if (e == null || !e.isEntityAlive()) {
					continue;
				}
				if (e instanceof EntityAnimal) {
					if (e.isChild()) {
						continue;
					}
					if (map.containsKey(e.getClass())) {
						map.put(e.getClass(), map.remove(e.getClass()) + 1);
					}
					else {
						map.put(e.getClass(), 1);
					}
					if (map.get(e.getClass()) > 2) {
						e.attackEntityFrom(DamageSource.generic, 20.0F);
					}
				}
				else {
					e.attackEntityFrom(DamageSource.generic, 20.0F);
				}
			}
		}
	}

}
