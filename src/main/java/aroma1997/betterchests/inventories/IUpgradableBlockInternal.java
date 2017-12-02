package aroma1997.betterchests.inventories;

import java.util.stream.StreamSupport;

import net.minecraft.item.ItemStack;

import aroma1997.core.inventory.IInventoryPartContainer;
import aroma1997.core.util.ItemUtil;
import aroma1997.betterchests.api.IUpgradableBlock;

public interface IUpgradableBlockInternal extends IInventoryPartContainer, IUpgradableBlock {

	InventoryPartUpgrades getUpgradePart();
	InventoryPartFilter getFilterPart();

	@Override
	default	int getAmountUpgrades(ItemStack stack) {
		return (int) StreamSupport.stream(getUpgradePart().spliterator(), false).filter(other -> !isUpgradeDisabled(other) && ItemUtil.areItemsSameMatchingIdDamage(stack, other)).count();
	}

	@Override
	default boolean isUpgradeDisabled(ItemStack stack) {
		return getUpgradePart().isUpgradeDisabled(stack);
	}

	@Override
	default void setUpgradeDisabled(ItemStack stack, boolean targetVal) {
		getUpgradePart().setUpgradeDisabled(stack, targetVal);
	}

	@Override
	default Iterable<ItemStack> getUpgrades() {
		return getUpgradePart();
	}

	@Override
	default Iterable<ItemStack> getActiveUpgrades() {
		return getUpgradePart()::getActiveUpgrades;
	}


}
