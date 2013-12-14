package drunkmafia.mobilebase.tents;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class Tent {
	
	private int[][][][] structure;
	private int[] blockID;
	protected int center;
	
	private ItemStack stack;
	
	public void setStructure(int[][][][] structure){
		this.structure = structure;
	}
	
	public void setBlock(int[] block){
		blockID = block;
	}
	
	public boolean buildTent(World world, int x, int y, int z, ItemStack stack, ForgeDirection direction){
		int tempX = x - 4;
		int tempZ = z - 4;
		this.stack = stack;
		if(isAreaClear(world, x, y, z, direction)){
			for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
				for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
					for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
						int temp = structure[direction.ordinal() - 2][a1][a2][a3];
						if(temp != 0){
							switch(temp){
								case 1:
									world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, Block.cloth.blockID);
									break;
								case 2:
									world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, Block.doorWood.blockID);
									break;
								case 3:
									world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, Block.fence.blockID);
									break;
								case 4:
									world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, Block.torchWood.blockID, 5, 2);
									break;
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	public boolean isAreaClear(World world, int x, int y, int z, ForgeDirection direction){
		int tempX = x - 4;
		int tempZ = z - 4;
		int index = 0;
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					index++;
					if(a1 == 0){
						if(world.isBlockNormalCube(a3 + tempX, a1 + y, a2 + tempZ)){
							NBTTagCompound tag;
							if(stack.getTagCompound() == null)
								tag = new NBTTagCompound();
							else
								tag = stack.getTagCompound();
							
							tag.setInteger("floorID:" + index, world.getBlockId(a3 + tempX, a1 + y, a2 + tempZ));
							tag.setInteger("floorMETA:" + index, world.getBlockId(a3 + tempX, a1 + y, a2 + tempZ));
						}	
					}else{
						if(world.isAirBlock(a3 + tempX, a1 + y, a2 + tempZ))
							index++;
					}
				}
			}
		}
		System.out.println(index);
		return true;
	}
}
