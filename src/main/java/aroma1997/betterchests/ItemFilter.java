/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */
package aroma1997.betterchests;

import java.util.HashMap;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import aroma1997.betterchests.api.ItemUpgradeBasic;
import aroma1997.core.inventories.AromaContainer;
import aroma1997.core.inventories.ISpecialGUIProvider;
import aroma1997.core.inventories.Inventories;

public class ItemFilter extends ItemUpgradeBasic implements ISpecialGUIProvider {

	public ItemFilter() {
		super();
		setCreativeTab(BetterChests.creativeTabBC);
		setMaxStackSize(1);
		setUnlocalizedName("betterchests:filter");
		setHasSubtypes(true);
		registerModels();
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, BlockPos pos, EnumFacing side, float par8, float par9, float par10) {
		onItemRightClick(par1ItemStack, par3World, par2EntityPlayer);
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer thePlayer) {
		if (par2World.isRemote) {
			Inventories.sendItemInventoryOpen(thePlayer.inventory.currentItem);
		}
		return par1ItemStack;
	}

	@Override
	public boolean canChestTakeUpgrade(ItemStack item) {
		return true;
	}

	@Override
	public boolean canBagTakeUpgrade(ItemStack item) {
		return true;
	}

	@Override
	public int getMaxUpgrades(ItemStack item) {
		return -1;
	}

	@Override
	public boolean canBeDisabled(ItemStack stack) {
		return false;
	}
	
	@Override
	public AromaContainer getContainer(EntityPlayer player, int i) {
		InventoryFilter inv = getInventory(player.inventory.getStackInSlot(i));
		return inv.getContainer(player, i);
	}
	
	private static HashMap<ItemStack, InventoryFilter> invs = new HashMap<ItemStack, InventoryFilter>();

	public static InventoryFilter getInventory(ItemStack item) {
		if (!invs.containsKey(item)) {
			InventoryFilter inv = new InventoryFilter(item);
			invs.put(item, inv);
			return inv;
		}
		return invs.get(item);
	}
	
	@Override
    public String getUnlocalizedName(ItemStack item)
    {
        return super.getUnlocalizedName(item) + (item.getItemDamage() == 0 ? ".whitelist" : ".blacklist");
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		for (int i = 0; i <= 1; i++) {
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		
		InventoryFilter inv = getInventory(par1ItemStack);
		
		if (inv.getStackInSlot(InventoryFilter.SLOT_UPGRADE) != null) {
			par3List.add(StatCollector.translateToLocalFormatted("info.betterchests:tooltip.filter.upgrade", inv.getStackInSlot(InventoryFilter.SLOT_UPGRADE).getDisplayName()));
		}
	}

	@Override
	public boolean supportsFilter(ItemStack stack, boolean inverted) {
		return false;
	}

}
