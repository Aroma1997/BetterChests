package aroma1997.betterchests.bag;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.util.LocalizationHelper;
import aroma1997.betterchests.client.ClientProxy;

public class ItemBBag extends ItemBBagBase<InventoryBBag> {
	public ItemBBag() {
		setUnlocalizedName("betterchests:betterbag");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(LocalizationHelper.localizeFormatted("betterchests:tooltip.betterbag.keybind", Keyboard.getKeyName(ClientProxy.keyBind.getKeyCode())));
	}

	@Override
	protected InventoryBBag getNewInstance(Entity entity, ItemStack stack) {
		return new InventoryBBag(entity, stack);
	}
}
