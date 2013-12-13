package drunkmafia.mobilebase.tents;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class Tent {
	
	private int[][][][] structure;
	protected int center;
	
	public void setStructure(int[][][][] structure){
		this.structure = structure;
	}
	
	public boolean buildTent(World world, int x, int y, int z, ItemStack stack, ForgeDirection direction){
		int i = 0;
		int tempX = x;
        int tempY = y - 1;
        int tempZ = z;  
        
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			tempY++;
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				tempZ++;
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					tempX++;
					int temp = structure[direction.ordinal() - 2][a1][a2][a3];
					if(temp != 0){
						switch(temp){
							case 1:
								world.setBlock(a3 + x, a1 + y, a2 + z, Block.cloth.blockID);
						}
					}
				}
			}
		}
		return true;
	}
}
