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
	
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int X, int Y, int Z, int side, float hitX, float hitY, float hitZ)
    {
		if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemUpgrade)) return super.onItemUseFirst(stack, player, world, X, Y, Z, side, hitX, hitY, hitZ);
		
		TileEntityBChest te = (TileEntityBChest) world.getBlockTileEntity(X, Y, Z);
		boolean tmp =te.upgrade(player, world);
		if (tmp) {
			stack.stackSize -= 1;
		}
		return tmp;
    }
	
}
