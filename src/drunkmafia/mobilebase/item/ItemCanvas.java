package drunkmafia.mobilebase.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCanvas extends Item{

	public ItemCanvas() {
		super(ItemInfo.canvas_ID);
		setUnlocalizedName(ItemInfo.canvas_UnlocalizedName);
	}
	
	@Override
	public void getSubItems(int var, CreativeTabs creativeTabs, List list) {
		for(int i = 0; i < ItemInfo.tent_LocalizedName.length; i++){
			ItemStack stack = new ItemStack(this, 1, i);
			stack.setItemName(ItemInfo.canvas_LocalizedName[i]);
			list.add(stack);
		}
	}
}
