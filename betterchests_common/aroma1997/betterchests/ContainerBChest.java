package aroma1997.betterchests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;


public class ContainerBChest extends Container {

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return false;
	}
	
}
