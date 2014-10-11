package aroma1997.betterchests;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import aroma1997.core.inventories.SlotGhost;

public class SlotFilterUpgrade extends SlotGhost {

	public SlotFilterUpgrade(IInventory par1iInventory, int par2, int par3,
			int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return UpgradeHelper.isUpgrade(par1ItemStack);
	}

}
