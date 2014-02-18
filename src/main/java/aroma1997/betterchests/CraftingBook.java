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
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

public class CraftingBook implements IRecipe {
	
	@Override
	public boolean matches(InventoryCrafting inventorycrafting, World world) {
		return getCraftingResult(inventorycrafting) != null;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		ItemStack item = null;
		for (int i = 0; i < inventorycrafting.getSizeInventory(); i++) {
			ItemStack tmp = inventorycrafting.getStackInSlot(i);
			if (tmp == null) {
				continue;
			}
			if (! (tmp.getItem() instanceof ItemEditableBook)) {
				return null;
			}
			if (tmp.getTagCompound() == null) {
				return null;
			}
			if (((NBTTagString) tmp.getTagCompound().getTag("author")).data.equals("Aroma1997")
				&& ((NBTTagString) tmp.getTagCompound().getTag("title")).data.contains("BetterChests")
				|| tmp.getTagCompound().getString("id").equals("BetterChests")) {
				if (item != null) {
					return null;
				}
				item = tmp;
				continue;
			}
			return null;
		}
		if (item == null) {
			return null;
		}
		return BetterChests.getHelpBook();
	}
	
	@Override
	public int getRecipeSize() {
		return 1;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return BetterChests.getHelpBook();
	}
	
}
