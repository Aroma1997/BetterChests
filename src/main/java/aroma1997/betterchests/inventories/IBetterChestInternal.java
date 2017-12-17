package aroma1997.betterchests.inventories;

import aroma1997.core.inventory.IInventoryPartContainer;
import aroma1997.betterchests.api.IBetterChest;

public interface IBetterChestInternal extends IBetterChest, IUpgradableBlockInternal, IInventoryPartContainer {

	InventoryPartChest getChestPart();
}
