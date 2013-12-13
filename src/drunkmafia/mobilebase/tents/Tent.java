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
		int i = 0;
		for(int xCoord = 0; xCoord < structure[direction.ordinal() - 2][0][0].length; xCoord++){
			for(int zCoord = 0; zCoord < structure[direction.ordinal() - 2][0].length; zCoord++){
				for(int yCoord = 0; yCoord < structure[direction.ordinal() - 2].length; yCoord++){
					int temp = structure[direction.ordinal() - 2][yCoord][zCoord][xCoord];
					if(yCoord == 0){
						world.setBlock(xCoord + x, yCoord + y, zCoord + z, Block.cloth.blockID);
					}else if(temp != 3 && temp != 2 && temp != 0){
						world.setBlock(xCoord + x, yCoord + y, zCoord + z, Block.cloth.blockID);
					}
				}
			}
		}
		return true;
	}
}
