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
	
	private static ArrayList<Tent> tents = new ArrayList<Tent>();
	private int[][][][] structure;
	protected int center;
	
	private boolean isFirstPlace;
	public ForgeDirection direction;
	
	public void addTent(){
		tents.add(this);
	}
	
	public int getTentID(){
		return tents.indexOf(this);
	}
	
	public static Tent getTentByID(int id){
		return tents.get(id);
	}
	
	public void setStructure(int[][][][] structure){
		this.structure = structure;
	}
	
	public boolean buildTent(World world, int x, int y, int z, ItemStack stack, ForgeDirection placementDir){
		int tempX = x - 4;
		int tempZ = z - 4;
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
		}
		
		if(isAreaClear(world, x, y, z, tag)){
			int[][] blocks = getFloorBlocks(world, x, y, z);
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
									tile.woolType = stack.getItemDamage();
									tile.direction = direction;
									tile.blocks = blocks;
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
	
	private int[][] getFloorBlocks(World world, int x, int y, int z) {
		int tempX = x - 4;
		int tempZ = z - 4;
		int[][] blocks = new int[structure[direction.ordinal() - 2][0].length][structure[direction.ordinal() - 2][0][0].length];
		for(int a1 = 0; a1 < blocks.length; a1++){
			for(int a2 = 0; a2 < blocks[a1].length; a2++){
				blocks[a1][a2] = world.getBlockId(a1 + tempX, y - 1, a2 + tempZ);
			}
		}
		return blocks;
	}

	public boolean isAreaClear(World world, int x, int y, int z, NBTTagCompound tag){
		int tempX = x - 4;
		int tempZ = z - 4;
		int index = 0;
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					if(world.isAirBlock(a3 + tempX, a1 + y, a2 + tempZ))
						index++;
				}
			}
		}
		System.out.println(index);
		return index == 405;
	}
	
	public boolean reBuildInside(World world, int x, int y, int z, NBTTagCompound tag){
		if(world.isRemote) return false;
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
							world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, tag.getInteger("blockID:" + index));
							world.setBlockMetadataWithNotify(a3 + tempX, a1 + y, a2 + tempZ, tag.getInteger("blockMETA:" + index), 2);
							if(tag.getBoolean("blockHasTile:" + index)){
								world.setBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ, TileEntity.createAndLoadEntity(tag.getCompoundTag("blockTILE:" + index)));
								TileEntity tile = world.getBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ);
								tile.xCoord = a3 + tempX;
								tile.yCoord = a1 + y;
								tile.zCoord = a2 + tempZ;
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	public boolean isTentStable(World world, int x, int y, int z, int woolType){
		int tempX = -4 + x;
		int tempZ = -4 + z;
		int count = 0;
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					int temp = structure[direction.ordinal() - 2][a1][a2][a3];
					if(!world.isAirBlock(a3 + tempX, a1 + y - 1, a2 + tempZ)){
						if(temp == 1 || temp == -1 || temp == 3 || temp ==  4)
							count++;
					}
				}
			}
		}
		return count == 200;
	}
	
	/**
	 * This breaks all blocks inside the tent, from the top down
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	
	public void breakTent(World world, int x, int y, int z) {
		int tempX = x - 4;
		int tempZ = z - 4;
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					int temp = structure[direction.ordinal() - 2][a1][a2][a3];
					if(!world.isAirBlock(a3 + tempX, a1 + y - 1, a2 + tempZ))
						if(temp == 1 || temp == -1 || temp == 3 || temp ==  4)
							world.setBlockToAir(a3 + tempX, a1 + y - 1, a2 + tempZ);
				}
			}
		}
	}
	
	/**
	 * This breaks the tent from the top down and skips out the control post
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public void playerBreaksTent(World world, int x, int y, int z) {
		int tempX = x - 4;
		int tempZ = z - 4;
		int tempY = structure[direction.ordinal() - 2].length;
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			tempY--;
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					int temp = structure[direction.ordinal() - 2][a1][a2][a3];
					if(!world.isAirBlock(a3 + tempX, tempY + y - 1, a2 + tempZ))
						if(world.getBlockId(a3 + tempX, tempY + y - 1, a2 + tempZ) != ModBlocks.tentPost.blockID){
							if(world.blockHasTileEntity(a3 + tempX, tempY + y - 1, a2 + tempZ))
								world.removeBlockTileEntity(a3 + tempX, tempY + y - 1, a2 + tempZ);
							world.setBlock(a3 + tempX, tempY + y - 1, a2 + tempZ, 0);
						}
				}
			}
		}
		rebuildFloor(world, x, y, z);
	}
	
	private void rebuildFloor(World world, int x, int y, int z) {
		int tempX = x - 4;
		int tempZ = z - 4;
		TentPostTile tile = (TentPostTile)world.getBlockTileEntity(x, y, z);
		for(int a1 = 0; a1 < tile.blocks.length; a1++){
			for(int a2 = 0; a2 < tile.blocks[a1].length; a2++){
				world.setBlock(tempX + a2, y - 1, tempZ + a1, tile.blocks[a1][a2]);
			}
		}
	}

	public ItemStack getItemVersionOfTent(World world, int x, int y, int z, int woolType){
		TentPostTile tile = (TentPostTile) world.getBlockTileEntity(x, y, z);
		ItemStack stack = new ItemStack(ModItems.tent, 1, woolType);
		NBTTagCompound tag = new NBTTagCompound();
		int tempX = x - 4;
		int tempZ = z - 4;
		int index = 0;
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					int temp = structure[direction.ordinal() - 2][a1][a2][a3];
					if(temp == 5){
						index++;
						if(!world.isAirBlock(a3 + tempX, a1 + y - 1, a2 + tempZ)){
							tag.setBoolean("blockExists:" + index, true);
							tag.setInteger("blockID:" + index, world.getBlockId(a3 + tempX, a1 + y - 1, a2 + tempZ));
							tag.setInteger("blockMETA:" + index, world.getBlockMetadata(a3 + tempX, a1 + y - 1, a2 + tempZ));
							if(world.blockHasTileEntity(a3 + tempX, a1 + y - 1, a2 + tempZ)){
								tag.setBoolean("blockHasTile:" + index, true);
								NBTTagCompound tileTag = new NBTTagCompound();
								world.getBlockTileEntity(a3 + tempX, a1 + y - 1, a2 + tempZ).writeToNBT(tileTag);
								tag.setCompoundTag("blockTILE:" + index, tileTag);
								System.out.println("Tile detected");
							}else
								tag.setBoolean("blockHasTile:" + index, false);
						}else
							tag.setBoolean("blockExists" + index, false);
					}
				}
			}
		}
		tag.setInteger("direction", direction.ordinal());
		stack.setTagCompound(tag);
		return stack;
	}
}
