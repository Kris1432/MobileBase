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
		for(int xCoord = getXCoord(direction); xCoord <= getXCoord(direction); xCoord++){
			for(int yCoord = 0; yCoord <= structure[direction.ordinal() - 2].length; yCoord++){
				for(int zCoord = 0; zCoord <= structure[direction.ordinal() - 2].length; z++){
					System.out.println("T");
					if(structure[direction.ordinal() - 2][yCoord][zCoord][xCoord] != 0){
						int tempX = xCoord + x;
						int tempY = yCoord + y;
						int tempZ = zCoord + z;
						world.setBlock(tempX, tempY, tempZ, Block.cloth.blockID);
					}
				}
			}
		}
		
		return true;
	}
	
	public int getXCoord(ForgeDirection direction){
		int x = 0;
		for(int i = 0; i < structure[direction.ordinal() - 2].length; i++){
			if(structure[direction.ordinal() - 2][0][center][i] != 3){
				x++;
			}else{
				break;
			}
		}
		return -x;
	}
}
