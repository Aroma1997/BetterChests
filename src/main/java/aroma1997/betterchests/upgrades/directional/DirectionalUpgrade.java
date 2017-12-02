package aroma1997.betterchests.upgrades.directional;

import java.util.Collection;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import aroma1997.betterchests.BlocksItemsBetterChests;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class DirectionalUpgrade extends BasicUpgrade {

	protected int tickTime = 60;

	public DirectionalUpgrade(boolean canBeDisabled, int maxUpgrades, UpgradableBlockType[] type) {
		super(canBeDisabled, maxUpgrades, type);
	}

	public DirectionalUpgrade(boolean canBeDisabled, int maxUpgrades, UpgradableBlockType[] type, Supplier<Collection<ItemStack>> requiredUpgrades) {
		super(canBeDisabled, maxUpgrades, type, requiredUpgrades);
	}

	public BlockPos getPos(IUpgradableBlock chest, ItemStack upgrade) {
		return chest.getPosition().offset(getCurrentSide(upgrade, chest));
	}

	public int getInternalTickTime(IUpgradableBlock chest, ItemStack upgrade) {
		return UpgradeHelper.INSTANCE.getFrequencyTick(chest, upgrade, tickTime * EnumFacing.VALUES.length);
	}

	public int getTickTime(IUpgradableBlock chest, ItemStack upgrade) {
		return getInternalTickTime(chest, upgrade) % tickTime;
	}

	public EnumFacing getCurrentSide(ItemStack stack, IUpgradableBlock block) {
		EnumFacing side = getSide(stack);
		if (side == null) {
			side = EnumFacing.VALUES[getInternalTickTime(block, stack) / tickTime];
		}
		return side;
	}


	public EnumFacing getSide(ItemStack upgrade) {
		return BlocksItemsBetterChests.directionalupgrade.getSide(upgrade);
	}
}
