package aroma1997.betterchests.client.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.client.gui.GuiBase;
import aroma1997.core.client.gui.IRenderable;
import aroma1997.core.client.gui.elements.GuiElementExpandableColor;
import aroma1997.core.util.LocalizationHelper;
import aroma1997.betterchests.BlocksItemsBetterChests;
import aroma1997.betterchests.container.ContainerFilter;

@SideOnly(Side.CLIENT)
public class GuiFilter extends GuiBase<ContainerFilter> {
	public GuiFilter(ContainerFilter container) {
		super(container);

		GuiElementToggle isBlacklist = new GuiElementToggle(this, 96, 16, "isBlacklist");
		isBlacklist.setTexture(new IRenderable() {
			IRenderable blacklist = IRenderable.getRenderable(BlocksItemsBetterChests.filter.getBlacklistItemStack());
			IRenderable whitelist = IRenderable.getRenderable(BlocksItemsBetterChests.filter.getWhitelistItemStack());

			@Override
			public void render(int x, int y, Gui gui, TextureManager textureManager) {
				if (isBlacklist.getState()) {
					blacklist.render(x, y, gui, textureManager);
				} else {
					whitelist.render(x, y, gui, textureManager);
				}
			}
		}.offset(6, 2));
		addGuiElement(isBlacklist);

		GuiElementToggle checkDamage = new GuiElementToggle(this, 96, 38, "checkdamage");
		checkDamage.setTexture(checkDamage.getRenderable());
		addGuiElement(checkDamage);

		GuiElementToggle checkNbt = new GuiElementToggle(this, 96, 60, "checknbt");
		checkNbt.setTexture(checkNbt.getRenderable());
		addGuiElement(checkNbt);

		GuiElementExpandableColor upgradeInfo = addGuiElement(new GuiElementExpandableColor(this, right(22, 22), 125, 70));
		upgradeInfo.setColor(0xFF808000);
		upgradeInfo.setTexture(IRenderable.getRenderable(new ItemStack(BlocksItemsBetterChests.crafting)));
		upgradeInfo.setActualContent(IRenderable.getRenderable(() -> LocalizationHelper.localize("betterchests:gui.filter.info.restrictupgrade"), upgradeInfo.getLocation()));
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		fontRenderer.drawString(LocalizationHelper.localize(getContainer().inventory.filterInv.getName()), 11, 8, 0x404040);
		fontRenderer.drawString(LocalizationHelper.localize("container.inventory"), 11, 83, 0x404040);

	}
}
