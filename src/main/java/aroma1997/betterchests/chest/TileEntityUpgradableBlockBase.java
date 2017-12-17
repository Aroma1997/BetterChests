package aroma1997.betterchests.chest;

import java.util.stream.StreamSupport;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.minecraftforge.energy.IEnergyStorage;

import aroma1997.core.block.te.TileEntityInventory;
import aroma1997.core.block.te.element.EnergyElement;
import aroma1997.core.block.te.element.FakePlayerElement;
import aroma1997.core.block.te.element.RedstoneEmitterElement;
import aroma1997.core.container.IGuiProvider;
import aroma1997.core.network.AutoEncode;
import aroma1997.core.network.AutoEncode.GuiEncode;
import aroma1997.core.util.ItemUtil;
import aroma1997.core.util.Util;
import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.inventories.IUpgradableBlockInternal;
import aroma1997.betterchests.inventories.InventoryPartFilter;
import aroma1997.betterchests.inventories.InventoryPartUpgrades;

public abstract class TileEntityUpgradableBlockBase extends TileEntityInventory implements IUpgradableBlockInternal, IGuiProvider, ITickable {

	@AutoEncode
	protected final InventoryPartUpgrades upgradeInv;
	@AutoEncode
	protected final InventoryPartFilter filterInv;

	protected final FakePlayerElement fakePlayer;

	protected final RedstoneEmitterElement redstoneEmitter;

	private int tickCount = Util.getRandom().nextInt();

	@GuiEncode
	public final EnergyElement energy;

	public TileEntityUpgradableBlockBase() {
		upgradeInv = new InventoryPartUpgrades(this);
		filterInv = new InventoryPartFilter(this);

		energy = addElement(new EnergyElement(this, 0, Integer.MAX_VALUE, 0) {
			@Override
			public int getMaxEnergyStored() {
				return UpgradeHelper.INSTANCE.getPowerCapacity(TileEntityUpgradableBlockBase.this);
			}
		});
		fakePlayer = addElement(new FakePlayerElement(this));

		comparator.setRedstoneLookup(() -> UpgradeHelper.INSTANCE.intSumFirst(this, ChestModifier.COMPARATOR, 0));

		redstoneEmitter = addElement(new RedstoneEmitterElement(this));
		redstoneEmitter.setRedstoneLookup(() -> UpgradeHelper.INSTANCE.intSumFirst(this, ChestModifier.REDSTONE, 0));
	}

	private Vec3d posPrecise;

	@Override
	public Vec3d getPositionPrecise() {
		if (posPrecise == null) {
			posPrecise = new Vec3d(getPos()).add(new Vec3d(0.5, 0.5, 0.5));
		}
		return posPrecise;
	}

	@Override
	public BlockPos getPosition() {
		return pos;
	}

	@Override
	public World getWorldObj() {
		return world;
	}

	@Override
	public Iterable<ItemStack> getUpgrades() {
		return upgradeInv;
	}

	@Override
	public int getAmountUpgrades(ItemStack stack) {
		return (int) StreamSupport.stream(upgradeInv.spliterator(), false).filter(other -> ItemUtil.areItemsSameMatchingIdDamage(stack, other)).count();
	}

	@Override
	public boolean isUpgradeDisabled(ItemStack stack) {
		return upgradeInv.isUpgradeDisabled(stack);
	}

	@Override
	public void setUpgradeDisabled(ItemStack stack, boolean targetVal) {
		upgradeInv.setUpgradeDisabled(stack, targetVal);
	}

	@Override
	public IEnergyStorage getEnergyStorage() {
		return energy;
	}

	@Override
	public InventoryPartUpgrades getUpgradePart() {
		return upgradeInv;
	}

	@Override
	public InventoryPartFilter getFilterPart() {
		return filterInv;
	}

	@Override
	public void update() {
		super.update();
		tickCount++;
		upgradeInv.tick();
	}

	@Override
	public int getTickCount() {
		return tickCount;
	}

	@Override
	public EntityPlayerMP getFakePlayer() {
		return fakePlayer.getFakePlayer();
	}

	public IFilter getFilterFor(ItemStack stack) {
		return filterInv.getFilterForUpgrade(stack);
	}
}
