package aroma1997.betterchests;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;


public class ItemBlockBChest extends ItemBlock {

	public ItemBlockBChest(int id) {
		super(id);
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return "Adjustable Chest";
	}
	
}
