package aroma1997.betterchests.client.gui;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.inventory.IInventory;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.client.gui.GuiBase;
import aroma1997.core.client.gui.IRenderable;
import aroma1997.core.client.gui.elements.GuiElementButton;
import aroma1997.core.network.NetworkHelper;
import aroma1997.core.network.packets.PacketContainerUpdate;
import aroma1997.core.util.LocalizationHelper;
import aroma1997.core.util.ReflectionUtil;

@SideOnly(Side.CLIENT)
public class GuiElementToggle extends GuiElementButton {

	private String field;

	public GuiElementToggle(GuiBase<?> gui, int x, int y, String field) {
		super(gui, x, y, 28);
		this.field = field;
		addHoverTooltip(() -> LocalizationHelper.localize("betterchests:gui.filter." + field + "." + getState()));
	}

	@Override
	protected void onButtonClick(int x, int y) {
		IInventory inv = gui.getContainer().inventory;
		Field f;
		try {
			f = ReflectionUtil.getFieldInClassOrSuperclass(inv.getClass(), field);
			f.setAccessible(true);
			f.setBoolean(inv, !f.getBoolean(inv));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		PacketContainerUpdate update = new PacketContainerUpdate(gui.inventorySlots, field -> false, field -> field.getName().equals(this.field));
		NetworkHelper.getCorePacketHandler().sendPacketToPlayers(update);
	}

	public boolean getState() {
		IInventory inv = gui.getContainer().inventory;
		Field f;
		try {
			f = ReflectionUtil.getFieldInClassOrSuperclass(inv.getClass(), field);
			f.setAccessible(true);
			return f.getBoolean(inv);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

	public IRenderable getRenderable() {
		return new IRenderable() {
			@Override
			public void render(int x, int y, Gui gui, TextureManager textureManager) {
				String str = LocalizationHelper.localize("betterchests:gui.filter." + field + ".desc");
				FontRenderer render = Minecraft.getMinecraft().fontRenderer;
				int len = render.getStringWidth(str);
				render.drawString(str, x + location.getWidth() / 2 - (len + 1) / 2, y + 6, getState() ? 0xFF00FF00 : 0xFFFF0000);
			}
		};
	}
}
