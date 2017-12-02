package aroma1997.betterchests.upgrades;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.util.LocalizationHelper;
import aroma1997.core.util.ServerUtil;

public class ItemUpgradeDirectional<T extends Enum<?> & Supplier<BasicUpgrade>> extends ItemUpgrade<T> {

	public ItemUpgradeDirectional(Class<T> clazz) {
		super(clazz);
	}

	public EnumFacing getSide(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null || !nbt.hasKey("dir", NBT.TAG_INT)) {
			return null;
		}
		int val = nbt.getInteger("dir");
		if (val < 0 || val >= EnumFacing.VALUES.length) {
			return null;
		}
		return EnumFacing.VALUES[val];
	}

	public void setSide(ItemStack stack, EnumFacing side) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) {
			stack.setTagCompound(nbt = new NBTTagCompound());
		}

		int target = -1;
		if (side != null) {
			target = side.ordinal();
		}
		nbt.setInteger("dir", target);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote) {
			setSide(stack, null);
			player.sendMessage(ServerUtil.getChatForString(getTooltipName(stack)));
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			ItemStack stack = player.getHeldItem(hand);
			EnumFacing currentSide = getSide(stack);
			if (currentSide == facing) {
				facing = null;
			}
			setSide(stack, facing);

			player.sendMessage(ServerUtil.getChatForString(getTooltipName(stack)));
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(getTooltipName(stack));
	}

	private String getTooltipName(ItemStack stack) {
		return LocalizationHelper.localize("betterchests:tooltip.directionalupgrade." + getSide(stack));
	}
}
