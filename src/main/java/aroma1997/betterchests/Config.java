package aroma1997.betterchests;

import net.minecraftforge.common.config.Configuration;

import aroma1997.core.config.Conf;
import aroma1997.core.config.ConfigEntry;
import aroma1997.core.config.ConfigEntry.ConfigCategoryDesc;
import aroma1997.core.config.ConfigEntry.ConfigLimitInt;

public enum Config {
	INSTANCE;

	@ConfigCategoryDesc("The values here determine how much energy the upgrade will take at each operation.")
	public static final String CATEGORY_ENERGY = "Energy usage";

	@ConfigCategoryDesc("The values here determine how much power the upgrades will produce.")
	public static final String CATEGORY_ENERGY_PRODUCTION = "Energy production";

	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energyCobblestone = 0;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energyVoid = 0;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energyWater = 0;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energySmelting = 2000;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energyTicking = 1000;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energyCollector = 1000;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energyPlanting = 1000;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energyHarvesting = 500;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energyBreeding = 1000;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energyFeeding = 10000;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energyKilling = 1000;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energyAnimal = 1000;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energyPlacer = 1000;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY)
	public final int energyBreaker = 1000;

	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY_PRODUCTION)
	public final int energySolar = 10;
	@ConfigLimitInt(min = 0)
	@ConfigEntry(category = CATEGORY_ENERGY_PRODUCTION, description = "This value acts as a multiplier to the vanilla item burn time.")
	public final int energyFueled = 20;


	public void load() {
		Configuration config = Conf.getConfig(Reference.MOD_ID);
		Conf.loadConfig(config, getClass(), this);
		if (config.hasChanged()) {
			config.save();
		}
	}

}
