package aroma1997.betterchests.chest;

import java.lang.reflect.Field;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.minecraftforge.energy.IEnergyStorage;

import aroma1997.core.block.te.TileEntityInventory;
import aroma1997.core.block.te.element.EnergyElement;
import aroma1997.core.block.te.element.FakePlayerElement;
import aroma1997.core.block.te.element.RedstoneEmitterElement;
import aroma1997.core.container.IGuiProvider;
import aroma1997.core.inventory.inventorypart.InventoryPartBase;
import aroma1997.core.network.AutoEncode;
import aroma1997.core.network.AutoEncode.GuiEncode;
import aroma1997.core.network.NetworkHelper;
import aroma1997.core.network.packets.PacketTeUpdate;
import aroma1997.core.util.ItemUtil;
import aroma1997.core.util.Util;
import aroma1997.betterchests.UpgradeHelper;
import aroma1997.betterchests.api.ChestModifier;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.client.gui.GuiBChest;
import aroma1997.betterchests.client.gui.GuiUpgrades;
import aroma1997.betterchests.container.ContainerBChest;
import aroma1997.betterchests.container.ContainerUpgrades;
import aroma1997.betterchests.inventories.IBetterChestInternal;
import aroma1997.betterchests.inventories.InventoryPartChest;
import aroma1997.betterchests.inventories.InventoryPartFilter;
import aroma1997.betterchests.inventories.InventoryPartUpgrades;

public class TileEntityBChest extends TileEntityInventory implements IBetterChestInternal, IGuiProvider, ITickable {

	private final InventoryPartChest chestInv;
	@AutoEncode
	private final InventoryPartUpgrades upgradeInv;
	@AutoEncode
	private final InventoryPartFilter filterInv;

	protected final FakePlayerElement fakePlayer;

	protected final RedstoneEmitterElement redstoneEmitter;

	private static final Predicate<Field> updatePredicate = NetworkHelper.getForName("angle").or(NetworkHelper.getForName("prevAngle")).or(NetworkHelper.getForName("numPlayers"));
	public float angle, prevAngle;
	public int numPlayers;

	private int tickCount = Util.getRandom().nextInt();

	@GuiEncode
	public final EnergyElement energy;

	public TileEntityBChest() {
		chestInv = new InventoryPartChest(this);
		upgradeInv = new InventoryPartUpgrades(this);
		filterInv = new InventoryPartFilter(this);

		energy = addElement(new EnergyElement(this, 0, Integer.MAX_VALUE, 0) {
			@Override
			public int getMaxEnergyStored() {
				return UpgradeHelper.INSTANCE.getPowerCapacity(TileEntityBChest.this);
			}
		});
		fakePlayer = addElement(new FakePlayerElement(this));

		comparator.setRedstoneLookup(() -> UpgradeHelper.INSTANCE.intSumFirst(this, ChestModifier.COMPARATOR, 0));

		redstoneEmitter = addElement(new RedstoneEmitterElement(this));
		redstoneEmitter.setRedstoneLookup(() -> UpgradeHelper.INSTANCE.intSumFirst(this, ChestModifier.REDSTONE, 0));
	}

	private Vec3d posPrecise;

	@Override
	public UpgradableBlockType getUpgradableBlockType() {
		return UpgradableBlockType.CHEST;
	}

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
	public Container getContainer(EntityPlayer player, short id) {
		switch (id) {
		case 1: return new ContainerUpgrades(this, player);
		}
		return new ContainerBChest(this, player);
	}

	@Override
	public Gui getGui(EntityPlayer player, short id) {
		switch (id) {
		case 1: return new GuiUpgrades(new ContainerUpgrades(this, player));
		}
		return new GuiBChest(new ContainerBChest(this, player));
	}

	@Override
	public InventoryPartChest getChestPart() {
		return chestInv;
	}

	@Override
	public void update() {
		super.update();
		tickCount++;
		upgradeInv.tick();
		updateAngle();
	}

	@Override
	public int getTickCount() {
		return tickCount;
	}

	@Override
	public EntityPlayerMP getFakePlayer() {
		return fakePlayer.getFakePlayer();
	}

	private void updateAngleForClient() {
		new PacketTeUpdate(this, updatePredicate).sendPacket();
		markDirty();
	}

	private void updateAngle() {
		int x = this.pos.getX();
		int y = this.pos.getY();
		int z = this.pos.getZ();

		//Our notifications should be consistent enough. If that's not the case, we can just uncomment this again.

//		if (!this.world.isRemote && this.numPlayers != 0 && tickCount % 200 == 0) {
//			this.numPlayers = 0;
//			float f = 5.0F;
//
//			for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double)((float)x - 5.0F), (double)((float)y - 5.0F), (double)((float)z - 5.0F), (double)((float)(x + 1) + 5.0F), (double)((float)(y + 1) + 5.0F), (double)((float)(z + 1) + 5.0F)))) {
//				if (entityplayer.openContainer instanceof ContainerBase) {
//					if (((ContainerBase)entityplayer.openContainer).inventory == this) {
//						numPlayers++;
//					}
//				}
//			}
//		}

		this.prevAngle = this.angle;

		if (this.numPlayers > 0 && this.angle <= 0.0F) {
			world.playSound((EntityPlayer)null, x + 0.5D, (double)y + 0.5D, z + 0.5D, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (this.numPlayers == 0 && this.angle > 0.0F)
		{
			this.angle -= 0.1F;

			if (this.angle < 0.5F && prevAngle >= 0.5F)
			{
				this.world.playSound((EntityPlayer)null, x + 0.5D, (double)y + 0.5D,z + 0.5D, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (this.angle < 0.0F)
			{
				this.angle = 0.0F;
			}
		}

		if (this.numPlayers > 0 && this.angle < 1.0F) {
			this.angle += 0.1F;

			if (this.angle > 1.0F)
			{
				this.angle = 1.0F;
			}
		}

		if (prevAngle != angle) {
			updateAngleForClient();
		}
	}

	@Override
	public void openInventory(EntityPlayer player) {
		for (InventoryPartBase part : getParts()) {
			part.openInventory(player);
		}
		numPlayers++;
		updateAngleForClient();
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		for (InventoryPartBase part : getParts()) {
			part.closeInventory(player);
		}
		numPlayers--;
		updateAngleForClient();
	}

	@Override
	public IFilter getFilterFor(ItemStack stack) {
		return filterInv.getFilterForUpgrade(stack);
	}
}
