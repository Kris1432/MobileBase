package drunkmafia.mobilebase.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.tents.Tent;
import drunkmafia.mobilebase.tents.TentSmall;
import drunkmafia.mobilebase.util.RotationHelper;

public class ItemTent extends Item{
	
	public ItemTent(){
		super(ItemInfo.tent_ID);
		setUnlocalizedName(ItemInfo.tent_UnlocalizedName);
		setCreativeTab(MobileBase.tab);
		hasSubtypes = true;
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return false;
		if(player.isSneaking()){
			System.out.println(world.getBlockMetadata(x, y, z));
		}else{
			TentSmall test = new TentSmall();
			test.buildTent(world, x, y, z, stack, RotationHelper.yawToForge(player.rotationYaw));
			stack.stackSize--;
		}
		return true;
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int pass) {
		return Item.bakedPotato.getIcon(stack, pass);
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
