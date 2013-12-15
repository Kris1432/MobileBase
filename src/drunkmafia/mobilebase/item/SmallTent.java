package drunkmafia.mobilebase.item;

import java.util.List;

import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.tents.ModTents;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SmallTent extends ItemTent{
	
	public SmallTent(){
		super(ItemInfo.tent_ID);
		setUnlocalizedName(ItemInfo.tent_UnlocalizedName);
		setCreativeTab(MobileBase.tab);
		hasSubtypes = true;
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		return placeTentDown(stack, player, world, x, y, z, side, hitX, hitY, hitZ, ModTents.smallTent);
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
