package drunkmafia.mobilebase.tents;

import ibxm.Player;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLLog;
import drunkmafia.mobilebase.block.ModBlocks;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.tileentity.TentPostTile;

public class TentHelper {
	
	public static boolean buildTent(World world, int x, int y, int z, ItemStack stack, ForgeDirection direction, Tent tent){
		int tempX = x - 4;
		int tempZ = z - 4;
		NBTTagCompound tag = stack.getTagCompound();

		if(isAreaClear(world, x, y, z, tag, tent, direction)){
			int[][] blocks = getFloorBlocks(world, x, y, z, tent, direction);
			for(int a1 = 0; a1 < tent.getStructure()[direction.ordinal() - 2].length; a1++){
				for(int a2 = 0; a2 < tent.getStructure()[direction.ordinal() - 2][0].length; a2++){
					for(int a3 = 0; a3 < tent.getStructure()[direction.ordinal() - 2][0][0].length; a3++){
						int temp = tent.getStructure()[direction.ordinal() - 2][a1][a2][a3];
						if(temp != 0){
							switch(temp){
								case -1:
									world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, ModBlocks.tentPost.blockID);
									world.setBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ, new TentPostTile());
									TentPostTile tile = (TentPostTile)world.getBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ);
									tile.tentType = tent;
									tile.woolType = stack.getItemDamage();
									tile.direction = direction;
									tile.blocks = blocks;
									tile.tentID = stack.getItem().itemID;
									tile.itemName = stack.getDisplayName();
									break;
								case 1:
									world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, ModBlocks.wool.blockID, stack.getItemDamage(), 3);
									break;
								case 2:
									world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, ModBlocks.tentPost.blockID);
									break;
							}
						}
					}
				}
			}
			reBuildInside(world, x, y, z, tag, tent, direction);
		}else
			return false;
		return true;
	}
	
	private static int[][] getFloorBlocks(World world, int x, int y, int z, Tent tent, ForgeDirection direction) {
		int tempX = x - 4;
		int tempZ = z - 4;
		int[][][][] structure = tent.getStructure();
		int[][] blocks = new int[structure[direction.ordinal() - 2][0].length][structure[direction.ordinal() - 2][0][0].length];
		for(int a1 = 0; a1 < blocks.length; a1++){
			for(int a2 = 0; a2 < blocks[a1].length; a2++){
				blocks[a1][a2] = world.getBlockId(a1 + tempX, y, a2 + tempZ);
			}
		}
		return blocks;
	}

	public static boolean isAreaClear(World world, int x, int y, int z, NBTTagCompound tag, Tent tent, ForgeDirection direction){
		int tempX = x - 4;
		int tempZ = z - 4;
		int index = 0;
		for(int a1 = 0; a1 < tent.getStructure()[direction.ordinal() - 2].length; a1++){
			for(int a2 = 0; a2 < tent.getStructure()[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < tent.getStructure()[direction.ordinal() - 2][0][0].length; a3++){
					if(world.isAirBlock(a3 + tempX, a1 + y, a2 + tempZ))
						index++;
				}
			}
		}
		return index == tent.getAreaSize();
	}
	
	public static boolean reBuildInside(World world, int x, int y, int z, NBTTagCompound tag, Tent tent, ForgeDirection direction){
		if(world.isRemote) return false;
		int tempX = x - tent.center;
		int tempZ = z - tent.center;
		int index = 0;
		if(tag != null){
			int[][][][] structure = tent.getStructure();
			for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
				for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
					for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
						int temp = structure[direction.ordinal() - 2][a1][a2][a3];	
						if(temp == 5){
							index++;
							if(tag.getBoolean("blockExists:" + index)){
								world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, tag.getInteger("blockID:" + index));
								if(tag.getBoolean("blockHasTile:" + index)){
									TileEntity tile = TileEntity.createAndLoadEntity(tag.getCompoundTag("blockTILE:" + index));
									if(tile != null){
										tile.xCoord = a3 + tempX;
										tile.yCoord = a1 + y;
										tile.zCoord = a2 + tempZ;
										world.setBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ, tile);
									}
								}
								world.setBlockMetadataWithNotify(a3 + tempX, a1 + y, a2 + tempZ, tag.getInteger("blockMETA:" + index), 1);
							}
						}
					}
				}
			}
		}
		cleanUpArea(world, x, y, z, tent, direction, tag);
		hasBeenBuilt(world, x, y, z, tag, tent, direction);
		return true;
	}
	
	public static void hasBeenBuilt(World world, int x, int y, int z, NBTTagCompound tag, Tent tent, ForgeDirection direction){
		int tempX = x - (tent.center - 1);
		int tempZ = z - tent.center;
		int index = 0;
		if(tag != null){
			int[][][][] structure = tent.getStructure();
			for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
				for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
					for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
						int temp = structure[direction.ordinal() - 2][a1][a2][a3];	
						if(temp == 5){
							index++;
							if(tag.getBoolean("blockExists:" + index)){
								if(world.isAirBlock(a3 + tempX, a1 + y, a2 + tempZ)){
									world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, tag.getInteger("blockID:" + index));
									if(tag.getBoolean("blockHasTile:" + index)){
										TileEntity tile = TileEntity.createAndLoadEntity(tag.getCompoundTag("blockTILE:" + index));
										if(tile != null){
											tile.xCoord = a3 + tempX;
											tile.yCoord = a1 + y;
											tile.zCoord = a2 + tempZ;
											world.setBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ, tile);
										}
									}
									world.setBlockMetadataWithNotify(a3 + tempX, a1 + y, a2 + tempZ, tag.getInteger("blockMETA:" + index), 1);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static boolean isTentStable(World world, int x, int y, int z, int woolType, Tent tent, ForgeDirection direction){
		int tempX = -tent.center + x;
		int tempZ = -tent.center + z;
		int count = 0;
		int[][][][] structure = tent.getStructure();
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					int temp = structure[direction.ordinal() - 2][a1][a2][a3];
					if(!world.isAirBlock(a3 + tempX, a1 + y - 1, a2 + tempZ)){
						if(temp == 1 || temp == -1 || temp == 2)
							count++;
					}
				}
			}
		}
		return count == tent.getStrucutureCount();
	}
	
	/**
	 * This breaks all blocks inside the tent, from the top down
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	
	public static void breakTent(World world, int x, int y, int z, Tent tent, ForgeDirection direction) {
		destoryTentInside(world, x, y, z, tent, direction);
		destoryTentOutside(world, x, y, z, tent, direction);
		rebuildFloor(world, x, y, z, tent, direction);
	}
	
	public static void cleanUpArea(World world, int x, int y, int z, Tent tent, ForgeDirection direction, NBTTagCompound tag) {
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double)x - tent.center, (double)y, (double)z- tent.center, (double)(x + tent.center), (double)(y + tent.structure[0].length), (double)(z + tent.center)).expand(tent.center, tent.structure[0].length, tent.center);
        List list = world.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
        Iterator iterator = list.iterator();
        EntityItem item;
        while (iterator.hasNext()){
        	if(tag != null){
	        	item = (EntityItem) iterator.next();
	        	int index = 0;
	        	int[][][][] structure = tent.getStructure();
	    		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
	    			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
	    				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
	    					int temp = structure[direction.ordinal() - 2][a1][a2][a3];
	    					if(temp == 5){
	    						index++;
				        		if(tag.getBoolean("blockExists:" + index)){
				        			if(item.getEntityItem().getDisplayName().equals(Block.blocksList[tag.getInteger("blockID:" + index)].getLocalizedName()) || item.getEntityItem().getUnlocalizedName().equals(Block.blocksList[tag.getInteger("blockID:" + index)].getUnlocalizedName()) || item.getEntityItem().getItem().itemID == Block.blocksList[tag.getInteger("blockID:" + index)].blockID){
				        				item.setDead();
				        				break;
				        			}
				        		}
	    					}
	    				}
	    			}
	        	}
        	}else{
        		FMLLog.log(Level.SEVERE, "[" + ModInfo.MODID + "] Error: A tent item does not have a NBT. Please delete this item at X: " + x + " Y: " + y + " Z: " + z);
        	}
        }
	}

	public static void destoryTentInside(World world, int x, int y, int z, Tent tent, ForgeDirection direction){
		removeAllTiles(world, x, y, z, tent, direction);
		int tempX = x - tent.center;
		int tempZ = z - tent.center;
		int[][][][] structure = tent.getStructure();
		int tempY = tent.getStructure()[direction.ordinal() - 2].length;
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			tempY--;
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					int temp = structure[direction.ordinal() - 2][tempY][a2][a3];
					if(temp != 1 && temp != -1){
						world.setBlockToAir(a3 + tempX, tempY + y - 1, a2 + tempZ);
					}
				}
			}
		}
	}
	
	public static void removeAllTiles(World world, int x, int y, int z, Tent tent, ForgeDirection direction){
		int tempX = x - tent.center;
		int tempZ = z - tent.center;
		int[][][][] structure = tent.getStructure();
		int tempY = tent.getStructure()[direction.ordinal() - 2].length;
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			tempY--;
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					int temp = structure[direction.ordinal() - 2][tempY][a2][a3];
					if(temp != 1 && temp != -1){
						if(world.blockHasTileEntity(a3 + tempX, tempY + y - 1, a2 + tempZ)){
							world.getBlockTileEntity(a3 + tempX, tempY + y - 1, a2 + tempZ).invalidate();
						}
					}
				}
			}
		}
	}
	
	public static void destoryTentOutside(World world, int x, int y, int z, Tent tent, ForgeDirection direction){
		int tempX = x - tent.center;
		int tempZ = z - tent.center;
		int[][][][] structure = tent.getStructure();
		int tempY = tent.getStructure()[direction.ordinal() - 2].length;
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					int temp = structure[direction.ordinal() - 2][a1][a2][a3];
					if(temp != 5 && temp != -1)
						world.setBlockToAir(a3 + tempX, a1 + y - 1, a2 + tempZ);
				}
			}
		}
	}
		
	private static void rebuildFloor(World world, int x, int y, int z, Tent tent, ForgeDirection direction) {
		int tempX = x - tent.center;
		int tempZ = z - tent.center;
		TentPostTile tile = (TentPostTile)world.getBlockTileEntity(x, y, z);
		for(int a1 = 0; a1 < tile.blocks.length; a1++){
			for(int a2 = 0; a2 < tile.blocks[a1].length; a2++){
				world.setBlock(tempX + a1, y - 1, tempZ + a2, tile.blocks[a1][a2]);
			}
		}
	}

	public static ItemStack getItemVersionOfTent(World world, int x, int y, int z, int woolType, Tent tent, ForgeDirection direction){
		TentPostTile tile = (TentPostTile) world.getBlockTileEntity(x, y, z);
		ItemStack stack = new ItemStack(tile.tentID, 1, woolType);
		NBTTagCompound tag = new NBTTagCompound();
		int tempX = x - tent.center;
		int tempZ = z - tent.center;
		int index = 0;
		int[][][][] structure = tent.getStructure();
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
								if(tag.getCompoundTag("blockTILE:" + index) == null)
									tag.setBoolean("blockHasTile:" + index, false);
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
		stack.setItemName(tile.itemName);
		return stack;
	}
	
	private static final int[] yToFlookup = { 3, 4, 2, 5 };
	
	public static ForgeDirection yawToForge(float yaw) {
        ForgeDirection result = ForgeDirection.getOrientation(yToFlookup[MathHelper.floor_double(yaw * 4.0F / 360.0F + 0.5D) & 3]);
        return result;
    }

	public static void movePlayer(EntityPlayer player, World world, int x, int y, int z, Tent tent, ForgeDirection direction) {
		int tempX = x - tent.center;
		int tempZ = z - tent.center;
		int index = 0;
		int[][][][] structure = tent.getStructure();
		for(int a1 = 0; a1 < structure[direction.ordinal() - 2].length; a1++){
			for(int a2 = 0; a2 < structure[direction.ordinal() - 2][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction.ordinal() - 2][0][0].length; a3++){
					int temp = structure[direction.ordinal() - 2][a1][a2][a3];
					if(temp == 3)
						player.setPosition(a3 + tempX, a1 + y - 1, a2 + tempZ);
				}
			}
		}
	}
}
