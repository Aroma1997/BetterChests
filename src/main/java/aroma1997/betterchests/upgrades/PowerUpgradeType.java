package aroma1997.betterchests.upgrades;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import aroma1997.betterchests.BlocksItemsBetterChests;
import aroma1997.betterchests.upgrades.power.UpgradeCapacitor;
import aroma1997.betterchests.upgrades.power.UpgradeEnergyCreative;
import aroma1997.betterchests.upgrades.power.UpgradeFueled;
import aroma1997.betterchests.upgrades.power.UpgradeSolar;

public enum PowerUpgradeType implements Supplier<BasicUpgrade> {
	CAPACITOR(new UpgradeCapacitor()),
	SOLAR(new UpgradeSolar()),
	FUELED(new UpgradeFueled()),
	CREATIVE(new UpgradeEnergyCreative());

	private final BasicUpgrade impl;

	private PowerUpgradeType(BasicUpgrade impl) {
		this.impl = impl;
	}

	@Override
	public BasicUpgrade get() {
		return impl;
	}

	public ItemStack getStack() {
		return BlocksItemsBetterChests.powerupgrade.getStack(this);
	}
}
