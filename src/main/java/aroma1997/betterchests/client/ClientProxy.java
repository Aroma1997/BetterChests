package aroma1997.betterchests.client;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BuiltInModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.client.models.CustomModelLoader;
import aroma1997.core.inventory.InvUtil;
import aroma1997.core.util.registry.TickRegistry;
import aroma1997.betterchests.BetterChests;
import aroma1997.betterchests.BlocksItemsBetterChests;
import aroma1997.betterchests.CommonProxy;
import aroma1997.betterchests.bag.ItemBBag;
import aroma1997.betterchests.chest.TileEntityBBarrel;
import aroma1997.betterchests.chest.TileEntityBChest;
import aroma1997.betterchests.client.model.ModelFilter;
import aroma1997.betterchests.client.model.ModelPortableBarrel;
import aroma1997.betterchests.client.model.TESRBBarrel;
import aroma1997.betterchests.client.model.TESRBChest;
import aroma1997.betterchests.client.model.TESRBTank;
import aroma1997.betterchests.network.PacketOpenBag;
import aroma1997.betterchests.tank.TileEntityBTank;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	public static KeyBinding keyBind;
	private boolean pressed;

	@Override
	public void preInit() {
		CustomModelLoader.INSTANCE.registerModel(new ResourceLocation("betterchests:models/block/betterchest"), (a, b, c) -> new BuiltInModel(TESRBChest.TRANSFORMS, ItemOverrideList.NONE));
		CustomModelLoader.INSTANCE.registerModel(new ResourceLocation("betterchests:models/item/filter"), new ModelFilter());
		CustomModelLoader.INSTANCE.registerModel(new ResourceLocation("betterchests:models/item/betterportablebarrel"), new ModelPortableBarrel());
		keyBind = new KeyBinding("betterchests.keybind.openbag", Keyboard.KEY_ADD, "betterchests:keybind.category");
	}

	@Override
	public void init() {
		new ClientEventListener();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBChest.class, TESRBChest.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBBarrel.class, TESRBBarrel.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBTank.class, TESRBTank.INSTANCE);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(BlocksItemsBetterChests.betterchest), 0, TileEntityBChest.class);

		TickRegistry.CLIENT.addContinuousCallback(() -> {
			if (!pressed && keyBind.isPressed() && Minecraft.getMinecraft().world != null) {
				//Open bag
				int idx = InvUtil.findInInvInternal(Minecraft.getMinecraft().player.inventory, null, stack -> stack.getItem() instanceof ItemBBag);
				if (idx != -1) {
					BetterChests.instance.ph.sendPacketToPlayers(new PacketOpenBag(idx));
				}
			}
			pressed = keyBind.isPressed();
		});
		ClientRegistry.registerKeyBinding(keyBind);
	}
}
