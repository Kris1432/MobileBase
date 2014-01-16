package drunkmafia.mobilebase.tents;

import ibxm.Player;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
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
									world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, ModBlocks.tentPost.blockID);
									world.setBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ, new TentPostTile());
									TentPostTile tile = (TentPostTile)world.getBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ);
									tile.tentType = tent;
									tile.woolType = stack.getItemDamage();
									tile.direction = direction;
									tile.directionName = tag.getString("directionName");
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
	
	private static int[][] getFloorBlocks(World world, int x, int y, int z, Tent tent, int direction) {
		int tempX = x - 4;
		int tempZ = z - 4;
		int[][][][] structure = tent.getStructure();
		int[][] blocks = new int[structure[direction][0].length][structure[direction][0][0].length];
		for(int a1 = 0; a1 < blocks.length; a1++){
			for(int a2 = 0; a2 < blocks[a1].length; a2++){
				blocks[a1][a2] = world.getBlockId(a1 + tempX, y, a2 + tempZ);
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
					if(world.isAirBlock(a3 + tempX, a1 + y, a2 + tempZ))
						index++;
					if(a1 == 0 && world.getBlockId(a3 + tempX, a1 + y, a2 + tempZ) == Block.bedrock.blockID){
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
				
		for(int i = 0; i <= tag.getInteger("entities"); i++){
			if(tag.hasKey("entityID:" + i)){
				Entity ent = EntityList.createEntityByID(tag.getInteger("entityID:" + i), world);
				if(ent != null && tag.getCompoundTag("entityNBT:" + i) != null){
					ent.readFromNBT(tag.getCompoundTag("entityNBT:" + i));
					ent.setPosition(x + tag.getInteger("entityX:" + i), y + tag.getInteger("entityY:" + i), z + tag.getInteger("entityZ:" + i));
					world.spawnEntityInWorld(ent);
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
	
	public static boolean isTentStable(World world, int x, int y, int z, int woolType, Tent tent, int direction){
		int tempX = -(tent.getCenter() - 1) + x;
		int tempZ = -(tent.getCenter() - 1) + z;
		int count = 0;
		int[][][][] structure = tent.getStructure();
		for(int a1 = 0; a1 < structure[direction].length; a1++){
			for(int a2 = 0; a2 < structure[direction][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
					int temp = structure[direction][a1][a2][a3];
					if(!world.isAirBlock(a3 + tempX, a1 + y - 1, a2 + tempZ)){
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
						world.setBlockToAir(a3 + tempX, tempY + y - 1, a2 + tempZ);
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
						if(world.blockHasTileEntity(a3 + tempX, tempY + y - 1, a2 + tempZ)){
							world.getBlockTileEntity(a3 + tempX, tempY + y - 1, a2 + tempZ).invalidate();
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
						world.setBlockToAir(a3 + tempX, a1 + y - 1, a2 + tempZ);
				}
			}
		}
	}
		
	private static void rebuildFloor(World world, int x, int y, int z, Tent tent, int direction) {
		int tempX = x - (tent.getCenter() - 1);
		int tempZ = z - (tent.getCenter() - 1);
		TentPostTile tile = (TentPostTile)world.getBlockTileEntity(x, y, z);
		for(int a1 = 0; a1 < tile.blocks.length; a1++){
			for(int a2 = 0; a2 < tile.blocks[a1].length; a2++){
				world.setBlock(tempX + a1, y - 1, tempZ + a2, tile.blocks[a1][a2]);
			}
		}
	}

	public static ItemStack getItemVersionOfTent(World world, int x, int y, int z, int woolType, Tent tent, int direction){
		TentPostTile tile = (TentPostTile) world.getBlockTileEntity(x, y, z);
		ItemStack stack = new ItemStack(tile.tentID, 1, woolType);
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
		tag.setByte("direction", (byte) direction);
		tag.setString("directionName", tile.directionName);
		
		getEntities(world, x, y, z, tent, direction, tag);
		
		stack.setTagCompound(tag);
		stack.setItemName(tile.itemName);
		return stack;
	}
	
	private static void getEntities(World world, int x, int y, int z, Tent tent, int direction, NBTTagCompound tag) {
		int tempX = x - (tent.getCenter() - 1);
		int tempZ = z - (tent.getCenter() - 1);
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double)x - (tent.getCenter() - 1), (double)y, (double)z- (tent.getCenter() - 1), (double)(x + (tent.getCenter() - 1)), (double)(y + tent.structure[0].length), (double)(z + (tent.getCenter() - 1))).expand((tent.getCenter() - 1), tent.structure[0].length, (tent.getCenter() - 1));
        List list = world.getEntitiesWithinAABB(Entity.class, axisalignedbb);
        Iterator iterator = list.iterator();
        Entity entity;
        int index = 0;
		while(iterator.hasNext()){
			entity = (Entity) iterator.next();
			if(entity != null && !(entity instanceof EntityLivingBase)){
				tag.setInteger("entityID:" + index, EntityList.getEntityID(entity));
				tag.setInteger("entityX:" + index, (int) Math.abs((x - entity.posX)));
				tag.setInteger("entityY:" + index, (int) Math.abs((y - entity.posY)));
				tag.setInteger("entityZ:" + index, (int) Math.abs((z - entity.posZ)));
				tag.setCompoundTag("entityNBT:" + index, entity.getEntityData());
				entity.setDead();
				index++;
			}
		}
		tag.setInteger("entities", index);
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
