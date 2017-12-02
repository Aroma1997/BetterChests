package aroma1997.betterchests.upgrades.power;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.util.LocalizationHelper;
import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.upgrades.BasicUpgrade;

public class PowerBaseUpgrade extends BasicUpgrade {
	public PowerBaseUpgrade(boolean canBeDisabled, int maxUpgrades, UpgradableBlockType[] type) {
		super(canBeDisabled, maxUpgrades, type);
	}

	public PowerBaseUpgrade(boolean canBeDisabled, int maxUpgrades, UpgradableBlockType[] type, Supplier<Collection<ItemStack>> requiredUpgrades) {
		super(canBeDisabled, maxUpgrades, type, requiredUpgrades);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addTooltips(ItemStack upgrade, List<String> tooltips) {
		Number capacitorIncrease = getChestModifier(null, ChestModifier.ENERGY_CAPACITY, upgrade);
		if (capacitorIncrease != null) {
			tooltips.add(LocalizationHelper.localizeFormatted("betterchests:tooltip.capacitorincrease", capacitorIncrease));
		}
		int powerProvided = getPowerProvided();
		if (powerProvided > 0) {
			tooltips.add(LocalizationHelper.localizeFormatted("betterchests:tooltip.providesPower", powerProvided));
		}
	}

	public int getPowerProvided() {
		return 0;
	}
}
