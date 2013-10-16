package aroma1997.betterchests;

import aroma1997.core.inventories.ISpecialInventory;
import aroma1997.core.inventories.ISpecialInventoryProvider;
import aroma1997.core.inventories.Inventories;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;


public class ItemBag extends Item implements ISpecialInventoryProvider {
	
	public ItemBag(int id) {
		super(id);
		setMaxStackSize(1);
		setCreativeTab(BetterChests.creativeTabBC);
	}
	
	@Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
		onItemRightClick(par1ItemStack, par3World, par2EntityPlayer);
    	return true;
    }
	
	@Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
		Inventories.openContainerAtPlayer(par3EntityPlayer, par3EntityPlayer.inventory.currentItem);
		return par1ItemStack;
    }

	@Override
	public ISpecialInventory getInventory(EntityPlayer player, int id) {
		return getInventory(player.inventory.getStackInSlot(id));
	}
	
	public ISpecialInventory getInventory(ItemStack item) {
		return new BagInventory();
	}
	
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if (par1ItemStack.getTagCompound() == null) {
			par1ItemStack.setTagCompound(new NBTTagCompound());
		}
		BagInventory inv = (BagInventory) getInventory(par1ItemStack);
		inv.readFromNBT(par1ItemStack.getTagCompound());
		inv.onUpdate();
		inv.writeToNBT(par1ItemStack.getTagCompound());
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return StatCollector.translateToLocal("item.betterchests:bag.name");
	}
	
}
