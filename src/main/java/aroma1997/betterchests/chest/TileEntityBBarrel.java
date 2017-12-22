package aroma1997.betterchests.chest;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.network.AutoEncode;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.client.gui.GuiUpgrades;
import aroma1997.betterchests.container.ContainerUpgrades;
import aroma1997.betterchests.inventories.IBetterChestInternal;
import aroma1997.betterchests.inventories.InventoryPartBarrel;

public class TileEntityBBarrel extends TileEntityUpgradableBlockBase implements IBetterChestInternal {

	@AutoEncode
	private final InventoryPartBarrel chestPart;

	public TileEntityBBarrel() {
		chestPart = new InventoryPartBarrel(this);
	}

	@Override
	public UpgradableBlockType getUpgradableBlockType() {
		return UpgradableBlockType.BARREL;
	}

	@Override
	public Container getContainer(EntityPlayer player, short id) {
		return new ContainerUpgrades(this, player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Gui getGui(EntityPlayer player, short id) {
		return new GuiUpgrades(new ContainerUpgrades(this, player));
	}

	@Override
	public InventoryPartBarrel getChestPart() {
		return chestPart;
	}

	@Override
	public IFilter getFilterFor(ItemStack stack) {
		IFilter filter = filterInv.getFilterForUpgrade(stack);
		return new IFilter() {
			@Override
			public boolean matchesStack(ItemStack stack) {
				return chestPart.isItemValidForSlot(0, stack) && filter.matchesStack(stack);
			}

			@Override
			public boolean hasStackFilter() {
				return true;
			}
		};
	}
}
