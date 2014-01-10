package drunkmafia.mobilebase.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import drunkmafia.mobilebase.MobileBase;
import drunkmafia.mobilebase.lib.ItemInfo;
import drunkmafia.mobilebase.lib.ModInfo;
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

		if(!player.isSneaking()){
			NBTTagCompound tag;
			if(stack.getTagCompound() == null)
				tag = new NBTTagCompound();
			else
				tag = stack.getTagCompound();
			
			if(!tag.hasKey("pos1")){
				tag.setIntArray("pos1", getArrayCoords(x, y, z));
				player.sendChatToPlayer(ChatMessageComponent.createFromText("Position 1 set"));
			}else if(!tag.hasKey("pos2") && y != tag.getIntArray("pos1")[2]){
				tag.setIntArray("pos2", getArrayCoords(x, y, z));
				player.sendChatToPlayer(ChatMessageComponent.createFromText("Position 2 set"));
			}else{
				player.sendChatToPlayer(ChatMessageComponent.createFromText("Blueprint already exists, shift-click to remove plan"));
			}
			stack.setTagCompound(tag);
			
			if(tag.hasKey("pos1") && tag.hasKey("pos2"))
				isTent(world, tag);
			
		}else{
			player.sendChatToPlayer(ChatMessageComponent.createFromText("Whipped Plan"));
			stack.setTagCompound(new NBTTagCompound());
		}
		
		return true;
	}
	
	public boolean isTent(World world, NBTTagCompound tag){
		System.out.println("Is Tent method Run");
		Vector3 pos1 = Vector3.getVector(tag.getIntArray("pos1"));
		Vector3 pos2 = Vector3.getVector(tag.getIntArray("pos2"));
		
		int posXLarger = 0, posXSmaller = 0;
		int posZLarger = 0, posZSmaller = 0;
		
		if(pos1.getX() > pos2.getX()){ 
			posXLarger = pos1.getX();
			posXSmaller = pos2.getX();
		}else{
			posXSmaller = pos2.getX();
			posXLarger = pos1.getX();
		}
		
		if(pos1.getX() < pos2.getX()){
			posXLarger = pos2.getX();
			posXSmaller = pos1.getX();
		}else{
			posXSmaller = pos1.getX();
			posXLarger = pos2.getX();
		}
		
		if(pos1.getZ() > pos2.getZ()){ 
			posZLarger = pos1.getZ();
			posZSmaller = pos2.getZ();
		}else{
			posZSmaller = pos2.getZ();
			posZLarger = pos1.getZ();
		}
		
		if(pos1.getZ() < pos2.getZ()){
			posZLarger = pos2.getZ();
			posZSmaller = pos1.getZ();
		}else{
			posZSmaller = pos1.getZ();
			posZLarger = pos2.getZ();
		}
		
		for(int x = posXSmaller; x <= posXLarger; x++){
			System.out.println("X");
			for(int y = pos1.getY(); y < pos2.getY(); y++){
				System.out.println("Y");
				for(int z = posZSmaller; z <= posZLarger; z++){
					System.out.println("Z");
				}
			}
		}
		return false;
	}
	
	@Override
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(ModInfo.MODID + ":blueprint");
	}
	
	public int[] getArrayCoords(int x, int y, int z){
		int[] coords = new int[3];
		coords[0] = x;
		coords[1] = y;
		coords[2] = z;
		return coords;
	}
}
