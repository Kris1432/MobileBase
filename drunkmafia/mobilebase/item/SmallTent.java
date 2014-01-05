package drunkmafia.mobilebase.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.ModInfo;
import drunkmafia.mobilebase.tents.ModTents;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class SmallTent extends ItemTent{
	
	public SmallTent(){
		super(ItemInfo.tent_ID);
		setUnlocalizedName(ItemInfo.tent_UnlocalizedName);
		setCreativeTab(MobileBase.tab);
		hasSubtypes = true;
	}
	
	@SideOnly(Side.CLIENT)
	private Icon[] icons;
	
	@Override
	public void registerIcons(IconRegister register) {
		icons = new Icon[ItemInfo.canvas_LocalizedName.length];
		for(int i = 0; i < icons.length; i++){
			icons[i] = register.registerIcon(ModInfo.MODID + ":" + "tents/" + ItemInfo.tent_LocalizedName[i].toLowerCase());
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
			stack.setItemName(ItemInfo.tent_LocalizedName[i]);
			list.add(stack);
		}
	}
}
