package aroma1997.betterchests.bag;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import aroma1997.core.container.ContainerBase;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.IMobileUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.client.gui.GuiBChest;
import aroma1997.betterchests.client.gui.GuiUpgrades;
import aroma1997.betterchests.container.ContainerBChest;
import aroma1997.betterchests.container.ContainerUpgrades;
import aroma1997.betterchests.inventories.IBetterChestInternal;
import aroma1997.betterchests.inventories.InventoryPartChest;

public class InventoryBBag extends BasicBagInventory implements IBetterChestInternal, IMobileUpgradableBlock {

	protected final InventoryPartChest chestInv;

	public InventoryBBag(Entity entity, ItemStack stack) {
		super(entity, stack);
		chestInv = new InventoryPartChest(this);
		load();
	}

	@Override
	public ContainerBase<?> getContainer(EntityPlayer player, short id) {
		switch(id) {
		case 1:
			return new ContainerUpgrades(this, player);
		}
		return new ContainerBChest(this, player);
	}

	@Override
	public Gui getGui(EntityPlayer player, short id) {
		switch(id) {
		case 1:
			return new GuiUpgrades(new ContainerUpgrades(this, player));
		}
		return new GuiBChest(new ContainerBChest(this, player));
	}

	@Override
	public InventoryPartChest getChestPart() {
		return chestInv;
	}

	@Override
	public UpgradableBlockType getUpgradableBlockType() {
		return UpgradableBlockType.BAG;
	}

	@Override
	public Entity getEntity() {
		return entity;
	}

	@Override
	public IFilter getFilterFor(ItemStack stack) {
		return getFilterPart().getFilterForUpgrade(stack);
	}

	@Override
	protected String getDefaultName() {
		return "item.betterchests:betterbag.name";
	}
}
