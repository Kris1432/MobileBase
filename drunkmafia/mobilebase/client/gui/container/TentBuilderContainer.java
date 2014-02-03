package drunkmafia.mobilebase.client.gui.container;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import drunkmafia.mobilebase.client.gui.container.slot.BlueprintSlot;
import drunkmafia.mobilebase.client.gui.container.slot.EnderSlot;
import drunkmafia.mobilebase.client.gui.container.slot.FenceSlot;
import drunkmafia.mobilebase.client.gui.container.slot.WoolSlot;
import drunkmafia.mobilebase.item.ItemBlueprint;
import drunkmafia.mobilebase.tileentity.TentBuilderTile;

public class TentBuilderContainer extends Container{

	private TentBuilderTile tile;
	
	public TentBuilderContainer(TentBuilderTile tile, EntityPlayer player) {
		this.tile = tile;
		
		InventoryPlayer invPlayer = player.inventory;
		
		addSlotToContainer(new BlueprintSlot(tile, 0, 13, 18));
		addSlotToContainer(new WoolSlot(tile, 1, 13, 42));
		addSlotToContainer(new FenceSlot(tile, 2, 13, 66));
		addSlotToContainer(new EnderSlot(tile, 3, 13, 89));
		addSlotToContainer(new Slot(tile, 4, 152, 118));
		
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
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		Slot slot = getSlot(i);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			ItemStack result = stack.copy();
			
			if (i >= 36) {
				if (!mergeItemStack(stack, 0, 36, false)) {
					return null;
				}
			}else if(!mergeItemStack(stack, 36, 36 + tile.getSizeInventory(), false)) {
				return null;
			}
			
			if (stack.stackSize == 0) {
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}
			
			slot.onPickupFromSlot(player, stack);
			
			return result;
		}
		
		return null;
	}
	
	
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

}
