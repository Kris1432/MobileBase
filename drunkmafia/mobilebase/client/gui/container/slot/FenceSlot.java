package drunkmafia.mobilebase.client.gui.container.slot;

import drunkmafia.mobilebase.item.ItemBlueprint;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class FenceSlot extends Slot{

	public FenceSlot(IInventory inventory, int id, int x, int y) {
		super(inventory, id, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem().itemID == Block.fence.blockID;
	}

}
