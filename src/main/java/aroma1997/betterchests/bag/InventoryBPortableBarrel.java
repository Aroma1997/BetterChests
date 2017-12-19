package aroma1997.betterchests.bag;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.client.gui.GuiBarrel;
import aroma1997.betterchests.client.gui.GuiUpgrades;
import aroma1997.betterchests.container.ContainerBarrel;
import aroma1997.betterchests.container.ContainerUpgrades;
import aroma1997.betterchests.inventories.IBetterBarrelInternal;
import aroma1997.betterchests.inventories.IBetterChestInternal;
import aroma1997.betterchests.inventories.InventoryPartBarrel;

public class InventoryBPortableBarrel extends BasicBagInventory implements IBetterChestInternal, IBetterBarrelInternal {

	private final InventoryPartBarrel chestPart;

	public InventoryBPortableBarrel(Entity entity, ItemStack stack) {
		super(entity, stack);
		chestPart = new InventoryPartBarrel(this);
		load();
	}

	@Override
	public UpgradableBlockType getUpgradableBlockType() {
		return UpgradableBlockType.PORTABLE_BARREL;
	}

	@Override
	public Container getContainer(EntityPlayer player, short id) {
		switch (id) {
		case 1:
			return new ContainerUpgrades(this, player);
		default:
			return new ContainerBarrel(this, player);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Gui getGui(EntityPlayer player, short id) {
		switch (id) {
		case 1:
			return new GuiUpgrades(new ContainerUpgrades(this, player));
		default:
			return new GuiBarrel(new ContainerBarrel(this, player));
		}
	}

	@Override
	public InventoryPartBarrel getChestPart() {
		return chestPart;
	}

	@Override
	public IFilter getFilterFor(ItemStack stack) {
		IFilter filter = getFilterPart().getFilterForUpgrade(stack);
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

	@Override
	protected String getDefaultName() {
		return "item.betterchests:betterportablebarrel.name";
	}
}
