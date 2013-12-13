package drunkmafia.mobilebase.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.lib.ItemInfo;
import drunkmafia.mobilebase.tents.Tent;
import drunkmafia.mobilebase.tents.TentSmall;
import drunkmafia.mobilebase.util.RotationHelper;

public class ItemTent extends Item{
	
	public ItemTent(){
		super(ItemInfo.tent_ID);
		setUnlocalizedName(ItemInfo.tent_UnlocalizedName);
		setCreativeTab(MobileBase.tab);
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return false;
		TentSmall test = new TentSmall();
		System.out.println(RotationHelper.yawToForge(player.rotationYaw).ordinal());
		test.buildTent(world, x, y, z, stack, RotationHelper.yawToForge(player.rotationYaw));
		return true;
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int pass) {
		return Item.bakedPotato.getIcon(stack, pass);
	}
}
