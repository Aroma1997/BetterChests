package aroma1997.betterchests.tank;

import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.FMLCommonHandler;

import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.inventories.IUpgradableBlockInternal;

public class BetterTank extends FluidTank {

	private final IUpgradableBlockInternal parent;

	private FluidStack dummy;

	public BetterTank(IUpgradableBlockInternal parent) {
		super(0);
		this.parent = parent;
		if (parent instanceof TileEntity) {
			setTileEntity((TileEntity) parent);
		}
	}

	public void updateCapacity() {
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) return;
		setCapacity(8000 + UpgradeHelper.INSTANCE.intSum(parent, ChestModifier.SIZE));
	}

	@Override
	protected void onContentsChanged() {
		parent.markDirty();
	}

	@Override
	public FluidStack getFluid() {
		FluidStack fs = super.getFluid();
		if (fs != null && fs.amount > getCapacity()) {
			fs = fs.copy();
			fs.amount = getCapacity();
		}
		return fs;
	}
}
