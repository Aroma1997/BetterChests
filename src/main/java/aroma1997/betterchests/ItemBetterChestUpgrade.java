/**
 * The code of BetterChests and all related materials like textures is copyrighted material.
 * It may only be redistributed or used for Commercial purposes with the permission of Aroma1997.
 * 
 * All Rights reserved (c) by Aroma1997
 * 
 * See https://github.com/Aroma1997/BetterChests/blob/master/LICENSE.md for more information.
 */

package aroma1997.betterchests;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import aroma1997.core.items.AromicItem;

public class ItemBetterChestUpgrade extends AromicItem {

	public ItemBetterChestUpgrade() {
		setMaxStackSize(16);
		setCreativeTab(BetterChests.creativeTabBC);
		setUnlocalizedName("betterchests:chestUpgrade");
		setRecipe("CCC", "CWC", "CCC", 'C', "cobblestone", 'W', "plankWood");
		registerModels();
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player,
			World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
			float hitZ) {
		if (world.isRemote)
			return false;
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityChest) {
			TileEntityChest tec = (TileEntityChest) te;
			if (tec.numPlayersUsing > 0) {
				return false;
			}

			TileEntityBChest newchest = new TileEntityBChest();

			for (int i = 0; i < tec.getSizeInventory(); i++) {
				newchest.setStackInSlotWithoutNotify(i, tec.getStackInSlot(i));
				tec.setInventorySlotContents(i, null);
			}
			EnumFacing facing = (EnumFacing) world.getBlockState(pos).getValue(
					BlockChest.FACING);
			world.setBlockState(pos, Blocks.air.getDefaultState(), 3);
			tec.updateContainingBlockInfo();
			tec.checkForAdjacentChests();
			NBTTagCompound nbt = new NBTTagCompound();
			newchest.writeToNBT(nbt);
			IBlockState state = BetterChestsItems.chest.getStateFromMeta(facing
					.getIndex());
			world.setBlockState(pos, state, 3);

			TileEntity te1 = world.getTileEntity(pos);

			te1.readFromNBT(nbt);
			te1.setPos(pos);

			if (!player.capabilities.isCreativeMode) {
				stack.stackSize--;

			}
			return true;
		} else {
			return false;
		}
	}

}
