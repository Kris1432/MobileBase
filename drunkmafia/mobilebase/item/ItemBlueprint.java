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
		
		FMLNetworkHandler.openGui(player, MobileBase.instance, 0, world, x, y, z);
		
		return true;
	}
	
	
	@Override
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(ModInfo.MODID + ":blueprint");
	}	
	
	
	
	
	
	
	/**
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
				player.sendChatToPlayer(ChatMessageComponent.createFromText("§4 Position 1 set"));
			}else if(!tag.hasKey("pos2")){
				tag.setIntArray("pos2", getArrayCoords(x, y, z));
				player.sendChatToPlayer(ChatMessageComponent.createFromText("§4 Position 2 set"));
			}else{
				player.sendChatToPlayer(ChatMessageComponent.createFromText("Blueprint already exists, shift-click to remove plan"));
			}
			stack.setTagCompound(tag);
			
			if(tag.hasKey("pos1") && tag.hasKey("pos2"))
				getTent(world, tag);
			
		}else{
			player.sendChatToPlayer(ChatMessageComponent.createFromText("Wiped Plan"));
			stack.setTagCompound(new NBTTagCompound());
		}
		
		return true;
	}

	public Tent getTent(World world, NBTTagCompound tag){
		System.out.println("Is Tent method Run");
		Vector3 pos1 = Vector3.getVector(tag.getIntArray("pos1"));
		Vector3 pos2 = Vector3.getVector(tag.getIntArray("pos2"));
		
		int posXLarger = 0, posXSmaller = 0;
		int posZLarger = 0, posZSmaller = 0;
		int posYLarger = 0, posYSmaller = 0;
		
		if(pos1.getX() > pos2.getX()){ 
			posXLarger = pos1.getX();
			posXSmaller = pos2.getX();
		}else{
			posXLarger = pos2.getX();
			posXSmaller = pos1.getX();
		}
		
		if(pos1.getY() > pos2.getY()){ 
			posYLarger = pos1.getY();
			posYSmaller = pos2.getY();
		}else{
			posYLarger = pos2.getY();
			posYSmaller = pos1.getY();
		}
		
		if(pos1.getZ() > pos2.getZ()){ 
			posZLarger = pos1.getZ();
			posZSmaller = pos2.getZ();
		}else{
			posZLarger = pos2.getZ();
			posZSmaller = pos1.getZ();
		}
		
		System.out.println("X: " + (posXLarger - posXSmaller - 1));
		System.out.println("Y: " + (posYLarger - posYSmaller + 1));
		System.out.println("Z: " + (posZLarger - posZSmaller - 1));
		
		int[][][] structure = new int[Math.abs((posYSmaller - posYLarger))][Math.abs(posXSmaller - posXLarger)][Math.abs(posZSmaller - posZLarger)];	
		for(int a1 = 0; a1 < structure.length; a1++){
			for(int a2 = 0; a2 < structure[0].length; a2++){
				for(int a3 = 0; a3 < structure[0][0].length; a3++){
					int id = world.getBlockId(a2 + posXSmaller, a1, a3 + posZSmaller);
					if(id == Block.cloth.blockID || id == Block.fence.blockID){
						structure[a1][a2][a3] = id == Block.cloth.blockID ? 1 : 2;
					}else{
						structure[a1][a2][a3] = 0;
					}
				}
			}
		}
		
		for(int a1 = 0; a1 < structure.length; a1++){
			for(int a2 = 0; a2 < structure[0].length; a2++){
				for(int a3 = 0; a3 < structure[0][0].length; a3++){
					System.out.print(structure[a1][a2][a3] + " ");
				}
				System.out.println("");
			}
			System.out.println("----------");
		}
		
		int test = 0;
		for(int a2 = 0; a2 < structure[0].length; a2++){
			for(int a3 = 0; a3 < structure[0][0].length; a3++){
				boolean hasRoof = false;
				for(int a1 = 0; a1 < structure.length; a1++){
					int temp = structure[a1][a2][a3];
					if(!hasRoof && temp == 1){
						hasRoof = true;
					}else if(hasRoof && temp == 1){
						break;
					}else if(hasRoof && temp == 0){
						structure[a1][a2][a3] = 5;
					}
					if(structure[a1][a2][a3] == 5)
						test++;
				}
			}
		}
		
		System.out.println(test + "height " + structure.length);
		Tent tent = new Tent();
		
		return null;
	}
	
	public int[] getArrayCoords(int x, int y, int z){
		int[] coords = new int[3];
		coords[0] = x;
		coords[1] = y;
		coords[2] = z;
		return coords;
	}**/
}
