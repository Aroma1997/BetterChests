package aroma1997.betterchests.upgrades;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.util.LazyInitializer;
import aroma1997.core.util.LocalizationHelper;
import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.betterchests.api.UpgradableBlockType;

public class BasicUpgrade implements IUpgrade {

	private final boolean canBeDisabled;
	private final int maxUpgrades;
	private final Supplier<Collection<ItemStack>> requiredUpgrades;
	private final Set<UpgradableBlockType> compatibleBlocks = EnumSet.noneOf(UpgradableBlockType.class);

	public BasicUpgrade(boolean canBeDisabled, int maxUpgrades, UpgradableBlockType[] type) {
		this(canBeDisabled, maxUpgrades, type, () -> Collections.emptyList());
	}

	public BasicUpgrade(boolean canBeDisabled, int maxUpgrades, UpgradableBlockType[] type, Supplier<Collection<ItemStack>> requiredUpgrades) {
		this.canBeDisabled = canBeDisabled;
		this.requiredUpgrades = new LazyInitializer<>(requiredUpgrades);
		this.maxUpgrades = maxUpgrades;
		compatibleBlocks.addAll(Arrays.asList(type));
	}

	@Override
	public final Collection<ItemStack> getRequiredUpgrades(ItemStack stack) {
		return requiredUpgrades.get();
	}

	@Override
	public Collection<UpgradableBlockType> getCompatibleTypes(ItemStack stack) {
		return compatibleBlocks;
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {

	}

	@Override
	public boolean canBeDisabled(ItemStack stack) {
		return canBeDisabled;
	}

	@Override
	public int getMaxAmountUpgrades(ItemStack stack) {
		return maxUpgrades;
	}

	@Override
	public Number getChestModifier(IUpgradableBlock chest, ChestModifier modifier, ItemStack stack) {
		return null;
	}

	public int getUpgradeOperationCost() {
		return 0;
	}

	protected boolean hasUpgradeOperationCost(IUpgradableBlock chest) {
		return chest.getEnergyStorage().extractEnergy(getUpgradeOperationCost(), true) >= getUpgradeOperationCost();
	}

	protected void drawUpgradeOperationCode(IUpgradableBlock chest) {
		chest.getEnergyStorage().extractEnergy(getUpgradeOperationCost(), false);
	}

	@SideOnly(Side.CLIENT)
	public void addTooltips(ItemStack upgrade, List<String>tooltips) {
		int requiredPower = getUpgradeOperationCost();
		if (requiredPower > 0) {
			tooltips.add(LocalizationHelper.localizeFormatted("betterchests:tooltip.requiresPower", requiredPower));
		}
	}
}
