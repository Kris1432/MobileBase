package drunkmafia.mobilebase.client.gui.container.slot;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class WoolSlot extends Slot{

	public WoolSlot(IInventory inventory, int id, int x, int y) {
		super(inventory, id, x, y);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem().itemID == Block.cloth.blockID;
	}
}
