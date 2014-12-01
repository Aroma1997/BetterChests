package aroma1997.betterchests.upgrades;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.core.inventories.ContainerBasic;

public abstract class BasicUpgrade {

	public abstract void updateChest(IBetterChest chest, int tick, World world,
			ItemStack item);

	@SideOnly(Side.CLIENT)
	public void draw(GuiContainer gui, ContainerBasic container, int par1,
			int par2, ItemStack item) {
	}

}
