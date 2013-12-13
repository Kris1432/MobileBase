package drunkmafia.mobilebase.tents;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class Tent {
	
	protected int[][][][] structure;
	protected int center;
	
	public void setStructure(int[][][][] structure){
		this.structure = structure;
	}
	
	public boolean buildTent(World world, int x, int y, int z, ItemStack stack, ForgeDirection direction){
		if(world.isRemote) return false;
		
		for(int xCoord = x + getXCoord(direction); xCoord < Math.abs(getXCoord(direction)); xCoord++){
			for(int yCoord =  y; y > structure[direction.ordinal()].length; yCoord++){
				for(int zCoord = 0; z < structure[direction.ordinal()].length; z++){
					if(structure[direction.ordinal()][yCoord][zCoord][xCoord] != 0){
						world.setBlock(xCoord, yCoord, zCoord, Block.cloth.blockID);
					}
				}
			}
		}
		
		return true;
	}
	
	public int getXCoord(ForgeDirection direction){
		int x = 0;
		for(int i = 0; i < structure[direction.ordinal()].length; i++){
			if(structure[direction.ordinal()][0][center][i] != 3){
				x++;
			}else{
				break;
			}
		}
		return -x;
	}
}
