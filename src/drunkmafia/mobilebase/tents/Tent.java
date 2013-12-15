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
		
	public void setStructure(int[][][][] temp){
		structure = temp;
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
