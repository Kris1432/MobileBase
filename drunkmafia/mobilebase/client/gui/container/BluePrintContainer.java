package drunkmafia.mobilebase.client.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class BluePrintContainer extends Container{

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

}
