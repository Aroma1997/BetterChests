package aroma1997.betterchests;

import aroma1997.core.item.AromicItem;
import aroma1997.core.util.registry.AutoRegister;
import aroma1997.core.util.registry.HasItemBlock;
import aroma1997.betterchests.bag.ItemBBag;
import aroma1997.betterchests.chest.BlockBetterChest;
import aroma1997.betterchests.chest.ItemChestPickup;
import aroma1997.betterchests.chest.ItemChestUpgrade;
import aroma1997.betterchests.filter.ItemFilter;
import aroma1997.betterchests.upgrades.DirectionalUpgrade;
import aroma1997.betterchests.upgrades.DummyUpgradeType;
import aroma1997.betterchests.upgrades.ItemUpgrade;
import aroma1997.betterchests.upgrades.ItemUpgradeDirectional;
import aroma1997.betterchests.upgrades.PowerUpgradeType;
import aroma1997.betterchests.upgrades.UpgradeType;

public class BlocksItemsBetterChests {

	@AutoRegister
	@HasItemBlock
	public static BlockBetterChest betterchest = new BlockBetterChest();

	@AutoRegister
	public static ItemChestUpgrade chestupgrade = new ItemChestUpgrade();

	@AutoRegister
	public static ItemChestPickup chestpickup = new ItemChestPickup();

	@AutoRegister
	public static ItemUpgrade<UpgradeType> upgrade = (ItemUpgrade<UpgradeType>) new ItemUpgrade<>(UpgradeType.class).setUnlocalizedName("betterchests:upgrade");

	@AutoRegister
	public static ItemUpgrade<PowerUpgradeType> powerupgrade = (ItemUpgrade<PowerUpgradeType>) new ItemUpgrade<>(PowerUpgradeType.class).setUnlocalizedName("betterchests:powerupgrade");

	@AutoRegister
	public static ItemUpgrade<DummyUpgradeType> dummyupgrade = (ItemUpgrade<DummyUpgradeType>) new ItemUpgrade<>(DummyUpgradeType.class).setUnlocalizedName("betterchests:dummyupgrade");

	@AutoRegister
	public static ItemUpgradeDirectional<DirectionalUpgrade> directionalupgrade = (ItemUpgradeDirectional<DirectionalUpgrade>) new ItemUpgradeDirectional<>(DirectionalUpgrade.class).setUnlocalizedName("betterchests:directionalupgrade");

	@AutoRegister
	public static ItemBBag betterbag = new ItemBBag();

	@AutoRegister
	public static ItemFilter filter = new ItemFilter();

	@AutoRegister
	public static AromicItem crafting = (AromicItem) new AromicItem().setUnlocalizedName("betterchests:crafting").setCreativeTab(BetterChests.creativeTab);

}
