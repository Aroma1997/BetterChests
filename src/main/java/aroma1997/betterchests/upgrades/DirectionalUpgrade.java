package aroma1997.betterchests.upgrades;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import aroma1997.betterchests.BlocksItemsBetterChests;
import aroma1997.betterchests.upgrades.directional.UpgradeBreaker;
import aroma1997.betterchests.upgrades.directional.UpgradePlacer;

public enum DirectionalUpgrade implements Supplier<BasicUpgrade> {
	PLACER(new UpgradePlacer()),
	BREAKER(new UpgradeBreaker());

	private final BasicUpgrade upgrade;

	private DirectionalUpgrade(BasicUpgrade upgrade) {
		this.upgrade = upgrade;
	}

	@Override
	public BasicUpgrade get() {
		return upgrade;
	}

	public ItemStack getStack() {
		return BlocksItemsBetterChests.directionalupgrade.getStack(this);
	}
}
