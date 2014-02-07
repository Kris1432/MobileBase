package drunkmafia.mobilebase.item;

import java.util.List;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.block.ModBlocks;
import drunkmafia.mobilebase.lib.ItemInfo;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.tents.Tent;
import drunkmafia.mobilebase.tents.TentHelper;

public class ItemBlueprint extends Item{

	public ItemBlueprint(int id) {
		super(id);
		setUnlocalizedName(ItemInfo.bluePrint_UnlocalizedName);
		setCreativeTab(MobileBase.tab);
		setMaxStackSize(1);
	}
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		if(world.isRemote) return;
		
		stack.setTagCompound(new NBTTagCompound());
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(world.isRemote) return stack;
		
		return stack;
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return false;
		
		if(!player.isSneaking()){
			NBTTagCompound tag = stack.getTagCompound();		
			if(tag != null && !tag.hasKey("tentY")){
				if(!tag.hasKey("tentSet") && world.getBlockId(x, y, z) == Block.fence.blockID && world.getBlockId(x, y - 1, z) != Block.fence.blockID ){
					tag.setInteger("postX", x);
					tag.setInteger("postY", y - 1);
					tag.setInteger("postZ", z);
					tag.setBoolean("tentSet", true);
					stack.setTagCompound(tag);
					
					player.sendChatToPlayer(ChatMessageComponent.createFromText("Tent Position Set"));
				}else if(tag.hasKey("tentSet")){
					FMLNetworkHandler.openGui(player, MobileBase.instance, 0, world, x, y, z);
				}
			}else if(tag.hasKey("tentY") && player.capabilities.isCreativeMode){
				
				int direction = TentHelper.convertForgeDirToTentDir(TentHelper.yawToForge(player.rotationYaw));
				
				tag.setString("directionName", TentHelper.yawToForge(player.rotationYaw).toString());
				
				Tent tent = Tent.loadFromNBT(tag);
				
				if(!TentHelper.buildTent(world, x, y, z, stack, direction, tent))
					player.sendChatToPlayer(ChatMessageComponent.createFromText("Area is not clear, please clear any nearby blocks or try another place"));
			}
		}else{
			stack.setTagCompound(new NBTTagCompound());
			stack.setItemName("Blank Blueprint");
		}
		
		return true;
	}	
	
	@Override
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(ModInfo.MODID + ":blueprint");
	}
	
	@Override
	public void getSubItems(int par1, CreativeTabs creativeTabs, List list) {
		ItemStack stack = new ItemStack(this, 1, 0);
        stack.setItemName("Blank Blueprint");
        list.add(stack);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean val) {
		super.addInformation(stack, player, list, val);
		if(player.capabilities.isCreativeMode){
			list.add("Notice: You are in creative meaning");
			list.add("that you can place down a tent with");
			list.add("this blueprint!");
		}
	}
}
