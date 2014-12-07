/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import aroma1997.core.util.ItemUtil;
import aroma1997.core.util.ItemUtil.ItemMatchCriteria;

public class CraftingFilter implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting craftinggrid, World world) {
		return getCraftingResult(craftinggrid) != null;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting craftinggrid) {
		ItemStack result = null;
		for (int i = 0; i < craftinggrid.getSizeInventory(); i++) {
			ItemStack item = craftinggrid.getStackInSlot(i);
			if (item != null) {
				if (result != null)
					return null;
				if (ItemUtil.areItemsSameMatching(item, getRecipeOutput(),
						ItemMatchCriteria.ID)) {
					result = item.copy();
					result.setItemDamage(result.getItemDamage() == 0 ? 1 : 0);
				} else {
					return null;
				}
			}
		}
		return result;
	}

	@Override
	public int getRecipeSize() {
		return 1;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(BetterChestsItems.filter);
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}

}
