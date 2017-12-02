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
	BAG;

	/**
	 * A array just containing the current UpgradableBlockType.
	 */
	public final UpgradableBlockType[] array = new UpgradableBlockType[]{this};

	/**
	 * All UpgradableBlockTypes, that exist
	 */
	public static final UpgradableBlockType[] VALUES = values();
	/**
	 * All upgradableBlockTypes, that are inventories.
	 * (As stated above, more may follow)
	 */
	public static final UpgradableBlockType[] INVENTORIES = {CHEST, BAG};
}
