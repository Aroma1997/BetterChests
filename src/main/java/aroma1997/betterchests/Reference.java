/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import java.io.InputStream;
import java.util.Properties;

import com.google.common.base.Throwables;

public class Reference {

	static {
		Properties prop = new Properties();

		try {
			InputStream stream = Reference.class
					.getResourceAsStream("reference.properties");
			prop.load(stream);
			stream.close();
		} catch (Exception e) {
			Throwables.propagate(e);
		}

		VERSION = prop.getProperty("version");
	}

	public static final String MOD_ID = "BetterChests";

	public static final String MOD_NAME = MOD_ID;

	public static final String VERSION;

	/**
	 * Some static values. Subject to change.
	 */
	public static class Conf {

		public static final float RAIN_THINGY = 0.8F;

		public static final int TICK_TIME = 64;

		public static final double FEED_RADIUS = 7.0D;

		public static final double FEED_HEIGHT = 3.0D;

		public static final int FEED_ENTITIES_TO_STOP = 50;

		public static final int PLANTS_RANGE_MULTIPLIER = 2;

		public static final int FISHING_RANGE = 1;

		public static final int ENERGY_FUEL_MULTIPLIER = 10;

		public static final int ENERGY_SMELTING = 2000;

		public static final int ENERGY_COLLECTOR = 5;

		public static final int ENERGY_TICKING = 3200;

		public static final int ENERGY_HARVESTING = 10;

		public static final int ENERGY_PLANTING = 10;

		public static final int ENERGY_ANIMAL = 100;

		public static final int ENERGY_KILLING = 100;
	}

}
