/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.upgrades;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import aroma1997.betterchests.Reference;
import aroma1997.betterchests.Upgrade;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.coremod.CoreMod;

public class Killing extends BasicUpgrade {

	@Override
	public void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item) {
		if (chest.getLongTick() % 128 == 100
				&& chest.getEnergyObject().getCurrent() >= Reference.Conf.ENERGY_KILLING) {
			AxisAlignedBB bounds = AxisAlignedBB.fromBounds(chest.getXPos()
					- Reference.Conf.FEED_RADIUS / 2, chest.getYPos()
					- Reference.Conf.FEED_HEIGHT / 2, chest.getZPos()
					- Reference.Conf.FEED_RADIUS / 2, chest.getXPos()
					+ Reference.Conf.FEED_RADIUS / 2, chest.getYPos()
					+ Reference.Conf.FEED_HEIGHT / 2, chest.getZPos()
					+ Reference.Conf.FEED_RADIUS / 2);
			List<EntityLiving> entities = world.getEntitiesWithinAABB(
					EntityLiving.class, bounds);
			Map<Class<? extends EntityLiving>, Integer> map = new HashMap<Class<? extends EntityLiving>, Integer>();
			boolean ai = chest.isUpgradeInstalled(Upgrade.AI.getItem());
			boolean didSomething = false;
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
					} else {
						map.put(e.getClass(), 1);
					}
					if (map.get(e.getClass()) > 2) {
						if (ai) {
							ReflectionHelper
									.setPrivateValue(
											EntityLivingBase.class,
											e,
											100,
											CoreMod.runtimeDeobfuscationEnabled ? "field_70718_bc"
													: "recentlyHit");
						}
						didSomething = true;
						e.attackEntityFrom(DamageSource.generic, 20.0F);
					}
				} else {
					if (ai) {
						ReflectionHelper
								.setPrivateValue(
										EntityLivingBase.class,
										e,
										100,
										CoreMod.runtimeDeobfuscationEnabled ? "field_70718_bc"
												: "recentlyHit");
					}
					didSomething = true;
					e.attackEntityFrom(DamageSource.generic, 20.0F);
				}
			}
			if (didSomething) {
				chest.getEnergyObject().setCurrent(
						chest.getEnergyObject().getCurrent()
								- Reference.Conf.ENERGY_KILLING);
			}
		}
	}

}
