package aroma1997.betterchests.client.gui;

import aroma1997.core.client.gui.GuiBase;
import aroma1997.core.util.LocalizationHelper;
import aroma1997.betterchests.container.ContainerBTank;

public class GuiBTank extends GuiBase<ContainerBTank> {
	public GuiBTank(ContainerBTank container) {
		super(container, 158);
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(LocalizationHelper.localize(getContainer().inventory.getName()), 12, 8, 0x404040);
		fontRenderer.drawString(LocalizationHelper.localize("container.inventory"), 12, 65, 0x404040);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
}
