package aroma1997.betterchests;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


public class CraftingBag implements IRecipe {
	
	@Override
	public boolean matches(InventoryCrafting inventorycrafting, World world) {
		return getCraftingResult(inventorycrafting) != null;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		ItemStack bag = null;
		ItemStack upgrade = null;
		for (int i = 0; i < inventorycrafting.getSizeInventory(); i++) {
			ItemStack item = inventorycrafting.getStackInSlot(i);
			if (item != null) {
				if (item.getItem() instanceof ItemUpgrade) {
					if (upgrade != null) {
						return null;
					}
					else {
						upgrade = item;
						continue;
					}
				}
				if (item.getItem() instanceof ItemBag) {
					if (bag != null) {
						return null;
					}
					else {
						bag = item;
						continue;
					}
				}
				
			}
		}
		if (bag == null || upgrade == null) {
			return null;
		}
		ItemBag itemBag = (ItemBag) bag.getItem();
		ItemStack item = bag.copy();
		BagInventory inv = (BagInventory)itemBag.getInventory(item);
		if (item.stackTagCompound == null) {
			item.setTagCompound(new NBTTagCompound());
		}
		inv.readFromNBT(item.stackTagCompound);
		Upgrade upgr = Upgrade.values()[item.getItemDamage()];
		if (inv.getAmountUpgrade(upgr) >= upgr.getMaxAmount() || !upgr.canBagTakeUpgrade()) {
			return null;
		}
		inv.setAmountUpgrade(upgr, inv.getAmountUpgrade(upgr) + 1);
		inv.writeToNBT(item.stackTagCompound);
		return item;
	}
	
	@Override
	public int getRecipeSize() {
		return 2;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}
	
}
