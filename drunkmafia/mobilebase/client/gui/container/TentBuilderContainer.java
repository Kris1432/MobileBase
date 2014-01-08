package drunkmafia.mobilebase.client.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import drunkmafia.mobilebase.tileentity.TentBuilderTile;

public class TentBuilderContainer extends Container{

	private TentBuilderTile tile;
	
	public TentBuilderContainer(TentBuilderTile tile, EntityPlayer player) {
		this.tile = tile;
		
		InventoryPlayer invPlayer = player.inventory;
		
		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(invPlayer, x, 8 + 18 * x, 194));
		}
		
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, 8 + 18 * x, 136 + y * 18));
			}
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

}
