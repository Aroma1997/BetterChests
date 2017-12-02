package aroma1997.betterchests.upgrades;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import aroma1997.betterchests.BlocksItemsBetterChests;
import aroma1997.betterchests.api.UpgradableBlockType;

public enum DummyUpgradeType implements Supplier<BasicUpgrade> {
	AI;

	private static final BasicUpgrade impl = new BasicUpgrade(false, 1, UpgradableBlockType.VALUES);

	@Override
	public BasicUpgrade get() {
		return impl;
	}

	public ItemStack getStack() {
		return BlocksItemsBetterChests.dummyupgrade.getStack(this);
	}
}
