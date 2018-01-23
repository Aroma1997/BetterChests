package aroma1997.betterchests.tank;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.block.te.element.FluidElement;
import aroma1997.core.network.AutoEncode;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.chest.TileEntityUpgradableBlockBase;
import aroma1997.betterchests.client.gui.GuiBTank;
import aroma1997.betterchests.client.gui.GuiUpgrades;
import aroma1997.betterchests.container.ContainerBTank;
import aroma1997.betterchests.container.ContainerUpgrades;

public class TileEntityBTank extends TileEntityUpgradableBlockBase implements IBetterTankInternal {

	protected final FluidElement fluid;

	@AutoEncode
	private final BetterTank tank;

	public TileEntityBTank() {
		fluid = addElement(new FluidElement(this));
		tank = fluid.addManagedTank("tank", new BetterTank(this));
	}

	@Override
	public UpgradableBlockType getUpgradableBlockType() {
		return UpgradableBlockType.TANK;
	}

	@Override
	public Container getContainer(EntityPlayer player, short id) {
		switch(id) {
		case 1:
			return new ContainerUpgrades(this, player);
		default:
			return new ContainerBTank(this, player);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Gui getGui(EntityPlayer player, short id) {
		switch(id) {
		case 1:
			return new GuiUpgrades(new ContainerUpgrades(this, player));
		default:
			return new GuiBTank(new ContainerBTank(this, player));
		}
	}

	@Override
	public FluidTank getTank() {
		return tank;
	}

	@Override
	public void markDirtyInternal() {
		super.markDirtyInternal();
		tank.updateCapacity();
		sendUpdates();
	}

	@Override
	protected void load() {
		super.load();
		tank.updateCapacity();
	}

	@Override
	public IFluidHandler getFluidHandler() {
		return fluid.getFluidHandler(null);
	}

}
