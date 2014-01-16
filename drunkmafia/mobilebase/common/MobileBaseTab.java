package drunkmafia.mobilebase.common;

import drunkmafia.mobilebase.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MobileBaseTab extends CreativeTabs{

	private String tab;
	
	public MobileBaseTab(String label) {
		super(label);
		tab = label;
	}
	
	@Override
	public String getTranslatedTabLabel() {
		return tab;
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.smallTent;
	}
}
