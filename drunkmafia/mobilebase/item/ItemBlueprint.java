package drunkmafia.mobilebase.item;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
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
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		if(world.isRemote) return;
		
		stack.setTagCompound(new NBTTagCompound());
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return false;
		
		if(!player.isSneaking()){
			NBTTagCompound tag = stack.getTagCompound();		
			
			if(tag != null && !tag.hasKey("tentSet") && world.getBlockId(x, y, z) == Block.fence.blockID && world.getBlockId(x, y - 1, z) != Block.fence.blockID ){
				tag.setInteger("postX", x);
				tag.setInteger("postY", y - 1);
				tag.setInteger("postZ", z);
				tag.setBoolean("tentSet", true);
				stack.setTagCompound(tag);
				
				player.sendChatToPlayer(ChatMessageComponent.createFromText("Tent Position Set"));
			}else if(tag != null && tag.hasKey("tentSet")){
				System.out.println("tent already set");
				FMLNetworkHandler.openGui(player, MobileBase.instance, 0, world, x, y, z);
			}
		}else{
			System.out.println("Removing old info");
			stack.setTagCompound(new NBTTagCompound());
		}
		return true;
	}	
	
	@Override
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(ModInfo.MODID + ":blueprint");
	}
}
