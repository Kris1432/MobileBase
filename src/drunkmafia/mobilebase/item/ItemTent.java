package drunkmafia.mobilebase.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.tents.ModTents;
import drunkmafia.mobilebase.tents.TentHelper;
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
			NBTTagCompound tag;
			if(stack.getTagCompound() == null){
				tag = new NBTTagCompound();
			}else
				tag = stack.getTagCompound();
			
			ForgeDirection direction;
			
			if(tag.hasKey("direction"))
				direction = ForgeDirection.values()[tag.getInteger("direction")];
			else
				direction = RotationHelper.yawToForge(player.rotationYaw);
			
			if(TentHelper.buildTent(world, x, y, z, stack, direction, ModTents.smallTent))
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
