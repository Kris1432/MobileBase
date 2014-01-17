package drunkmafia.mobilebase.item;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.block.ModBlocks;
import drunkmafia.mobilebase.lib.ItemInfo;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.tents.Tent;
import drunkmafia.mobilebase.util.Vector3;

public class ItemBlueprint extends Item{

	public ItemBlueprint() {
		super(ItemInfo.bluePrint_ID);
		setUnlocalizedName(ItemInfo.bluePrint_UnlocalizedName);
		setCreativeTab(MobileBase.tab);
	}
	
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return false;
		if(world.getBlockId(x, y, z) == ModBlocks.tentPost.blockID){
			NBTTagCompound tag;
			
			if(stack.getTagCompound() != null)
				tag = stack.getTagCompound();
			else
				tag = new NBTTagCompound();
			
			tag.setInteger("postX", x);
			tag.setInteger("postY", y - 1);
			tag.setInteger("postZ", z);
			
			stack.setTagCompound(tag);
			
			FMLNetworkHandler.openGui(player, MobileBase.instance, 0, world, x, y, z);
		}
		return true;
	}

	@Override
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(ModInfo.MODID + ":blueprint");
	}	
}
