package aroma1997.betterchests.chest;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import aroma1997.core.item.AromicItem;
import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.BlocksItemsBetterChests;

public class ItemChestUpgrade extends AromicItem {

	public ItemChestUpgrade() {
		setUnlocalizedName("betterchests:chestupgrade");
		setCreativeTab(BetterChests.creativeTab);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (world.isRemote) {
			return EnumActionResult.PASS;
		}
		IBlockState state = world.getBlockState(pos);
		TileEntity te = world.getTileEntity(pos);
		if (state.getBlock() == Blocks.CHEST && te instanceof TileEntityChest) {
			TileEntityChest chest = (TileEntityChest) te;
			ItemStack[] items = new ItemStack[chest.getSizeInventory()];
			for (int i = 0; i < items.length; i++) {
				items[i] = chest.getStackInSlot(i);
				chest.setInventorySlotContents(i, ItemStack.EMPTY);
			}
			IBlockState newState = BlocksItemsBetterChests.betterchest.getDefaultState().withProperty(BlockBetterChest.directions, state.getValue(BlockChest.FACING));
			world.setBlockState(pos, newState, 2);
			TileEntityBChest newte = new TileEntityBChest();
			world.setTileEntity(pos, newte);
			for (int i = 0; i < items.length; i++) {
				newte.getChestPart().setInventorySlotContents(i, items[i]);
			}
			world.notifyBlockUpdate(pos, state, newState, 1);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
}
