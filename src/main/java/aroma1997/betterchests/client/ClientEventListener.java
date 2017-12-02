package aroma1997.betterchests.client;

import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.util.LocalizationHelper;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.betterchests.api.UpgradableBlockType;
import aroma1997.betterchests.bag.ItemBBag;


import static aroma1997.core.client.models.ModelHelper.ONE_PIXEL;
@SideOnly(Side.CLIENT)
public class ClientEventListener {

	public ClientEventListener() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void drawTooltips(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.getItem() instanceof IUpgrade) {
			IUpgrade upgrade = (IUpgrade) stack.getItem();
			List<String> tooltips = event.getToolTip();

			Collection<UpgradableBlockType> compatibleBlocks = upgrade.getCompatibleTypes(stack);
			for (UpgradableBlockType type : compatibleBlocks) {
				tooltips.add(LocalizationHelper.localize("betterchests:tooltip.compatible." + type.name().toLowerCase()));
			}

			int maxUpgrades = upgrade.getMaxAmountUpgrades(stack);
			if (maxUpgrades > 0 && maxUpgrades < Integer.MAX_VALUE) {
				tooltips.add(LocalizationHelper.localizeFormatted("betterchests:tooltip.maxUpgrades", maxUpgrades));
			}

			Collection<ItemStack> requiredUpgrades = upgrade.getRequiredUpgrades(stack);
			if (!requiredUpgrades.isEmpty()) {
				tooltips.add(LocalizationHelper.localize("betterchests:tooltip.requiredUpgrades"));
				for (ItemStack req : requiredUpgrades) {
					tooltips.add("  " + req.getDisplayName());
				}
			}
		}
	}

	@SubscribeEvent
	public void renderPlayer(RenderPlayerEvent.Post event) {
		EntityPlayer player = event.getEntityPlayer();

		ItemStack bag = getBagInInv(player);

		if (!bag.isEmpty()) {
			GL11.glPushMatrix();

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(0.0F, 17 * ONE_PIXEL, 0.0F);
			GL11.glRotatef( - player.renderYawOffset, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, 0.0F, -4 * ONE_PIXEL);
			if (player.isSneaking()) {
				GL11.glTranslatef(0, -5 * ONE_PIXEL, 0);
				GL11.glRotatef(28.6F, 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0, 0, -3 * ONE_PIXEL);
			}

			Minecraft.getMinecraft().getRenderItem().renderItem(bag, TransformType.FIXED);
			GL11.glPopMatrix();
		}
	}

	private ItemStack getBagInInv(EntityPlayer player) {
		InventoryPlayer inv = player.inventory;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack == player.getHeldItemOffhand() || stack == player.getHeldItemMainhand() || stack == player.getItemStackFromSlot(EntityEquipmentSlot.HEAD)) {
				continue;
			}

			if (stack.getItem() instanceof ItemBBag) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

}
