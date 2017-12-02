package aroma1997.betterchests.upgrades.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;

import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradeBreeding extends BasicUpgrade {

	static final double RADIUS = 3;

	public UpgradeBreeding() {
		super(true, 1, UpgradableBlockType.CHEST.array);
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (UpgradeHelper.INSTANCE.getFrequencyTick(chest, stack, 200) != 0) {
			return;
		}
		IBetterChest inv = (IBetterChest) chest;

		IFilter filter = ((IBetterChest) chest).getFilterFor(stack);

		AxisAlignedBB bb = new AxisAlignedBB(chest.getPosition()).grow(RADIUS);
		List<EntityAnimal> list = chest.getWorldObj().getEntitiesWithinAABB(EntityAnimal.class, bb, this::isEntityOk);
		if (list.size() > 50 || !hasUpgradeOperationCost(chest)) {
			//Stop breeding, when too many animals are there.
			return;
		}
		list = new LinkedList<>(list);

		for (int i : inv.getSlotsForFace(null)) {
			ItemStack breedingItem = inv.getStackInSlot(i);
			if (!filter.matchesStack(breedingItem)) {
				continue;
			}
			for (Iterator<EntityAnimal> iter = list.iterator(); iter.hasNext() && !breedingItem.isEmpty();) {
				EntityAnimal entity = iter.next();
				assert isEntityOk(entity);

				if (entity.isBreedingItem(breedingItem)) {
					entity.setInLove(null);
					iter.remove();
					breedingItem.setCount(breedingItem.getCount() - 1);
					drawUpgradeOperationCode(chest);
					inv.markDirty();
				}
			}
			if (list.isEmpty() || !hasUpgradeOperationCost(chest)) {
				break;
			}
		}
	}

	private boolean isEntityOk(EntityAnimal entity) {
		return !entity.isDead && !entity.isInLove() && entity.getGrowingAge() == 0;
	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energyBreeding;
	}
}
