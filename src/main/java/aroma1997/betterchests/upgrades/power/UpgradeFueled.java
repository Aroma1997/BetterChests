package aroma1997.betterchests.upgrades.power;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import net.minecraftforge.energy.IEnergyStorage;

import aroma1997.core.inventory.InvUtil;
import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.api.UpgradeHelper;

public class UpgradeFueled extends PowerBaseUpgrade {

	public UpgradeFueled() {
		super(true, 8, UpgradableBlockType.VALUES);
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (UpgradeHelper.INSTANCE.getFrequencyTick(chest, stack, 64) != 0) {
			return;
		}
		IEnergyStorage storage = chest.getEnergyStorage();
		if (storage.getEnergyStored() >= storage.getMaxEnergyStored()) {
			return;
		}

		IBetterChest inv = (IBetterChest) chest;
		IFilter filter = inv.getFilterFor(stack);

		int slot = InvUtil.findInInvInternal(inv, null, test -> TileEntityFurnace.getItemBurnTime(test) > 0 && filter.matchesStack(stack));
		if (slot != -1) {
			ItemStack current = inv.getStackInSlot(slot);
			int provided = TileEntityFurnace.getItemBurnTime(current) * Config.INSTANCE.energyFueled;
			if (storage.receiveEnergy(provided, true) == provided) {
				storage.receiveEnergy(provided, false);
				current.setCount(current.getCount() - 1);
				inv.markDirty();
			}
		}
	}
}
