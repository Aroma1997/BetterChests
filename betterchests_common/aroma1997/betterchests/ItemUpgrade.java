package aroma1997.betterchests;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class ItemUpgrade extends Item {

	public ItemUpgrade(int id) {
		super(id);
		setCreativeTab(BetterChests.creativeTabBC);
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
		List par3List, boolean par4) {
		par3List.add(Upgrade.values()[par1ItemStack.getItemDamage()].getTooltip());
	}
	
	@Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < Upgrade.values().length; i++) {
        	par3List.add(new ItemStack(this.itemID, 1, i));
        }
    }
	
	@Override
    public String getItemStackDisplayName(ItemStack par1ItemStack)
    {
        return Upgrade.values()[par1ItemStack.getItemDamage()].getName();
    }
	
}
