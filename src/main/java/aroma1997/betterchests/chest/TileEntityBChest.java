package aroma1997.betterchests.chest;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

import aroma1997.core.container.IGuiProvider;
import aroma1997.core.network.NetworkHelper;
import aroma1997.core.network.packets.PacketTeUpdate;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.client.gui.GuiBChest;
import aroma1997.betterchests.client.gui.GuiUpgrades;
import aroma1997.betterchests.container.ContainerBChest;
import aroma1997.betterchests.container.ContainerUpgrades;
import aroma1997.betterchests.inventories.IBetterChestInternal;
import aroma1997.betterchests.inventories.InventoryPartChest;

public class TileEntityBChest extends TileEntityUpgradableBlockBase implements IBetterChestInternal, IGuiProvider, ITickable {

	private final InventoryPartChest chestInv;

	private static final Predicate<Field> updatePredicate = NetworkHelper.getForName("angle").or(NetworkHelper.getForName("prevAngle")).or(NetworkHelper.getForName("numPlayers"));
	public float angle, prevAngle;
	public int numPlayers;

	public TileEntityBChest() {
		chestInv = new InventoryPartChest(this);
	}

	@Override
	public UpgradableBlockType getUpgradableBlockType() {
		return UpgradableBlockType.CHEST;
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
		updateAngle();
	}

	private void updateAngleForClient() {
		new PacketTeUpdate(this, updatePredicate).sendPacket();
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
		super.openInventory(player);
		numPlayers++;
		updateAngleForClient();
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		super.closeInventory(player);
		numPlayers--;
		updateAngleForClient();
	}
}
