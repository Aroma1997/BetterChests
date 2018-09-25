package aroma1997.betterchests.client;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import aroma1997.core.coremod.MCPNames;
import aroma1997.core.util.LocalizationHelper;
import aroma1997.betterchests.api.IUpgrade;
import aroma1997.betterchests.api.UpgradableBlockType;
@SideOnly(Side.CLIENT)
public class ClientEventListener {

	static Set<Integer> entitiesWithBag = Collections.emptySet();
	/** Render layer for entites with attached jetpacks as chestplates */
	private LayerBag render;
	/** {@link RenderLivingBase#layerRenderers}, because Mojang removed the ability to easily remove from it */
	private static Field renderLayers;

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
	@SideOnly(Side.CLIENT)
	public void render(RenderLivingEvent.Pre<EntityLivingBase> event) throws NoSuchFieldException {
		EntityLivingBase entity = event.getEntity();

		if (entitiesWithBag.contains(entity.getEntityId())) {
			render = new LayerBag();
			if (renderLayers == null) {
				renderLayers = RenderLivingBase.class.getDeclaredField(MCPNames.field("field_177097_h"));
				renderLayers.setAccessible(true);
			}
			event.getRenderer().addLayer(render);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderPost(RenderLivingEvent.Post<EntityLivingBase> event) throws IllegalAccessException {
		if (render != null) {
			boolean success = ((List<LayerRenderer<?>>)renderLayers.get(event.getRenderer())).remove(render);
			assert success;
			render = null;
		}
	}

}
