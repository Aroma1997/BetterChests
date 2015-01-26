/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests.upgrades;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import aroma1997.betterchests.EntityBag;
import aroma1997.betterchests.Reference;
import aroma1997.betterchests.Upgrade;
import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

public class Collector extends BasicUpgrade {

	@SuppressWarnings("unchecked")
	@Override
	public void updateChest(IBetterChest inv, int tick, World world,
			ItemStack item) {
		if (tick % 4 == 1) {
			if (inv.getEnergyObject().getCurrent() < Reference.Conf.ENERGY_COLLECTOR)
				return;
			float radius = inv.getAmountUpgrade(Upgrade.COLLECTOR.getItem());
			AxisAlignedBB bounds = AxisAlignedBB.fromBounds(inv.getXPos()
					+ -0.5D, inv.getYPos() + 0.0D, inv.getZPos() + -0.5D,
					inv.getXPos() + 0.5D, inv.getYPos() + 0.0D,
					inv.getZPos() + 0.5D);
			bounds = bounds.expand(radius, radius, radius);
			List<EntityItem> list = world.getEntitiesWithinAABB(
					EntityItem.class, bounds);
			for (EntityItem e : list) {
				if (e.getEntityItem() == null
						|| !UpgradeHelper.isItemAllowed(e.getEntityItem(),
								inv.getFiltersForUpgrade(item))) {
					continue;
				}
				if (e.isEntityAlive()) {
					e.motionX = (inv.getXPos() - e.posX) / 2;
					e.motionY = (inv.getYPos() - e.posY) / 2;
					e.motionZ = (inv.getZPos() - e.posZ) / 2;
					if (world.rand.nextInt(10) == 0) {
						float pitch = 0.85F - world.rand.nextFloat() * 3.0F / 10.0F;
						world.playSoundEffect(e.posX, e.posY, e.posZ,
								"mob.endermen.portal", 0.6F, pitch);
					}
				}
			}

			AxisAlignedBB boundsNew = AxisAlignedBB.fromBounds(inv.getXPos()
					+ -0.5D, inv.getYPos() - 1.0D, inv.getZPos() + -0.5D,
					inv.getXPos() + 0.5D, inv.getYPos() + 0.5D,
					inv.getZPos() + 0.5D);
			boundsNew = boundsNew.expand(0.2D, 0.2D, 0.2D);
			List<EntityItem> listNew = world.getEntitiesWithinAABB(
					EntityItem.class, boundsNew);

			for (EntityItem e : listNew) {
				if (e.isEntityAlive() && !(e instanceof EntityBag)) {
					if (inv.getEnergyObject().getCurrent() < Reference.Conf.ENERGY_COLLECTOR)
						break;
					if (e.getEntityItem() == null
							|| !UpgradeHelper.isItemAllowed(e.getEntityItem(),
									inv.getFiltersForUpgrade(item))) {
						continue;
					}
					ItemStack itemBack = InvUtil.putIntoFirstSlot(inv,
							e.getEntityItem(), false);
					inv.getEnergyObject().setCurrent(
							inv.getEnergyObject().getCurrent()
									- Reference.Conf.ENERGY_COLLECTOR);
					if (itemBack == null) {
						world.removeEntity(e);
					} else {
						e.setEntityItemStack(itemBack);
					}
				}
			}

		}
	}

}
