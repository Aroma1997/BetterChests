/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabBChest extends CreativeTabs {

	public CreativeTabBChest() {
		super("creativeTabBC");
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(BetterChestsItems.chest);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getTranslatedTabLabel() {
		return "creativetab.betterchests:creativetab.name";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return BetterChestsItems.bag;
	}

}
