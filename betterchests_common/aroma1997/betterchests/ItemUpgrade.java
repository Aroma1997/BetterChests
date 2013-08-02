package aroma1997.betterchests;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class ItemUpgrade extends Item {

	public ItemUpgrade(int id) {
		super(id);
	}
	@Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < Upgrade.values().length; i++) {
        	par3List.add(new ItemStack(this.itemID, 1, i));
        }
    }
	
}
