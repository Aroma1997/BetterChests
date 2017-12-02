package aroma1997.betterchests.upgrades.directional;

import java.util.Collections;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import aroma1997.core.inventory.InvUtil;
import aroma1997.core.util.WorldUtil;
import aroma1997.betterchests.Config;
import aroma1997.betterchests.api.IBetterChest;
import aroma1997.betterchests.api.IFilter;
import aroma1997.betterchests.api.IUpgradableBlock;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.upgrades.DummyUpgradeType;

public class UpgradePlacer extends DirectionalUpgrade {
	public UpgradePlacer() {
		super(true, 1, UpgradableBlockType.CHEST.array, () -> Collections.singletonList(DummyUpgradeType.AI.getStack()));
	}

	@Override
	public void update(IUpgradableBlock chest, ItemStack stack) {
		if (getTickTime(chest, stack) != 0 || !hasUpgradeOperationCost(chest)) {
			return;
		}

		IBetterChest inv = (IBetterChest) chest;

		IFilter filter = ((IBetterChest) chest).getFilterFor(stack);

		int slot = InvUtil.findInInvInternal(inv, null, filter::matchesStack);

		ItemStack targetItem;
		if (slot != -1) {
			targetItem = inv.getStackInSlot(slot);
		} else if (!filter.hasStackFilter()) {
			targetItem = ItemStack.EMPTY;
		} else {
			return;
		}

		EntityPlayerMP player = chest.getFakePlayer();
		if (player == null) {
			return;
		}

		EnumFacing side = getCurrentSide(stack, chest);
		BlockPos targetPos = chest.getPosition().offset(side);

		if (WorldUtil.isBlockAir(chest.getWorldObj(), targetPos)) {
			BlockPos nextPos = targetPos.offset(side);
			if (!WorldUtil.isBlockAir(chest.getWorldObj(), nextPos)) {
				targetPos = nextPos;
			}
		}

		player.inventory.clear();
		player.setHeldItem(EnumHand.MAIN_HAND, targetItem);
		player.rotationPitch = (float) -Math.toDegrees(Math.asin(side.getFrontOffsetY()));
		player.rotationYaw = side.getHorizontalAngle();

		Vec3d playerTargetPos = player.getPositionVector().add(player.getLookVec().normalize());
		player.setPosition(playerTargetPos.x, playerTargetPos.y, playerTargetPos.z);

		if (placeBlock(player, targetPos, side)) {
			drawUpgradeOperationCode(chest);
		}
		if (slot != -1) {
			inv.setInventorySlotContents(slot, player.getHeldItem(EnumHand.MAIN_HAND));
			player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
			for (ItemStack current : InvUtil.getFromInv(player.inventory)) {
				current.setCount(InvUtil.putStackInInventoryInternal(current, inv, false).getCount());
			}
			player.inventory.dropAllItems();
		}
	}

	public boolean placeBlock(EntityPlayerMP player, BlockPos tatgetPos, EnumFacing side) {
		World world = player.world;
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		EnumActionResult result = player.interactionManager.processRightClickBlock(player, world, stack, EnumHand.MAIN_HAND, tatgetPos, side.getOpposite(), .5F, .5F, .5F);

		if (result == EnumActionResult.PASS) {
			result = player.interactionManager.processRightClick(player, world, stack, EnumHand.MAIN_HAND);
		}

		return result != EnumActionResult.PASS;
	}

	@Override
	public int getUpgradeOperationCost() {
		return Config.INSTANCE.energyPlacer;
	}
}
