package aroma1997.betterchests.integration.ic2;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import net.minecraftforge.energy.IEnergyStorage;

import aroma1997.core.util.LocalizationHelper;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradeHelper;

import ic2.api.item.IBackupElectricItemManager;


import static aroma1997.betterchests.integration.ic2.Ic2Integration.EU_PER_FE;

public class ItemHandler implements IBackupElectricItemManager {

	@Override
	public boolean handles(ItemStack stack) {
		return UpgradeHelper.INSTANCE.getInventory(stack, null) != null;
	}

	@Override
	public double charge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
		IUpgradableBlock block = UpgradeHelper.INSTANCE.getInventory(stack, null);
		return block.getEnergyStorage().receiveEnergy((int) (amount / EU_PER_FE), simulate) * EU_PER_FE;
	}

	@Override
	public double discharge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
		return 0;
	}

	@Override
	public double getCharge(ItemStack stack) {
		IUpgradableBlock block = UpgradeHelper.INSTANCE.getInventory(stack, null);
		return block.getEnergyStorage().getEnergyStored() * EU_PER_FE;
	}

	@Override
	public double getMaxCharge(ItemStack stack) {
		IUpgradableBlock block = UpgradeHelper.INSTANCE.getInventory(stack, null);
		return block.getEnergyStorage().getMaxEnergyStored() * EU_PER_FE;
	}

	@Override
	public boolean canUse(ItemStack stack, double amount) {
		return false;
	}

	@Override
	public boolean use(ItemStack stack, double amount, EntityLivingBase entity) {
		return false;
	}

	@Override
	public void chargeFromArmor(ItemStack stack, EntityLivingBase entity) {

	}

	@Override
	public String getToolTip(ItemStack stack) {
		IUpgradableBlock block = UpgradeHelper.INSTANCE.getInventory(stack, null);
		IEnergyStorage storage = block.getEnergyStorage();
		return LocalizationHelper.localizeFormatted("betterchests:gui.upgrades.energy.tooltip", storage.getEnergyStored(), storage.getMaxEnergyStored());
	}

	@Override
	public int getTier(ItemStack stack) {
		return 1;
	}
}
