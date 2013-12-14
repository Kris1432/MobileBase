package drunkmafia.mobilebase.tents;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import drunkmafia.mobilebase.block.ModBlocks;
import drunkmafia.mobilebase.block.TentPostTile;

public class Tent {
	
	private int[][][][] structure;
	protected int center;
	
	private ItemStack stack;
	private boolean isFirstPlace;
	private ForgeDirection direction;
	
	public void setStructure(int[][][][] structure){
		this.structure = structure;
	}
	
	public boolean buildTent(World world, int x, int y, int z, ItemStack stack, ForgeDirection placementDir){
		int tempX = x - 4;
		int tempZ = z - 4;
		this.stack = stack;
		NBTTagCompound tag;
		if(stack.getTagCompound() == null){
			tag = new NBTTagCompound();
		}else{
			tag = stack.getTagCompound();
		}
		
		if(tag.hasKey("direction")){
			System.out.println("Has Direction");
			isFirstPlace = false;
			direction = ForgeDirection.values()[tag.getInteger("direction")];
		}else{
			tag.setInteger("direction", placementDir.ordinal());
			direction = placementDir;
		}
		
		if(isAreaClear(world, x, y, z, tag)){
			for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
				for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
					for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
						int temp = structure[direction.ordinal() - 2][a1][a2][a3];
						if(temp != 0){
							switch(temp){
								case -1:
									world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, ModBlocks.tentPost.blockID);
									TentPostTile tile = (TentPostTile)world.getBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ);
									tile.tentType = this;
									break;
								case 1:
									world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, Block.cloth.blockID, stack.getItemDamage(), 3);
									break;
								case 2:
									//world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, Block.doorWood.blockID);
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
			reBuildInside(world, x, y, z, tag);
		}
		return true;
	}
	
	public boolean isAreaClear(World world, int x, int y, int z, NBTTagCompound tag){
		int tempX = x - 4;
		int tempZ = z - 4;
		int index = 0;
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					index++;
					if(a1 == 0){
						if(world.isBlockNormalCube(a3 + tempX, a1 + y, a2 + tempZ)){
							
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
		return index == 891;
	}
	
	public boolean reBuildInside(World world, int x, int y, int z, NBTTagCompound tag){
		int tempX = x - 4;
		int tempZ = z - 4;
		int index = 0;
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					int temp = structure[direction.ordinal() - 2][a1][a2][a3];					
					if(temp == 5){
						index++;
						if(tag.getBoolean("blockExists:" + index)){
							world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, tag.getInteger("blockID:" + index), tag.getInteger("blockMETA:" + index), 3);
							if(tag.getBoolean("blockHasTile")){
								NBTTagCompound tileNBT = tag.getCompoundTag("blockTILE:" + index);
								TileEntity tile = world.getBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ);
								tile.readFromNBT(tileNBT);
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	public boolean isTentStable(World world, int x, int y, int z){
		int tempX = x - 4;
		int tempZ = z - 4;
		int count = 0;
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					int temp = structure[direction.ordinal() - 2][a1][a2][a3];
					switch(temp){
						case -1:
							if(world.getBlockId(a3 + tempX, a1 + y, a2 + tempZ) == ModBlocks.tentPost.blockID)
								count++;
							break;
						case 1:
							if(world.getBlockId(a3 + tempX, a1 + y, a2 + tempZ) == Block.cloth.blockID && world.getBlockMetadata(a3 + tempX, a1 + y, a2 + tempZ) == stack.getItemDamage())
								count++;
							break;
						case 2:
							
							break;
						case 3:
							if(world.getBlockId(a3 + tempX, a1 + y, a2 + tempZ) == Block.fence.blockID)
								count++;
							break;
						case 4:
							if(world.getBlockId(a3 + tempX, a1 + y, a2 + tempZ) == Block.torchWood.blockID && world.getBlockMetadata(a3 + tempX, a1 + y, a2 + tempZ) == 5)
								count++;
							break;
					}
				}
			}
		}
		System.out.println(count);
		return count == 200;
	}

	public void breakTent(World world, int x, int y, int z) {
		int tempX = x - 4;
		int tempZ = z - 4;
		int count = 0;
		for(int a1 = -1; a1 < structure[direction.ordinal() - 2].length; a1++){
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					int temp = structure[direction.ordinal() - 2][a1][a2][a3];
					world.destroyBlock(a3 + tempX, a1 + y, a2 + tempZ, false);
				}
			}
		}
	}
}
