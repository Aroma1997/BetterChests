package aroma1997.betterchests.client.gui;

import aroma1997.core.client.gui.GuiBase;
import aroma1997.core.util.LocalizationHelper;
import aroma1997.betterchests.container.ContainerBarrel;

public class GuiBarrel extends GuiBase<ContainerBarrel> {
	public GuiBarrel(ContainerBarrel container) {
		super(container, 144);
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		fontRenderer.drawString(LocalizationHelper.localize(getContainer().inventory.getName()), 11, 8, 0x404040);
		fontRenderer.drawString(getContainer().inventory.getChestPart().getAmountDescr(), 110, 25, 0x404040);
		fontRenderer.drawString(LocalizationHelper.localize("container.inventory"), 11, 45, 0x404040);


	}
}