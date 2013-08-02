package aroma1997.betterchests;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;


public class CreativeTabBChest extends CreativeTabs {

	public CreativeTabBChest() {
		super("creativeTabBC");
	}
	
	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(BetterChests.chest);
	}
	
}
