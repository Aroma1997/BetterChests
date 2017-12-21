package aroma1997.betterchests.upgrades;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import aroma1997.betterchests.BlocksItemsBetterChests;
import aroma1997.betterchests.upgrades.impl.UpgradeAnimal;
import aroma1997.betterchests.upgrades.impl.UpgradeBlocking;
import aroma1997.betterchests.upgrades.impl.UpgradeBreeding;
import aroma1997.betterchests.upgrades.impl.UpgradeCharging;
import aroma1997.betterchests.upgrades.impl.UpgradeCobbestone;
import aroma1997.betterchests.upgrades.impl.UpgradeCollector;
import aroma1997.betterchests.upgrades.impl.UpgradeComparator;
import aroma1997.betterchests.upgrades.impl.UpgradeFeeding;
import aroma1997.betterchests.upgrades.impl.UpgradeFurnace;
import aroma1997.betterchests.upgrades.impl.UpgradeHarvesting;
import aroma1997.betterchests.upgrades.impl.UpgradeKilling;
import aroma1997.betterchests.upgrades.impl.UpgradePlanting;
import aroma1997.betterchests.upgrades.impl.UpgradeRedstone;
import aroma1997.betterchests.upgrades.impl.UpgradeResupply;
import aroma1997.betterchests.upgrades.impl.UpgradeSlot;
import aroma1997.betterchests.upgrades.impl.UpgradeTicking;
import aroma1997.betterchests.upgrades.impl.UpgradeUnbreakable;
import aroma1997.betterchests.upgrades.impl.UpgradeVoid;
import aroma1997.betterchests.upgrades.impl.UpgradeWater;

public enum UpgradeType implements Supplier<BasicUpgrade> {
	SLOT(new UpgradeSlot()),
	COBBLEGEN(new UpgradeCobbestone()),
	REDSTONE(new UpgradeRedstone()),
	COMPARATOR(new UpgradeComparator()),
	VOID(new UpgradeVoid()),
	UNBREAKABLE(new UpgradeUnbreakable()),
	WATER(new UpgradeWater()),
	SMELTING(new UpgradeFurnace()),
	TICKING(new UpgradeTicking()),
	COLLECTOR(new UpgradeCollector()),
	PLANTING(new UpgradePlanting()),
	HARVESTING(new UpgradeHarvesting()),
	BLOCKING(new UpgradeBlocking()),
	BREEDING(new UpgradeBreeding()),
	FEEDING(new UpgradeFeeding()),
	RESUPPLY(new UpgradeResupply()),
	KILLING(new UpgradeKilling()),
	ANIMAL(new UpgradeAnimal()),
	CHARGING(new UpgradeCharging());

	final BasicUpgrade impl;

	private UpgradeType(BasicUpgrade upgrade) {
		this.impl = upgrade;
	}

	@Override
	public BasicUpgrade get() {
		return impl;
	}

	public ItemStack getStack() {
		return BlocksItemsBetterChests.upgrade.getStack(this);
	}
}
