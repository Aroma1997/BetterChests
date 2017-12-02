package aroma1997.betterchests.api;

import java.util.Collection;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

/**
 * This interface needs to be implemented on the item class for each upgrade.
 *
 * @author Aroma1997
 */
public interface IUpgrade {

	/**
	 * Returns a list of upgrade, that need to be installed in a Upgradable Block for this upgrade to be installed.
	 *
	 * @param stack The upgrade ItemStack.
	 * @return a list of required Upgrades.
	 */
	Collection<ItemStack> getRequiredUpgrades(ItemStack stack);

	/**
	 * Returns whether the requirements for this upgrade are installed. This should take things into account like
	 * required upgrade, but not upgrade count.
	 * @param chest The Upgradable Block the upgrade is in.
	 * @param stack The actual Upgrade.
	 * @return Whether the chest meets the criteria for the upgrade.
	 */
	default boolean areRequirementsMet(IUpgradableBlock chest, ItemStack stack) {
		return getRequiredUpgrades(stack).stream().allMatch(chest::isUpgradeInstalled) &&
				getCompatibleTypes(stack).contains(chest.getUpgradableBlockType());
	}

	/**
	 * Returns whether this upgrade can be installed in the given Upgradable Block. This should take into account,
	 * whether the required Upgrades are installed, the UpgradableBlockType matchesStack the upgrade, the maximum amount of
	 * this type of upgrade into the Block and specific upgrade-related things.
	 *
	 * @param chest The Upgradable Block the upgrade will be inserted into.
	 * @param stack The actual Upgrade.
	 * @return Whether the upgrade can be installed into the Upgradable Block.
	 */
	default boolean canBePutInChest(IUpgradableBlock chest, ItemStack stack) {
		return chest.getAmountUpgrades(stack) < getMaxAmountUpgrades(stack) &&
				areRequirementsMet(chest, stack);
	}

	/**
	 * Returns a list of UpgradableBlockTypes this upgrade is compatible to.
	 * @param stack The actual Upgrade
	 * @return a list of compatible UpgradableBlockTypes
	 */
	Collection<UpgradableBlockType> getCompatibleTypes(ItemStack stack);

	/**
	 * This method will be called once a tick. Similar to {@link net.minecraft.util.ITickable#update()}
	 * @param chest The Upgradable Block the upgrade is in.
	 * @param stack The actual Upgrade.
	 */
	void update(IUpgradableBlock chest, ItemStack stack);

	/**
	 * Returns whether this upgrade can be disabled. Disabled Upgrades won't get @link{#update}d
	 * @param stack The actual Upgrade.
	 * @return Whether the upgrade can be disabled.
	 */
	boolean canBeDisabled(ItemStack stack);

	/**
	 * This method will be called to alter some functionality of the Upgradable Block.
	 * For information on what exactly you can change, you can take a look at {@link ChestModifier}.
	 * {@link ChestModifier} also contains information about what type the return value will be used as. (int, ...)
	 * Returning null should be the default behaviour. Null represents the neutral element. (0 for sums, 1 for
	 * multiplications etc.)
	 * @param chest The Upgradable Block the upgrade is in.
	 * @param modifier The ChestModifier the return value should be for.
	 * @param stack The actual Upgrade.
	 * @return A Number representing a ChestModifier or null for default behaviour.
	 */
	@Nullable Number getChestModifier(IUpgradableBlock chest, ChestModifier modifier, ItemStack stack);

	/**
	 * Returns the maximum amount of upgrade of this type the chest can have.
	 * @param stack The actual upgrade.
	 * @return The maximum amount of Upgrades per chest.
	 */
	int getMaxAmountUpgrades(ItemStack stack);
}
