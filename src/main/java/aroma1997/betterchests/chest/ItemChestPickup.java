package aroma1997.betterchests.chest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import aroma1997.core.item.AromicItem;
import aroma1997.core.util.WorldUtil;
import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.api.IUpgradableBlock;

public class ItemChestPickup extends AromicItem {

	public ItemChestPickup() {
		setUnlocalizedName("betterchests:chestpickup");
		setCreativeTab(BetterChests.creativeTab);
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (world.isRemote) {
			return EnumActionResult.SUCCESS;
		}
		if (canDoDrop(player, world, pos)) {
			ItemStack drop = getDrop(pos, world);
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof IInventory) {
				((IInventory)te).clear();
			}
			world.setBlockToAir(pos);
			WorldUtil.dropItemsRandom(world, drop, pos);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	protected boolean canDoDrop(EntityPlayer player, World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		return te != null &&
				(
				te instanceof IUpgradableBlock //Betterchest
				|| te instanceof TileEntityChest //Vanilla chest
				|| te.getClass().getCanonicalName().startsWith("cpw.mods.ironchest") //IronChest
				);
	}

	private ItemStack getDrop(BlockPos pos, World world) {

		IBlockState state = world.getBlockState(pos);

		Block block = state.getBlock();
		ItemStack stack = block.getItem(world, pos, state);
		TileEntity te = world.getTileEntity(pos);
		if (te == null) {
			return stack;
		}

		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setTag("BlockEntityTag", te.writeToNBT(new NBTTagCompound()));
		return stack;
	}
}
