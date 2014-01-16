package drunkmafia.mobilebase.tents;

import ibxm.Player;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameData;
import drunkmafia.mobilebase.block.ModBlocks;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.tileentity.TentPostTile;

public class TentHelper {
	
	public static boolean buildTent(World world, int x, int y, int z, ItemStack stack, int direction, Tent tent){
		int tempX = x - 4;
		int tempZ = z - 4;
		NBTTagCompound tag = stack.getTagCompound();

		if(isAreaClear(world, x, y, z, tag, tent, direction)){
			int[][] blocks = getFloorBlocks(world, x, y, z, tent, direction);
			for(int a1 = 0; a1 < tent.getStructure()[direction].length; a1++){
				for(int a2 = 0; a2 < tent.getStructure()[direction][0].length; a2++){
					for(int a3 = 0; a3 < tent.getStructure()[direction][0][0].length; a3++){
						int temp = tent.getStructure()[direction][a1][a2][a3];
						if(temp != 0){
							switch(temp){
								case -1:
									world.func_147465_d(a3 + tempX, a1 + y, a2 + tempZ, ModBlocks.tentPost, 0, 3); //setBlock
									world.func_147455_a(a3 + tempX, a1 + y, a2 + tempZ, new TentPostTile()); //setBlockTileEntity
									TentPostTile tile = (TentPostTile)world.func_147438_o(a3 + tempX, a1 + y, a2 + tempZ); //getBlockTileEntity
									tile.tentType = tent;
									tile.woolType = stack.getItemDamage();
									tile.direction = direction;
									tile.directionName = tag.getString("directionName");
									tile.blocks = blocks;
									tile.tentID = GameData.itemRegistry.getId(stack.getItem());
									tile.itemName = stack.getDisplayName();
									break;
								case 1:
									world.func_147465_d(a3 + tempX, a1 + y, a2 + tempZ, ModBlocks.wool, stack.getItemDamage(), 3); //setBlock
									break;
								case 2:
									world.func_147465_d(a3 + tempX, a1 + y, a2 + tempZ, ModBlocks.tentPost, 0, 3); //setBlock
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
	
	private static int[][] getFloorBlocks(World world, int x, int y, int z, Tent tent, int direction) {
		int tempX = x - 4;
		int tempZ = z - 4;
		int[][][][] structure = tent.getStructure();
		int[][] blocks = new int[structure[direction][0].length][structure[direction][0][0].length];
		for(int a1 = 0; a1 < blocks.length; a1++){
			for(int a2 = 0; a2 < blocks[a1].length; a2++){
				blocks[a1][a2] = GameData.blockRegistry.getId(world.func_147439_a(a1 + tempX, y, a2 + tempZ)); //getBlockAt (replaces getBlockId)
			}
		}
		return blocks;
	}

	public static boolean isAreaClear(World world, int x, int y, int z, NBTTagCompound tag, Tent tent, int direction){
		int tempX = x - 4;
		int tempZ = z - 4;
		int index = 0;
		boolean isBedrock = false;
		for(int a1 = 0; a1 < tent.getStructure()[direction].length; a1++){
			for(int a2 = 0; a2 < tent.getStructure()[direction][0].length; a2++){
				for(int a3 = 0; a3 < tent.getStructure()[direction][0][0].length; a3++){
					if(world.func_147437_c(a3 + tempX, a1 + y, a2 + tempZ)) //isAirBlock
						index++;
					if(a1 == 0 && world.func_147439_a(a3 + tempX, a1 + y, a2 + tempZ) == Blocks.bedrock){ //getBlockAt
						isBedrock = true;
						break;
					}
				}
				if(isBedrock)
					break;
			}
			if(isBedrock)
				break;
		}
		if(isBedrock)
			return false;
		return index == tent.getAreaSize();
	}
	
	public static boolean reBuildInside(World world, int x, int y, int z, NBTTagCompound tag, Tent tent, int direction){
		if(world.isRemote) return false;
		int tempX = x - (tent.getCenter() - 1);
		int tempZ = z - (tent.getCenter() - 1);
		int index = 0;
		if(tag != null){
			int[][][][] structure = tent.getStructure();
			for(int a1 = 0; a1 < structure[direction].length; a1++){
				for(int a2 = 0; a2 < structure[direction][0].length; a2++){
					for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
						int temp = structure[direction][a1][a2][a3];	
						if(temp == 5){
							index++;
							if(tag.getBoolean("blockExists:" + index)){
								world.func_147465_d(a3 + tempX, a1 + y, a2 + tempZ, GameData.blockRegistry.get(tag.getInteger("blockID:" + index)), 0, 3); //setBlock
								if(tag.getBoolean("blockHasTile:" + index)){
									TileEntity tile = TileEntity.func_145827_c(tag.getCompoundTag("blockTILE:" + index)); //creatAndLoadEntity
									if(tile != null){
										tile.field_145851_c = a3 + tempX; //xCoord
										tile.field_145848_d = a1 + y; //yCoord
										tile.field_145849_e = a2 + tempZ; //zCoord
										world.func_147455_a(a3 + tempX, a1 + y, a2 + tempZ, tile);
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
	
	public static void hasBeenBuilt(World world, int x, int y, int z, NBTTagCompound tag, Tent tent, int direction){
		int tempX = x - (tent.getCenter() - 1);
		int tempZ = z - (tent.getCenter() - 1);
		int index = 0;
		if(tag != null){
			int[][][][] structure = tent.getStructure();
			for(int a1 = 0; a1 < structure[direction].length; a1++){
				for(int a2 = 0; a2 < structure[direction][0].length; a2++){
					for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
						int temp = structure[direction][a1][a2][a3];	
						if(temp == 5){
							index++;
							if(tag.getBoolean("blockExists:" + index)){
								if(world.func_147437_c(a3 + tempX, a1 + y, a2 + tempZ)){
									world.func_147465_d(a3 + tempX, a1 + y, a2 + tempZ, GameData.blockRegistry.get(tag.getInteger("blockID:" + index)), 0, 3); //setBlock
									if(tag.getBoolean("blockHasTile:" + index)){
										TileEntity tile = TileEntity.func_145827_c(tag.getCompoundTag("blockTILE:" + index));
										if(tile != null){
											tile.field_145851_c = a3 + tempX;
											tile.field_145848_d = a1 + y;
											tile.field_145849_e = a2 + tempZ;
											world.func_147455_a(a3 + tempX, a1 + y, a2 + tempZ, tile);
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
	
	public static boolean isTentStable(World world, int x, int y, int z, int woolType, Tent tent, int direction){
		int tempX = -(tent.getCenter() - 1) + x;
		int tempZ = -(tent.getCenter() - 1) + z;
		int count = 0;
		int[][][][] structure = tent.getStructure();
		for(int a1 = 0; a1 < structure[direction].length; a1++){
			for(int a2 = 0; a2 < structure[direction][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
					int temp = structure[direction][a1][a2][a3];
					if(!world.func_147437_c(a3 + tempX, a1 + y - 1, a2 + tempZ)){
						if(temp == 1 || temp == -1 || temp == 2)
							count++;
					}
				}
			}
		}
		return count == tent.getStrucutureCount();
	}
	
	public static void breakTent(World world, int x, int y, int z, Tent tent, int direction, NBTTagCompound tag) {
		destoryTentInside(world, x, y, z, tent, direction, tag);
		destoryTentOutside(world, x, y, z, tent, direction);
		rebuildFloor(world, x, y, z, tent, direction);
	}
	
	public static void cleanUpArea(World world, int x, int y, int z, Tent tent, int direction, NBTTagCompound tag) {
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double)x - (tent.getCenter() - 1), (double)y, (double)z- (tent.getCenter() - 1), (double)(x + (tent.getCenter() - 1)), (double)(y + tent.structure[0].length), (double)(z + (tent.getCenter() - 1))).expand((tent.getCenter() - 1), tent.structure[0].length, (tent.getCenter() - 1));
        List list = world.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
        Iterator iterator = list.iterator();
        EntityItem item;
        while (iterator.hasNext()){
        	if(tag != null){
	        	item = (EntityItem) iterator.next();
	        	int index = 0;
	        	int[][][][] structure = tent.getStructure();
	    		for(int a1 = 0; a1 < structure[direction].length; a1++){
	    			for(int a2 = 0; a2 < structure[direction][0].length; a2++){
	    				for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
	    					int temp = structure[direction][a1][a2][a3];
	    					if(temp == 5){
	    						index++;
				        		if(tag.getBoolean("blockExists:" + index)){
				        			if(item.getEntityItem().getDisplayName().equals(GameData.blockRegistry.get(tag.getInteger("blockID:" + index)).func_149732_F()) || item.getEntityItem().getUnlocalizedName().equals(GameData.blockRegistry.get(tag.getInteger("blockID:" + index)).func_149739_a()) || GameData.itemRegistry.getId(item.getEntityItem().getItem()) == tag.getInteger("blockID:" + index)){
				        				item.setDead();
				        				break;
				        			}
				        		}
	    					}
	    				}
	    			}
	        	}
        	}else{
        		FMLLog.severe("[" + ModInfo.MODID + "] Error: A tent item does not have a NBT. Please delete this item at X: " + x + " Y: " + y + " Z: " + z);
        	}
        }
	}

	public static void destoryTentInside(World world, int x, int y, int z, Tent tent, int direction, NBTTagCompound tag){
		removeAllTiles(world, x, y, z, tent, direction);
		int tempX = x - (tent.getCenter() - 1);
		int tempZ = z - (tent.getCenter() - 1);
		int[][][][] structure = tent.getStructure();
		int tempY = tent.getStructure()[direction].length;
		for(int a1 = 0; a1 < structure[direction].length; a1++){
			tempY--;
			for(int a2 = 0; a2 < structure[direction][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
					int temp = structure[direction][tempY][a2][a3];
					if(temp != 1 && temp != -1 && temp != 0){
						world.func_147468_f(a3 + tempX, tempY + y - 1, a2 + tempZ);
					}
				}
			}
		}
		cleanUpArea(world, x, y, z, tent, direction, tag);
		//This makes the current Thread that the code is being ran from sleep
		try {
			Thread.sleep(5L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void removeAllTiles(World world, int x, int y, int z, Tent tent, int direction){
		int tempX = x - (tent.getCenter() - 1);
		int tempZ = z - (tent.getCenter() - 1);
		int[][][][] structure = tent.getStructure();
		int tempY = tent.getStructure()[direction].length;
		for(int a1 = 0; a1 < structure[direction].length; a1++){
			tempY--;
			for(int a2 = 0; a2 < structure[direction][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
					int temp = structure[direction][tempY][a2][a3];
					if(temp != 1 && temp != -1){
						if(world.func_147439_a(a3 + tempX, tempY + y - 1, a2 + tempZ).hasTileEntity(world.getBlockMetadata(a3 + tempX, tempY + y - 1, a2 + tempZ))){ //work around for world.blockHasTileEntity()
							world.func_147438_o(a3 + tempX, tempY + y - 1, a2 + tempZ).func_145843_s(); //getBlockTileEntity, invalidate
						}
					}
				}
			}
		}
	}
	
	public static void destoryTentOutside(World world, int x, int y, int z, Tent tent, int direction){
		int tempX = x - (tent.getCenter() - 1);
		int tempZ = z - (tent.getCenter() - 1);
		int[][][][] structure = tent.getStructure();
		int tempY = tent.getStructure()[direction].length;
		for(int a1 = 0; a1 < structure[direction].length; a1++){
			for(int a2 = 0; a2 < structure[direction][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
					int temp = structure[direction][a1][a2][a3];
					if(temp != 0 && (temp == 1 || temp == 2))
						world.func_147468_f(a3 + tempX, a1 + y - 1, a2 + tempZ);
				}
			}
		}
	}
		
	private static void rebuildFloor(World world, int x, int y, int z, Tent tent, int direction) {
		int tempX = x - (tent.getCenter() - 1);
		int tempZ = z - (tent.getCenter() - 1);
		TentPostTile tile = (TentPostTile)world.func_147438_o(x, y, z); //getBlockTileEntity
		for(int a1 = 0; a1 < tile.blocks.length; a1++){
			for(int a2 = 0; a2 < tile.blocks[a1].length; a2++){
				world.func_147465_d(tempX + a1, y - 1, tempZ + a2, GameData.blockRegistry.get(tile.blocks[a1][a2]), 0, 3);
			}
		}
	}

	public static ItemStack getItemVersionOfTent(World world, int x, int y, int z, int woolType, Tent tent, int direction){
		TentPostTile tile = (TentPostTile) world.func_147438_o(x, y, z); //getBlockTileEntity
		ItemStack stack = new ItemStack(GameData.itemRegistry.get(tile.tentID), 1, woolType); //getBlockFromEntity
		NBTTagCompound tag = new NBTTagCompound();
		int tempX = x - (tent.getCenter() - 1);
		int tempZ = z - (tent.getCenter() - 1);
		int index = 0;
		int[][][][] structure = tent.getStructure();
		for(int a1 = 0; a1 < structure[direction].length; a1++){
			for(int a2 = 0; a2 < structure[direction][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
					int temp = structure[direction][a1][a2][a3];
					if(temp == 5){
						index++;
						if(!world.func_147437_c(a3 + tempX, a1 + y - 1, a2 + tempZ)){
							tag.setBoolean("blockExists:" + index, true);
							tag.setInteger("blockID:" + index, GameData.blockRegistry.getId(world.func_147439_a(a3 + tempX, a1 + y - 1, a2 + tempZ)));
							tag.setInteger("blockMETA:" + index, world.getBlockMetadata(a3 + tempX, a1 + y - 1, a2 + tempZ));
							if(world.func_147439_a(a3 + tempX, a1 + y - 1, a2 + tempZ).hasTileEntity(world.getBlockMetadata(a3 + tempX, a1 + y - 1, a2 + tempZ))){
								tag.setBoolean("blockHasTile:" + index, true);
								NBTTagCompound tileTag = new NBTTagCompound();
								world.func_147438_o(a3 + tempX, a1 + y - 1, a2 + tempZ).func_145841_b(tileTag);
								tag.setTag("blockTILE:" + index, tileTag);
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
		tag.setByte("direction", (byte) direction);
		tag.setString("directionName", tile.directionName);
		
		getEntities(world, x, y, z, tent, direction, tag);
		
		stack.setTagCompound(tag);
		stack.func_151001_c(tile.itemName);
		return stack;
	}
	
	private static void getEntities(World world, int x, int y, int z, Tent tent, int direction, NBTTagCompound tag) {
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double)x - (tent.getCenter() - 1), (double)y, (double)z- (tent.getCenter() - 1), (double)(x + (tent.getCenter() - 1)), (double)(y + tent.structure[0].length), (double)(z + (tent.getCenter() - 1))).expand((tent.getCenter() - 1), tent.structure[0].length, (tent.getCenter() - 1));
        List list = world.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
        Iterator iterator = list.iterator();
        EntityItem item;
		
	}

	private static final int[] yToFlookup = { 3, 4, 2, 5 };
	
	public static ForgeDirection yawToForge(float yaw) {
		ForgeDirection result = ForgeDirection.getOrientation(yToFlookup[MathHelper.floor_double(yaw * 4.0F / 360.0F + 0.5D) & 3]);
        return result;
    }
	
	public static int[][] rotateMatrixRight(int[][] matrix){
	    /* W and H are already swapped */
	    int w = matrix.length;
	    int h = matrix[0].length;
	    int[][] ret = new int[h][w];
	    for (int i = 0; i < h; ++i) {
	        for (int j = 0; j < w; ++j) {
	            ret[i][j] = matrix[w - j - 1][i];
	        }
	    }
	    return ret;
	}
	
	public static int[][] rotateMatrixLeft(int[][] matrix){
	    /* W and H are already swapped */
	    int w = matrix.length;
	    int h = matrix[0].length;   
	    int[][] ret = new int[h][w];
	    for (int i = 0; i < h; ++i) {
	        for (int j = 0; j < w; ++j) {
	            ret[i][j] = matrix[j][h - i - 1];
	        }
	    }
	    return ret;
	}
	
	public static void movePlayer(EntityPlayer player, World world, int x, int y, int z, Tent tent, int direction) {
		int tempX = x - (tent.getCenter() - 1);
		int tempZ = z - (tent.getCenter() - 1);
		int index = 0;
		int[][][][] structure = tent.getStructure();
		for(int a1 = 0; a1 < structure[direction].length; a1++){
			for(int a2 = 0; a2 < structure[direction][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
					int temp = structure[direction][a1][a2][a3];
					if(temp == 3)
						player.setPosition(a3 + tempX, a1 + y - 1, a2 + tempZ);
				}
			}
		}
	}
	
	public static int convertForgeDirToTentDir(ForgeDirection dir){
		switch(dir){
			case NORTH:
				return 0;
			case SOUTH:
				return 2;
			case WEST:
				return 1;
			case EAST:
				return 3;
			default:
				return -1;
		}
	}
	
	public static int convertTentDirToForgeDir(int dir){
		switch(dir){
			case 0:
				return ForgeDirection.NORTH.ordinal();
			case 2:
				return ForgeDirection.SOUTH.ordinal();
			case 1:
				return ForgeDirection.WEST.ordinal();
			case 3:
				return ForgeDirection.EAST.ordinal();
			default:
				return -1;
		}
	}
}
