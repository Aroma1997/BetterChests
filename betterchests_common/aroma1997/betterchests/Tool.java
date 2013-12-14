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

import aroma1997.core.util.AromaRegistry;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;


public enum Tool {
	
	HAMMER(ItemHammer.class, new Object[] {"UUU", " S ", " S ", 'U', Upgrade.BASIC.getItem(), 'S', "stickWood"});
	
	private ToolItem tool;
	
	private Object[] crafting;
	
	private Tool(Class<? extends ToolItem> claSS, Object[] crafting) {
		try {
			tool = claSS.newInstance();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		this.crafting = crafting;
	}
	
	public ToolItem getTool() {
		return tool;
	}
	
	public String getName() {
		return StatCollector.translateToLocal("item.betterchests:upgrade." + toString() + ".name");
	}

	public String getTexture() {
		return Reference.MOD_ID + ":" + toString().toLowerCase();
	}
	
	public static void addBookDescription(List<String> list) {
		for (Tool tool : values()) {
			list.add("book.betterchests:tool." + tool);
		}
	}
	
	public ItemStack getItem() {
		return new ItemStack(BetterChests.tool, 1, ordinal());
	}
	
	static void generateRecipes() {
		for (Tool tool : values()) {
			AromaRegistry.registerShapedOreRecipe(tool.getItem(), tool.crafting);
		}
	}
	
}
