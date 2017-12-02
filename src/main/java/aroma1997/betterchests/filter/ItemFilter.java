package aroma1997.betterchests.filter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;

import aroma1997.core.inventory.ItemInventory;
import aroma1997.core.item.AromicItemInventory;
import aroma1997.betterchests.BetterChests;

public class ItemFilter extends AromicItemInventory {

	private static final Map<ItemStack, InventoryFilter> lookup = new WeakHashMap<>();

	public ItemFilter() {
		setCreativeTab(BetterChests.creativeTab);
		setUnlocalizedName("betterchests:filter");
	}

	public static synchronized InventoryFilter getInventoryFor(ItemStack stack) {
		InventoryFilter bag = lookup.get(stack);
		if (bag != null && bag.item.get() != stack) {
			bag = null;
		}

		if (bag == null) {
			bag = new InventoryFilter(stack);
			lookup.put(stack, bag);
		}
		return bag;
	}
	
	@Override
	public ItemInventory getGuiProvider(EntityPlayer player, ItemStack stack) {
		return getInventoryFor(stack);
	}

	public ItemStack getBlacklistItemStack() {
		ItemStack stack = getWhitelistItemStack();
		InventoryFilter inv = getInventoryFor(stack);
		inv.isBlacklist = true;
		inv.markDirty();
		return stack;
	}

	public ItemStack getWhitelistItemStack() {
		return new ItemStack(this);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (!isInCreativeTab(tab)) {
			return;
		}
		items.add(getWhitelistItemStack());
		items.add(getBlacklistItemStack());
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		InventoryFilter inv = getInventoryFor(stack);
		String postfix = inv.isBlacklist() ? "blacklist" : "whitelist";
		return super.getUnlocalizedName(stack) + "." + postfix;
	}

	@Override
	public List<Tuple<ItemStack, String>> getModels() {
		ItemStack stack = new ItemStack(this);
		return Collections.singletonList(new Tuple<>(stack, super.getUnlocalizedName(stack).substring(5)));
	}
}
