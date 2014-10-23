/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.core.inventories.SlotGhost;

public class SlotFilterUpgrade extends SlotGhost {
	
	public InventoryFilter inv;

	public SlotFilterUpgrade(InventoryFilter par1iInventory, int par2, int par3,
			int par4) {
		super(par1iInventory, par2, par3, par4);
		this.inv = par1iInventory;
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return UpgradeHelper.isUpgrade(par1ItemStack) && ((IUpgrade)par1ItemStack.getItem()).supportsFilter(par1ItemStack, inv.isBlacklist());
	}
	
	protected int maxStackSize() {
		return 1;
	}

}
