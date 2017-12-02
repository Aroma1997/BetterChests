package aroma1997.betterchests.api.planter;

import java.util.List;

/**
 * The registry for Plant- and HarvestHandlers.
 * @author Aroma1997
 */
public class PlantHarvestRegistry {

	/**
	 * The actual Plant- and HarvestRegistry.
	 */
	public static IPlantHarvestRegistry INSTANCE;

	public static interface IPlantHarvestRegistry {
		/**
		 * Registers a PlantHandler to this PlantHarvestRegistry.<br/>
		 * Please note: This can only be called during init.
		 * @param handler Tha PlantHandler to register.
		 */
		void registerPlantingHandler(IPlantHandler handler);
		/**
		 * Registers a HarvestHandler to this PlantHarvestRegistry.<br/>
		 * Please note: This can only be called during init.
		 * @param handler Tha HarvestHandler to register.
		 */
		void registerHarvestHandler(IHarvestHandler handler);
		/**
		 * Registers a Plant- and HarvestHandler to this PlantHarvestRegistry.<br/>
		 * Please note: This can only be called during init.
		 * @param handler Tha Plant- and HarvestHandler to register.
		 */
		<T extends IPlantHandler & IHarvestHandler> void register(T handler);

		/**
		 * Returns all currently registered PlantHandlers in decreasing priority.
		 */
		List<IPlantHandler> getPlantHandlers();
		/**
		 * Returns all currently registered HarvestHandlers in decreasing priority.
		 */
		List<IHarvestHandler> getHarvestHandlers();
	}
}
