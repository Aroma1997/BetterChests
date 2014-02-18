
package aroma1997.betterchests.upgrades;


import java.util.List;

import aroma1997.betterchests.Upgrade;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class Collector extends BasicUpgrade {
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateChest(IBetterChest inv, int tick, World world, ItemStack item) {
		if (inv.isUpgradeInstalled(Upgrade.COLLECTOR.getItem())) {
			float radius = inv.getAmountUpgrade(Upgrade.COLLECTOR.getItem());
			AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(inv.getXPos() + - 0.5D,
				inv.getYPos() + 0.0D, inv.getZPos() + - 0.5D, inv.getXPos() + 0.5D,
				inv.getYPos() + 0.0D, inv.getZPos() + 0.5D);
			bounds = bounds.expand(radius, radius, radius);
			List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, bounds);
			for (EntityItem e : list) {
				if (e.getEntityItem() == null) {
					continue;
				}
				if (e.age >= 10 && e.isEntityAlive())
				{
					ItemStack itemBack = InvUtil.putIntoFirstSlot(inv, e.getEntityItem(), false);
					e.motionX = inv.getXPos() - e.posX;
					e.motionY = inv.getYPos() - e.posY;
					e.motionZ = inv.getZPos() - e.posZ;
					if (itemBack == null) {
						world.removeEntity(e);
						// e.setDead();
					}
					else {
						e.setEntityItemStack(itemBack);
					}
					if (world.rand.nextInt(10) == 0)
					{
						float pitch = 0.85F - world.rand.nextFloat() * 3.0F / 10.0F;
						world.playSoundEffect(e.posX, e.posY, e.posZ, "mob.endermen.portal", 0.6F,
							pitch);
					}
				}
			}
		}
	}
	
}
