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
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import aroma1997.core.inventories.ContainerItem;
import aroma1997.core.inventories.ISpecialInventory;

public class ContainerFilter extends ContainerItem {

	protected ContainerFilter(InventoryPlayer playerInv, ISpecialInventory inv,
			int c) {
		super(playerInv, inv, c, true);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public GuiContainer getContainer() {
		return new GUIFilter(this);
	}
	
	@Override
	public int getAmountRows() {
		return 3;
	}
	
	@Override
	public int getAmountPerRow() {
		return 3;
	}
	
	@Override
	public int getYOffset() {
		return super.getXOffset() - 24;
	}

}
