/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests;


import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemBlockBChest extends ItemBlock {
	
	public ItemBlockBChest(int id) {
		super(id);
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return StatCollector.translateToLocal("tile.betterchests:chest.name");
	}
	
	@Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
		par3List.add(BetterChests.getHelpBook());
		super.getSubItems(par1, par2CreativeTabs, par3List);
    }
	
}
