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
import drunkmafia.mobilebase.tents.Tent;

public class SmallTent extends ItemTent{
	
	private static int[][][] structure = {
		{
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1}
		},
		{
			{1,1,1,1,1,1,1,1,1},
			{1,5,5,5,5,5,5,5,1},
			{1,5,5,5,5,5,5,5,1},
			{1,5,5,5,5,5,5,5,1},
			{1,5,5,5,-1,5,5,5,1},
			{1,5,5,5,5,5,5,5,1},
			{1,5,5,5,5,5,5,5,1},
			{1,5,5,5,3,5,5,5,1},
			{1,1,1,1,5,1,1,1,1}
		},
		{
			{0,1,1,1,1,1,1,1,0},
			{0,1,5,5,5,5,5,1,0},
			{0,1,5,5,5,5,5,1,0},
			{0,1,5,5,5,5,5,1,0},
			{0,1,5,5,2,5,5,1,0},
			{0,1,5,5,5,5,5,1,0},
			{0,1,5,5,5,5,5,1,0},
			{0,1,5,5,3,5,5,1,0},
			{0,1,1,1,5,1,1,1,0}
		},
		{
			{0,0,1,1,1,1,1,0,0},
			{0,0,1,5,5,5,1,0,0},
			{0,0,1,5,5,5,1,0,0},
			{0,0,1,5,5,5,1,0,0},
			{0,0,1,5,2,5,1,0,0},
			{0,0,1,5,5,5,1,0,0},
			{0,0,1,5,5,5,1,0,0},
			{0,0,1,5,5,5,1,0,0},
			{0,0,1,1,1,1,1,0,0}
		},
		{
			{0,0,0,1,1,1,0,0,0},
			{0,0,0,1,5,1,0,0,0},
			{0,0,0,1,5,1,0,0,0},
			{0,0,0,1,5,1,0,0,0},
			{0,0,0,1,2,1,0,0,0},
			{0,0,0,1,5,1,0,0,0},
			{0,0,0,1,5,1,0,0,0},
			{0,0,0,1,5,1,0,0,0},
			{0,0,0,1,1,1,0,0,0}
		},
		{
			{0,0,0,0,1,0,0,0,0},
			{0,0,0,0,1,0,0,0,0},
			{0,0,0,0,1,0,0,0,0},
			{0,0,0,0,1,0,0,0,0},
			{0,0,0,0,1,0,0,0,0},
			{0,0,0,0,1,0,0,0,0},
			{0,0,0,0,1,0,0,0,0},
			{0,0,0,0,1,0,0,0,0},
			{0,0,0,0,1,0,0,0,0}
		}
	};
	
	public SmallTent(){
		setUnlocalizedName(ItemInfo.tent_UnlocalizedName);
		setCreativeTab(MobileBase.tab);
		hasSubtypes = true;
		tent = new Tent(structure);		
	}
	
	@SideOnly(Side.CLIENT)
	private IIcon[] icons = new IIcon[ItemInfo.canvas_LocalizedName.length];
	
	@Override
	public void registerIcons(IIconRegister register) {
		for(int i = 0; i < icons.length; i++){
			icons[i] = register.registerIcon(ModInfo.MODID + ":" + "tents/" + ItemInfo.tent_LocalizedName[i].toLowerCase());
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
			stack.func_151001_c(ItemInfo.tent_LocalizedName[i]); //setItemName
			list.add(stack);
		}
	}
}
