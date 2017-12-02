package aroma1997.betterchests.client.gui;

import net.minecraft.inventory.Slot;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.client.gui.GuiBase;
import aroma1997.core.util.LocalizationHelper;
import aroma1997.betterchests.container.ContainerBChest;

@SideOnly(Side.CLIENT)
public class GuiBChest extends GuiBase<ContainerBChest> {
	public GuiBChest(ContainerBChest container) {
		super(container, container.width, container.height);
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int beginExternalIndex = getContainer().inventory.getChestPart().getAccessibleBegin();
		for (int i = 0; i < beginExternalIndex; i++) {
			Slot slot = getContainer().getSlot(i);
			drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, 0x80800000);
		}

		fontRenderer.drawString(LocalizationHelper.localize(getContainer().inventory.getName()), getContainer().startChestInvX + 2, getContainer().startChestInvY - 13, 0x404040);
		fontRenderer.drawString(LocalizationHelper.localize("container.inventory"), getContainer().startInvX + 2, getContainer().startInvY - 13, 0x404040);

		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
}
