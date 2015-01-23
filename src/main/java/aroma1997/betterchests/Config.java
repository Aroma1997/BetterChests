package aroma1997.betterchests;

import net.minecraftforge.common.config.Configuration;
import aroma1997.core.config.Conf;

public class Config {

	private Config() {
	}

	public static final Config INSTANCE = new Config();

	public boolean useEnergy;

	public void load() {
		Configuration cfg = Conf.getConfig(Reference.MOD_ID);
		cfg.load();
		useEnergy = cfg.getBoolean("useEnergy", Configuration.CATEGORY_GENERAL,
				true, "If some Upgrades use energy or not.");
		if (cfg.hasChanged()) {
			cfg.save();
		}
	}

}
