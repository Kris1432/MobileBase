package drunkmafia.mobilebase.tents;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import drunkmafia.mobilebase.block.ModBlocks;
import drunkmafia.mobilebase.block.TentPostTile;
import drunkmafia.mobilebase.item.ModItems;

public class Tent {
	
	protected static ArrayList<Tent> tents = new ArrayList<Tent>();
	protected int[][][][] structure;
	protected int center;
	private int strucutureCount, areaSize;	
	
	public void setStructure(int[][][][] temp){
		structure = temp;
		setStrucutureCountCount();
	}
	
	private void setStrucutureCountCount() {
		int i = 0;
		int size = 0;
		for(int x = 0; x < structure[0][0].length; x++){
			for(int y = 0; y < structure[0].length; y++){
				for(int z = 0; z < structure[0][0][0].length; z++){
					int temp = structure[0][y][x][z];
					if(temp == 1 || temp == -1 || temp == 2){
						i++;
					}
					if(y != 0)
						size++;
				}
			}
		}
		
		strucutureCount = i;
		areaSize = size;
		System.out.println("strucutureCount: " + strucutureCount +" areaSize: " + areaSize);
	}
	
	
	public int getAreaSize(){
		return areaSize;
	}
	
	public int getStrucutureCount(){
		return strucutureCount;
	}

	public int[][][][] getStructure(){
		return structure;
	}
	
	public int getTentID(){
		return tents.indexOf(this);
	}
	
	public static Tent getTentByID(int id){
		return tents.get(id);
	}
}
