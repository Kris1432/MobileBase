package drunkmafia.mobilebase.tents;

import java.text.NumberFormat;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.DimensionManager;

public class Tent {
	
	protected static ArrayList<Tent> tents = new ArrayList<Tent>();
	protected int[][][][] structure;
	protected int center;
	private int strucutureCount, areaSize, insideSize;	
	
	public void setStructure(int[][][][] temp){
		structure = temp;
		setStrucutureCountCount();
	}
	
	private void setStrucutureCountCount() {
		int i = 0;
		int size = 0;
		int inside = 0;
		for(int x = 0; x < structure[0][0].length; x++){
			for(int y = 0; y < structure[0].length; y++){
				for(int z = 0; z < structure[0][0][0].length; z++){
					int temp = structure[0][y][x][z];
					if(temp == 1 || temp == -1 || temp == 2)
						i++;
					
					if(temp == 2)
						inside++;
					
					if(y != 0)
						size++;
					
				}
			}
		}
		
		insideSize = inside;
		strucutureCount = i;
		areaSize = size;
		System.out.println("strucutureCount: " + strucutureCount +" areaSize: " + areaSize);
	}
	
	public int getInside(){
		return insideSize;
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
