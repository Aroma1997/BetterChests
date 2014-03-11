package aroma1997.betterchests;

import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.client.inventories.GUIAromaBasic;
import aroma1997.core.client.inventories.GUIAutoLayout;
import aroma1997.core.client.util.Colors;
import aroma1997.core.inventories.AromaContainer;
import aroma1997.core.util.InvUtil;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerUpgrades extends AromaContainer {

	private final IBetterChest chest;

	private final EntityPlayer player;

	private int upgrades = 0;

	private static final int bufferX = 9;

	private static final int bufferY = 18;

	public ContainerUpgrades(IBetterChest chest, EntityPlayer player) {
		this.chest = chest;
		for (ItemStack item : chest.getUpgrades()) {
			addSlotToContainer(new SlotUpgrades(chest, item, upgrades % 9 * 18
					+ bufferX, upgrades / 9 * 18));
			upgrades++;
		}
		this.player = player;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return chest.isUseableByPlayer(entityplayer);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getContainer() {
		GUIAutoLayout gui = new GUIAutoLayout(this);
		gui.setXSize(9 * 18 + bufferX * 2);
		gui.setYSize(upgrades / 9 * 18 + bufferY * 2 + 45);
		return gui;
	}

	@Override
	public void drawGuiContainerForegroundLayer(GUIAromaBasic gui, int par1,
			int par2) {
		gui.getFontRender()
				.drawString(
						StatCollector
								.translateToLocal("gui.betterchests:upgrades.name"),
						bufferX, -bufferY / 2 - 2, 4210752);
		gui.getFontRender()
				.drawSplitString(
						Colors.RED
								+ StatCollector
										.translateToLocal("gui.betterchests:upgrades.warning"),
						bufferX, bufferY + upgrades / 9 * 18, 162, 4210752);
	}

	@Override
	public ItemStack slotClick(int par1, int par2, int par3,
			EntityPlayer par4EntityPlayer) {
		if (par1 >= inventorySlots.size()) {
			return null;
		}
		if (par3 == 1) {
			if (par2 == 1) {
				ItemStack item = getSlot(par1).getStack();
				if (getSlot(par1) == null || !getSlot(par1).getHasStack()
						|| isRequired(par1)) {
					return null;
				}
				getSlot(par1).decrStackSize(1);
				item.stackSize = 1;
				InvUtil.putIntoFirstSlot(player.inventory, item, false);
			} else if (par2 == 0) {
				ItemStack item = getSlot(par1).getStack();
				if (getSlot(par1) == null || !getSlot(par1).getHasStack()
						|| isRequired(par1)) {
					return null;
				}
				int amount = getSlot(par1).getStack().stackSize;
				getSlot(par1).decrStackSize(amount);
				item.stackSize = amount;
				InvUtil.putIntoFirstSlot(player.inventory, item, false);
			}
		}
		return null;
	}

	private boolean isRequired(int slot) {
		ItemStack item = getSlot(slot).getStack();
		return UpgradeHelper.isRequirement(item, chest);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		return null;
	}

}
