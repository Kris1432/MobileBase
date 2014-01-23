package drunkmafia.mobilebase.client.gui.container.slot;

import drunkmafia.mobilebase.item.ItemBlueprint;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;

public class EnderSlot extends Slot{

	public EnderSlot(IInventory inventory, int id, int x, int y) {
		super(inventory, id, x, y);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof ItemEnderPearl;
	}

}
