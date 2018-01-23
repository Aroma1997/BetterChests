package aroma1997.betterchests.api;

import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * This interface is implemented on all Upgradable Blocks, that represent a Fluid Tank like a BetterTank.
 */
public interface IBetterTank extends IUpgradableBlock {

	/**
	 * Returns the {@link IFluidHandler} of this tank, that is not associated with any side.
	 * @return the IFluidHandler of this tank.
	 */
	IFluidHandler getFluidHandler();

	/**
	 * Returns the internal FluidTank of this BetterTank. It's safe to use drainInternal/fillInternal
	 * @return The FluidTank of this BetterTank.
	 */
	FluidTank getTank();

}
