package aroma1997.betterchests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import aroma1997.core.items.AromicItem;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class ItemBetterChestUpgrade extends AromicItem {

	public ItemBetterChestUpgrade() {
		setMaxStackSize(16);
		setCreativeTab(BetterChests.creativeTabBC);
		setNameAndTexture("betterchests:chestUpgrade");
		setRecipe("CCC", "CWC", "CCC", 'C', "cobblestone", 'W', "plankWood");
	}
	
	@Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int X, int Y, int Z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote) return false;
        TileEntity te = world.getTileEntity(X, Y, Z);
        if (te != null && te instanceof TileEntityChest)
        {
            TileEntityChest tec = (TileEntityChest) te;
            if (tec.numPlayersUsing > 0)
            {
                return false;
            }
            
            TileEntityBChest newchest = new TileEntityBChest();
            
            for (int i = 0; i < tec.getSizeInventory(); i++) {
            	newchest.setInventorySlotContents(i, tec.getStackInSlot(i));
            	tec.setInventorySlotContents(i, null);
            }
            int facing = tec.getBlockMetadata();
            world.setBlock(X, Y, Z, Blocks.air, 0, 3);
            tec.updateContainingBlockInfo();
            tec.checkForAdjacentChests();
            world.setBlock(X, Y, Z, BetterChests.chest, facing, 3);
            

            world.setTileEntity(X, Y, Z, newchest);
            if (!player.capabilities.isCreativeMode) {
                stack.stackSize--;
            	
            }
            return true;
        }
        else
        {
            return false;
        }
    }

}
