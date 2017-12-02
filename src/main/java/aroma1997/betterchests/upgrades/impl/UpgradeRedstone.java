package aroma1997.betterchests.upgrades.impl;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.FMLCommonHandler;

import aroma1997.core.container.ContainerBase;
import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class UpgradeRedstone extends BasicUpgrade {
	public UpgradeRedstone() {
		super(false, 1, UpgradableBlockType.CHEST.array);
	}

	@Override
	public Number getChestModifier(IUpgradableBlock chest, ChestModifier modifier, ItemStack stack) {
		switch (modifier) {
		case REDSTONE:
			int num = 0;
			for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
				if (player.openContainer != null && player.openContainer instanceof ContainerBase && ((ContainerBase<?>) player.openContainer).inventory == chest) {
					num++;
				}
			}
			return num;
		default:
			return null;
		}
	}
}
