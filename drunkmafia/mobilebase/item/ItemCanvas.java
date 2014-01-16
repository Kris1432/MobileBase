package drunkmafia.mobilebase.item;

import java.util.List;



import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.lib.ItemInfo;
import drunkmafia.mobilebase.lib.ModInfo;

public class ItemCanvas extends Item{

	public ItemCanvas() {
		setCreativeTab(MobileBase.tab);
		setUnlocalizedName(ItemInfo.canvas_UnlocalizedName);
	}
	
	@SideOnly(Side.CLIENT)
	private IIcon[] icons = new IIcon[ItemInfo.canvas_LocalizedName.length];
	
	@Override
	public void registerIcons(IIconRegister register) {
		for(int i = 0; i < icons.length; i++){
			icons[i] = register.registerIcon(ModInfo.MODID + ":" + "canvas\\" + ItemInfo.canvas_LocalizedName[i].toLowerCase());
		}
	}
		
	@Override
	public IIcon getIconFromDamage(int meta) {
		return icons[meta];
	}
	
	@Override
	public void func_150895_a(Item var, CreativeTabs creativeTabs, List list) {
		for(int i = 0; i < ItemInfo.tent_LocalizedName.length; i++){
			ItemStack stack = new ItemStack(this, 1, i);
			stack.func_151001_c(ItemInfo.canvas_LocalizedName[i]); //setName
			list.add(stack);
		}
	}
}
