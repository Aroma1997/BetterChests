package aroma1997.betterchests.chest;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import aroma1997.core.container.ContainerHelper;
import aroma1997.core.inventory.InvUtil;
import aroma1997.core.util.ItemUtil;
import aroma1997.core.util.WorldUtil;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.betterchests.inventories.InventoryPartBarrel;

public class BlockBBarrel extends BlockChestBase {
	public BlockBBarrel() {
		super(Material.ROCK);
		setUnlocalizedName("betterchests:betterbarrel");
		setHardness(2.5F);
	}

	@Override
	public Class<? extends TileEntity> getTeClass() {
		return TileEntityBBarrel.class;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityBBarrel) {
			TileEntityBBarrel chest = (TileEntityBBarrel) te;
			ItemStack currentItem = player.getHeldItem(hand);
			if (currentItem.getItem() instanceof IUpgrade) {
				IUpgrade upgrade = (IUpgrade) currentItem.getItem();
				ItemStack toPut = currentItem.copy();
				toPut.setCount(1);
				if (upgrade.canBePutInChest(chest, toPut)) {
					if (InvUtil.putStackInInventoryFirst(toPut, chest.getUpgradePart(), false, false, false, null).isEmpty()) {
						currentItem.setCount(currentItem.getCount() - 1);
						chest.markDirty();
						return true;
					}
				}
			}
			if (player.isSneaking()) {
				ContainerHelper.openGui(chest, player, (short) 0);
			} else {
				ItemStack stack = player.getHeldItem(hand);
				if (stack.isEmpty()) {
					stack = chest.getChestPart().getDummy();
					for (ItemStack other : InvUtil.getFromInv(player.inventory)) {
						if (ItemUtil.areItemsSameMatchingIdDamageNbt(stack, other)) {
							ItemStack curr = InvUtil.putStackInInventoryInternal(other, chest.getChestPart(), false);
							other.setCount(curr.getCount());
							if (!other.isEmpty()) {
								break;
							}
						}
					}
				} else if (chest.getChestPart().isItemValidForSlot(0, stack)) {
					stack.setCount(InvUtil.putStackInInventoryInternal(stack, chest.getChestPart(), false).getCount());
				}
			}
		}
		return true;
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		if (world.isRemote) {
			return;
		}
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityBBarrel) {
			TileEntityBBarrel chest = (TileEntityBBarrel)te;
			InventoryPartBarrel part = chest.getChestPart();
			ItemStack stack = part.getDummy();
			int targetAmount = player.isSneaking() ? 1 : stack.getMaxStackSize();
			int gottenAmount = 0;
			for (int i = 0; i < part.getSizeInventory() && !part.isEmpty(); i++) {
				if (ItemUtil.areItemsSameMatchingIdDamageNbt(stack, part.getStackInSlot(i))) {
					gottenAmount += part.decrStackSize(i, targetAmount - gottenAmount).getCount();
					if (gottenAmount >= targetAmount) {
						break;
					}
				}
			}
			if (gottenAmount > 0) {
				Vec3d vec = new Vec3d(pos);
				vec = vec.add(player.getPositionVector().subtract(vec).normalize());
				ItemStack drop = stack.copy();
				drop.setCount(gottenAmount);
				WorldUtil.dropItems(world, drop, vec.x, vec.y, vec.z);
				chest.getChestPart().markDirty();
			}
		}
	}
}
