package aroma1997.betterchests;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


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
	
	private Icon[] itemIcons;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		itemIcons = new Icon[Upgrade.values().length];
		for (int i = 0; i < itemIcons.length; i++) {
			itemIcons[i] = iconRegister.registerIcon(Upgrade.values()[i].getTexture());
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public Icon getIconFromDamage(int par1)
    {
        return this.itemIcons[par1];
    }
	
}
