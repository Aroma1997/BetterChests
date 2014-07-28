package aroma1997.betterchests.upgrades;

import aroma1997.betterchests.EntityBag;
import aroma1997.betterchests.Upgrade;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.util.InvUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

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
				if (e.age >= 10 && e.isEntityAlive()) {
					e.motionX = (inv.getXPos() - e.posX) / 2;
					e.motionY = (inv.getYPos() - e.posY) / 2;
					e.motionZ = (inv.getZPos() - e.posZ) / 2;
					if (world.rand.nextInt(10) == 0) {
						float pitch = 0.85F - world.rand.nextFloat() * 3.0F / 10.0F;
						world.playSoundEffect(e.posX, e.posY, e.posZ, "mob.endermen.portal", 0.6F,
						        pitch);
					}
				}
			}
			
			AxisAlignedBB boundsNew = AxisAlignedBB.getBoundingBox(inv.getXPos() + - 0.5D,
			        inv.getYPos() - 1.0D, inv.getZPos() + - 0.5D, inv.getXPos() + 0.5D,
			        inv.getYPos() + 0.5D, inv.getZPos() + 0.5D);
			boundsNew = boundsNew.expand(0.2D, 0.2D, 0.2D);
			List<EntityItem> listNew = world.getEntitiesWithinAABB(EntityItem.class, boundsNew);
			
			for (EntityItem e : listNew) {
				if (e.age > 10 && e.isEntityAlive() && !(e instanceof EntityBag)) {
					if (e.getEntityItem() == null) {
						continue;
					}
					ItemStack itemBack = InvUtil.putIntoFirstSlot(inv, e.getEntityItem(), false);
					if (itemBack == null) {
						world.removeEntity(e);
					}
					else {
						e.setEntityItemStack(itemBack);
					}
				}
			}
			
		}
	}
	
}
