package aroma1997.betterchests;

import aroma1997.core.util.ItemUtil;
import aroma1997.core.util.ItemUtil.ItemMatchCriteria;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

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
				if (result != null) return null;
				if (ItemUtil.areItemsSameMatching(item, getRecipeOutput(), ItemMatchCriteria.ID)) {
					result = item.copy();
					result.setItemDamage(result.getItemDamage() == 0 ? 1 : 0);
				}
				else {
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

}
