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

import aroma1997.core.inventories.ISpecialInventory;
import aroma1997.core.inventories.ISpecialInventoryProvider;
import aroma1997.core.inventories.Inventories;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBag extends Item implements ISpecialInventoryProvider {
	
	public ItemBag(int id) {
		super(id);
		setMaxStackSize(1);
		setCreativeTab(BetterChests.creativeTabBC);
	}
	
	@Override
	public boolean
	onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World,
		int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		onItemRightClick(par1ItemStack, par3World, par2EntityPlayer);
		return true;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
		EntityPlayer par3EntityPlayer)
	{
		Inventories.openContainerAtPlayer(par3EntityPlayer, par3EntityPlayer.inventory.currentItem);
		return par1ItemStack;
	}
	
	@Override
	public ISpecialInventory getInventory(EntityPlayer player, int id) {
		return getInventory(player.inventory.getStackInSlot(id));
	}
	
	public BagInventory getInventory(ItemStack item) {
		return BagInventory.getInvForItem(item);// new BagInventory(item);
	}
	
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4,
		boolean par5) {
		BagInventory inv = getInventory(par1ItemStack);
		inv.onUpdate();
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return StatCollector.translateToLocal("item.betterchests:bag.name");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		itemIcon = par1IconRegister.registerIcon(Reference.MOD_ID + ":bag");
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
		List par3List, boolean par4) {
		BagInventory inv = getInventory(par1ItemStack);
		for (Upgrade upgrade : Upgrade.values()) {
			int amount = inv.getAmountUpgrade(upgrade);
			if (amount > 0) {
				if (upgrade.getMaxAmount() == 1) {
					par3List.add(upgrade.getName());
				}
				else {
					par3List.add(upgrade.getName() + " (" + amount + ")");
				}
			}
		}
	}
	
}
