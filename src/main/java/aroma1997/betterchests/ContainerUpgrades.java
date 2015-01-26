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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.client.inventories.GUIAromaBasic;
import aroma1997.core.client.inventories.GUIAutoLayout;
import aroma1997.core.client.inventories.RenderHelper;
import aroma1997.core.client.inventories.RenderHelper.Tex;
import aroma1997.core.client.inventories.SpecialImagesBase.EnergyBar;
import aroma1997.core.client.util.Colors;
import aroma1997.core.inventories.AromaContainer;

public class ContainerUpgrades extends AromaContainer {

	private final IBetterChest chest;

	final EntityPlayer player;

	private int upgrades = 0;

	private static final int bufferX = 18;

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
		gui.addSpecialImage(new EnergyBar(0, -1, chest));
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
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		return null;
	}

}
