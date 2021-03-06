package aroma1997.betterchests.api;

/**
 * The different types a upgradable block can have.
 * Right now, there is only chest and bag available, but different ones might get added later.
 * (Upgradable tanks or buckets and so on)
 * @author Aroma1997
 */
public enum UpgradableBlockType {
	/**
	 * A Upgradable Chest.
	 * Must implement the interface {@link IBetterChest}
	 */
	CHEST,
	/**
	 * A upgradable backpack.
	 * Must implement the interfaces {@link IBetterChest} and {@link IMobileUpgradableBlock}
	 */
	BAG,
	/**
	 * A upgradable chest, that can only hold one kind of Item.
	 * Must implement the interface {@link IBetterChest}
	 */
	BARREL,
	/**
	 * A upgradable backpack, that can only hold one kind of Item.
	 * Must implement the interface {@link IBetterChest}
	 */
	PORTABLE_BARREL,
	/**
	 * A upgradable fluid tank, that can hold a single fluid.
	 * Must implement the interface {@link IBetterTank}
	 */
	TANK;

	/**
	 * A array just containing the current UpgradableBlockType.
	 */
	public final UpgradableBlockType[] array = new UpgradableBlockType[]{this};

	/**
	 * All UpgradableBlockTypes, that exist
	 */
	public static final UpgradableBlockType[] VALUES = values();
	/**
	 * All upgradableBlockTypes, that are normal inventories that can hold multiple different items like chests.
	 */
	public static final UpgradableBlockType[] NORMAL_INVENTORIES = {CHEST, BAG};
	/**
	 * All upgradableBlockTypes, that are inventories.
	 * (As stated above, more may follow)
	 */
	public static final UpgradableBlockType[] INVENTORIES = {CHEST, BAG, BARREL, PORTABLE_BARREL};

	/**
	 * All upgradableBlockTypes, that represent a fluid tank.
	 */
	public static final UpgradableBlockType[] TANKS = {TANK};

	/**
	 * All upgradableBlockTypes, that represent blocks, that are placed in-world.
	 */
	public static final UpgradableBlockType[] BLOCKS = {CHEST, BARREL, TANK};
}
