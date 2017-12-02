package aroma1997.betterchests.api;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;

/**
 * This interface is implemented on portable upgradable blocks like backpacks.
 * @author Aroma1997
 */
public interface IMobileUpgradableBlock extends IUpgradableBlock {

	/**
	 * Returns the entity currently holding the backpack or null, if the holding entity cannot be determined.
	 * Note that this entity is not always a player, but can also be EntityItem if the item is dropped and so on.
	 * @return The holding entity.
	 */
	@Nullable Entity getEntity();

}
