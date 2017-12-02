package aroma1997.betterchests.upgrades.impl;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.AxisAlignedBB;

import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.upgrades.BasicUpgrade;
import aroma1997.betterchests.upgrades.DummyUpgradeType;


import static aroma1997.betterchests.upgrades.impl.UpgradeBreeding.RADIUS;

public class UpgradeKilling extends BasicUpgrade {

	private static final String DAMAGE_DESC = "betterchests:damagesource.killingUpgrade";
	private static final DamageSource DAMAGE_SOURCE = new DamageSource(DAMAGE_DESC);

	private static final int ANIMALS_TO_KEEP_ALIVE = 2;

	public UpgradeKilling() {
		super(true, 1, UpgradableBlockType.CHEST.array);
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (UpgradeHelper.INSTANCE.getFrequencyTick(chest, stack, 100) != 0) {
			return;
		}

		AxisAlignedBB bb = new AxisAlignedBB(chest.getPosition()).grow(RADIUS);

		TObjectIntMap<Class<? extends EntityLiving>> map = new TObjectIntHashMap<>();

		for (EntityLiving entity : chest.getWorldObj().getEntitiesWithinAABB(EntityLiving.class, bb)) {
			if (entity.isDead) {
				continue;
			}

			if (entity instanceof EntityAnimal) {
				EntityAnimal animal = (EntityAnimal) entity;
				if (entity.isChild()) {
					continue;
				}
				int currentAnimals = map.get(animal.getClass());
				if (currentAnimals < ANIMALS_TO_KEEP_ALIVE) {
					map.put(animal.getClass(), currentAnimals + 1);
					continue;
				}
			}

			if (hasUpgradeOperationCost(chest)) {
				EntityPlayer source = null;
				if (chest.isUpgradeInstalled(DummyUpgradeType.AI.getStack())) {
					source = chest.getFakePlayer();
				}
				entity.attackEntityFrom(getDamageSource(source), 10);
				drawUpgradeOperationCode(chest);
			}
		}
	}

	public static DamageSource getDamageSource(EntityPlayer source) {
		return source == null ? DAMAGE_SOURCE : new EntityDamageSource(DAMAGE_DESC, source);
	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energyKilling;
	}
}
