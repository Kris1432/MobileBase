package drunkmafia.mobilebase.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.ModInfo;

public class ItemCanvas extends Item{

	public ItemCanvas() {
		super(ItemInfo.canvas_ID);
		setCreativeTab(MobileBase.tab);
		setUnlocalizedName(ItemInfo.canvas_UnlocalizedName);
	}
	
	@SideOnly(Side.CLIENT)
	private Icon[] icons;
	
	@Override
	public void registerIcons(IconRegister register) {
		icons = new Icon[ItemInfo.canvas_LocalizedName.length];
		for(int i = 0; i < icons.length; i++){
			icons[i] = register.registerIcon(ModInfo.MODID + ":" + "canvas/" + ItemInfo.canvas_LocalizedName[i].toLowerCase());
		}
	}
		
	@Override
	public Icon getIconFromDamage(int meta) {
		return icons[meta];
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
