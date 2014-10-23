/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import aroma1997.core.inventories.ContainerBasic;
import aroma1997.core.inventories.ContainerItem;
import aroma1997.core.inventories.ISpecialInventory;

public class ContainerFilter extends ContainerItem {
	
	public static final int indexCrafting = -375;

	protected ContainerFilter(InventoryPlayer playerInv, ISpecialInventory inv,
			int c) {
		super(playerInv, inv, c, true);
	}
	
	@Override
	public ItemStack slotClick(int par1, int par2, int par3,
			EntityPlayer par4EntityPlayer) {
		if (par1 == indexCrafting) {
			this.inv.setInventorySlotContents(InventoryFilter.SLOT_UPGRADE, new ItemStack(BetterChestsItems.upgrade, 1, Upgrade.CRAFTING.ordinal()));
			
			return null;
		}
		return super.slotClick(par1, par2, par3, par4EntityPlayer);
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

}
