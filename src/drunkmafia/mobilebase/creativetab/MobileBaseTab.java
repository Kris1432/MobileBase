package drunkmafia.mobilebase.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MobileBaseTab extends CreativeTabs{

	public MobileBaseTab(String label) {
		super(label);
	}
	
	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(Item.axeDiamond);
	}
}
