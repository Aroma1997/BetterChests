package aroma1997.betterchests.api;

import net.minecraft.item.ItemStack;

/**
 * ChestModifiers are used to change the behaviour of the chest with Upgrades.
 * The only use is {@link IUpgrade#getChestModifier(IUpgradableBlock, ChestModifier, ItemStack)}
 */
public enum ChestModifier {
	/**
	 * This modifies the size of the Upgradable Block.
	 * For Inventories, this increases the amount of slots the inventory has.
	 * The return type is used as integer.
	 */
	SIZE,
	/**
	 * This modifies the amount of "blocked" slots an inventory has. The first slots will be blocked from interaction
	 * with pipes and hoppers.
	 * The return type is used as integer.
	 */
	SIZE_BEGIN,
	/**
	 * This determines the strength of the redstone signal emitted by the block.
	 * The return type is used as integer
	 */
	REDSTONE,
	/**
	 * This determines the strength of the redstone signal emitted by a comparator facing away from the block.
	 * The return type is used as integer
	 */
	COMPARATOR,
	/**
	 * This determines the hardness of the block against explosions and similar.
	 * The return type is used as a float.
	 */
	HARDNESS,
	/**
	 * This modifies the size of the block's internal energy storage.
	 * The return type is used as a integer.
	 */
	ENERGY_CAPACITY;
}
