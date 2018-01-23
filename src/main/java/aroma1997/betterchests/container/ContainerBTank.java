package aroma1997.betterchests.container;

import net.minecraft.entity.player.EntityPlayer;

import aroma1997.core.container.ContainerBase;
import aroma1997.core.container.elements.ContainerElementTank;
import aroma1997.betterchests.tank.IBetterTankInternal;

public class ContainerBTank extends ContainerBase<IBetterTankInternal> {
	public ContainerBTank(IBetterTankInternal inventory, EntityPlayer player) {
		super(inventory, player);
		addContainerElement(new ContainerElementTank(this, 80, 10, inventory.getTank()));

		layoutPlayerInventory(10, 78, player);
	}
}
