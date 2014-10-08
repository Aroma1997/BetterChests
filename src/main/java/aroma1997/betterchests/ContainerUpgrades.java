/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.core.client.inventories.GUIAromaBasic;
import aroma1997.core.client.inventories.GUIAutoLayout;
import aroma1997.core.client.inventories.RenderHelper;
import aroma1997.core.client.inventories.RenderHelper.Tex;
import aroma1997.core.client.util.Colors;
import aroma1997.core.inventories.AromaContainer;
import aroma1997.core.util.InvUtil;
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
	@SideOnly(Side.CLIENT)
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
		for (Object o : inventorySlots) {
			Slot slot = (Slot) o;
			if (slot != null && chest.isUpgradeDisabled(slot.getStack())) {
				RenderHelper.renderTex(gui, Tex.REDCROSS,
						slot.xDisplayPosition - 1, slot.yDisplayPosition - 1);
			}
		}
	}

	@Override
	public ItemStack slotClick(int par1, int par2, int par3,
			EntityPlayer par4EntityPlayer) {
		if (par1 >= inventorySlots.size() || par1 < 0) {
			return null;
		}

		ItemStack item = getSlot(par1).getStack();
		if (item != null) {
			item = item.copy();
		}
		if (par3 == 1) {
			if (par2 == 1) {
				if (item == null || isRequired(par1)) {
					return null;
				}
				UpgradeHelper.enableUpgrade(item);
				getSlot(par1).decrStackSize(1);
				item.stackSize = 1;
				InvUtil.putIntoFirstSlot(player.inventory, item, false);
			} else if (par2 == 0) {
				if (item == null || isRequired(par1)) {
					return null;
				}
				int amount = getSlot(par1).getStack().stackSize;
				UpgradeHelper.enableUpgrade(item);
				getSlot(par1).decrStackSize(amount);
				item.stackSize = amount;
				InvUtil.putIntoFirstSlot(player.inventory, item, false);
			}
		} else if (par3 == 0) {
			if ((par2 == 0 || par2 == 1)) {
				if (item != null && UpgradeHelper.isUpgrade(item)) {
					IUpgrade upgr = (IUpgrade) item.getItem();
					if (upgr.canBeDisabled(item)) {
						boolean s = !chest.isUpgradeDisabled(item);
						chest.setUpgradeDisabled(item, s);
						return null;
					}
				}
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
