package aroma1997.betterchests.integration.ic2;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import aroma1997.core.block.te.TileEntityBase;
import aroma1997.core.block.te.element.EnergyElement;
import aroma1997.core.block.te.element.TileEntityElementBase;
import aroma1997.betterchests.api.IUpgradableBlock;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.info.ILocatable;


import static aroma1997.betterchests.integration.ic2.Ic2Integration.EU_PER_FE;

public class BlockHandler {

	public BlockHandler() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void initTe(AttachCapabilitiesEvent<TileEntity> event) {
		if (event.getObject() instanceof IUpgradableBlock && event.getObject() instanceof TileEntityBase) {
			TileEntityBase chest = (TileEntityBase) event.getObject();
			chest.addElement(new IC2EnergyElement(chest));
		}
	}

	public static class IC2EnergyElement extends TileEntityElementBase implements IEnergySink, ILocatable {

		public IC2EnergyElement(TileEntityBase parent) {
			super(parent);
		}

		@Override
		public void load() {
			EnergyNet.instance.addTile(this);
		}

		@Override
		public void unload() {
			EnergyNet.instance.removeTile(this);
		}

		@Override
		public double getDemandedEnergy() {
			return getEnergyElement().receiveEnergy(Integer.MAX_VALUE, true) * EU_PER_FE;
		}

		@Override
		public int getSinkTier() {
			return Integer.MAX_VALUE;
		}

		@Override
		public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
			return amount - getEnergyElement().receiveEnergy((int) (amount / EU_PER_FE), false) * EU_PER_FE;
		}

		@Override
		public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
			return true;
		}

		@Override
		public BlockPos getPosition() {
			return getParent().getPos();
		}

		@Override
		public World getWorldObj() {
			return getParent().getWorld();
		}
		private EnergyElement getEnergyElement() {
			return getParent().getElement(EnergyElement.class);
		}
	}
}
