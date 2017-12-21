package aroma1997.betterchests.client.gui;

import aroma1997.betterchests.BlocksItemsBetterChests;
import aroma1997.betterchests.container.ContainerUpgrades;
import aroma1997.betterchests.inventories.IUpgradableBlockInternal;
import aroma1997.betterchests.inventories.InventoryPartUpgrades;
import aroma1997.betterchests.upgrades.PowerUpgradeType;
import aroma1997.core.client.gui.DynamicTexture;
import aroma1997.core.client.gui.GuiBase;
import aroma1997.core.client.gui.IRenderable;
import aroma1997.core.client.gui.StaticTexture;
import aroma1997.core.client.gui.elements.GuiElementExpandableColor;
import aroma1997.core.client.gui.elements.GuiElementProgressBar;
import aroma1997.core.container.ContainerBase;
import aroma1997.core.util.LocalizationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiUpgrades extends GuiBase<ContainerBase<? extends IUpgradableBlockInternal>> {
	public GuiUpgrades(ContainerBase<? extends IUpgradableBlockInternal> container) {
		super(container, 194);
		
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		
		String energyInfoText = LocalizationHelper.localize("betterchests:gui.upgrades.info.power");
		String disableInfoText = LocalizationHelper.localize("betterchests:gui.upgrades.info.disable");
		String filterInfoText = LocalizationHelper.localize("betterchests:gui.upgrades.info.filter");

		GuiElementProgressBar progBar = new GuiElementProgressBar(this, 155, 25, DynamicTexture.ENERGY, container.inventory.getEnergyStorage().getMaxEnergyStored(), container.inventory.getEnergyStorage()::getEnergyStored);
		progBar.addHoverTooltip(() -> LocalizationHelper.localize("betterchests:gui.upgrades.energy.desc"));
		progBar.addProgressTooltip("betterchests:gui.upgrades.energy.tooltip");
		addGuiElement(progBar);

		GuiElementExpandableColor energyInfo = addGuiElement(new GuiElementExpandableColor(this, right(22, 22), 160, fontRenderer.getWordWrappedHeight(energyInfoText, 80)));
		energyInfo.setColor(0xFF00FF00);
		energyInfo.setTexture(IRenderable.getRenderable(PowerUpgradeType.CAPACITOR.getStack()));
		energyInfo.setActualContent(IRenderable.getRenderable(() -> energyInfoText, energyInfo.getLocation()));

		GuiElementExpandableColor disableInfo = addGuiElement(new GuiElementExpandableColor(this, energyInfo.getLocation().below(22, 22), 160, fontRenderer.getWordWrappedHeight(disableInfoText, 60)));
		disableInfo.setColor(0xFFFF0000);
		disableInfo.setTexture(StaticTexture.INFO_BLUE.offset(3, 0).scaled(0.75F));
		disableInfo.setActualContent(IRenderable.getRenderable(() -> LocalizationHelper.localize("betterchests:gui.upgrades.info.disable"), disableInfo.getLocation()));

		GuiElementExpandableColor filterInfo = addGuiElement(new GuiElementExpandableColor(this, disableInfo.getLocation().below(22, 22), 160, fontRenderer.getWordWrappedHeight(filterInfoText, 70)));
		filterInfo.setColor(0xFFFFFF00);
		filterInfo.setTexture(IRenderable.getRenderable(new ItemStack(BlocksItemsBetterChests.filter)));
		filterInfo.setActualContent(IRenderable.getRenderable(() -> LocalizationHelper.localize("betterchests:gui.upgrades.info.filter"), filterInfo.getLocation()));
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		fontRenderer.drawString(LocalizationHelper.localize(getContainer().inventory.getUpgradePart().getName()), ContainerUpgrades.startUpgradeInvX + 2, ContainerUpgrades.startUpgradeInvY - 13, 0x404040);
		fontRenderer.drawString(LocalizationHelper.localize(getContainer().inventory.getFilterPart().getName()), ContainerUpgrades.startFilterInvX + 2, ContainerUpgrades.startFilterInvY - 13, 0x404040);
		fontRenderer.drawString(LocalizationHelper.localize("container.inventory"), ContainerUpgrades.startInvX + 2, ContainerUpgrades.startInvY - 13, 0x404040);

		InventoryPartUpgrades upgrades = getContainer().inventory.getUpgradePart();

		for (int i = 0; i < upgrades.getSizeInventory(); i++) {
			Slot slot = getContainer().inventorySlots.get(i);
			if (upgrades.isUpgradeDisabled(slot.getStack())) {
				drawRect(slot.xPos - 1, slot.yPos - 1, slot.xPos + 16 + 1, slot.yPos + 16 + 1, 0xFFFF0000);
			}
		}
	}
}
