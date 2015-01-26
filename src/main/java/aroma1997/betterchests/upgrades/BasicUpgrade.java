/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

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
