/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

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
