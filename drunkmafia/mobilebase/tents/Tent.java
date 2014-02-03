package drunkmafia.mobilebase.tents;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.DimensionManager;

public class Tent {
	
	protected int[][][][] structure;
	private int strucutureCount, areaSize, insideSize, centerX, centerZ, amountWool, amountFences;	
	private int tentX, tentY, tentZ;
	
	public Tent(int[][][] array) {
		structure = new int[4][array.length][array[0].length][array[0][0].length];
		structure[0] = array;
		for(int d = 1; d < structure.length; d++){
			int[][][] temp = structure[d - 1];
			for(int y = 0; y < structure[d].length; y++){
				structure[d][y] = TentHelper.rotateMatrixLeft(temp[y]);
			}
		}
		
		tentY = structure[0].length;
		tentX = structure[0][0].length;
		tentZ = structure[0][0][0].length;
		
		setStrucutureCountCount();
		setCenter();
	}
	
	private void setCenter() {
		for(int y = 0; y < structure[0].length; y++){
			for(int x = 0; x < structure[0][y].length; x++){
				for(int z = 0; z < structure[0][y][x].length; z++){
					if(structure[0][y][x][z] == -1){
						centerX = (x + 1);
						centerZ = (z + 1);
						break;
					}
				}
			}
		}
	}

	public void printStrucuture(){
		for(int d = 0; d < structure.length; d++){
			for(int y = 0; y < structure[d].length; y++){
				for(int x = 0; x < structure[d][y].length; x++){
					System.out.println("{");
					for(int z = 0; z < structure[d][y][x].length; z++){
						System.out.print(structure[d][y][x][z] + ", ");
					}
					System.out.println("}");
				}
				System.out.println("");
			}
		}
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
					
					if(temp == 1)
						amountWool++;
					
					if(temp == -1 || temp == 2)
						amountFences++;
				}
			}
		}
		
		insideSize = inside;
		strucutureCount = i;
		areaSize = size;
	}
	
	public int getTentX() {
		return tentX;
	}
	
	public int getTentY() {
		return tentY;
	}
	
	public int getTentZ() {
		return tentZ;
	}
	
	public int getAmountWool() {
		return amountWool;
	}
	
	public int getAmountFences() {
		return amountFences;
	}
	
	public int getInside(){
		return insideSize;
	}
	
	public int getAreaSize(){
		return areaSize;
	}
	
	public int getCenterX(int direction) {
		if(direction == 1 || direction == 3)
			return centerX;
		else 
			return centerZ;
	}
	
	public int getCenterZ(int direction) {
		if(direction == 1 || direction == 3)
			return centerZ;
		else 
			return centerX;
	}
	
	public int getStrucutureCount(){
		return strucutureCount;
	}

	public int[][][][] getStructure(){
		return structure;
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("tentY", getTentY());
		tag.setInteger("tentX", getTentX());
		tag.setInteger("tentZ", getTentZ());
		for(int y = 0; y < structure[0].length; y++)		
			for(int x = 0; x < structure[0][y].length; x++)
				tag.setIntArray("tentStructure:" + y + x, structure[0][y][x]);
	}
	
	public static Tent loadFromNBT(NBTTagCompound tag){
		if(tag.hasKey("tentY")){
			int[][][] temp = new int[tag.getInteger("tentY")][tag.getInteger("tentX")][tag.getInteger("tentZ")];
			for(int y = 0; y < temp.length; y++)
				for(int x = 0; x < temp[y].length; x++)
					temp[y][x] = tag.getIntArray("tentStructure:" + y + x);
			
			return new Tent(temp);
		}else
			return null;
	}
}
