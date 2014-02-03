package drunkmafia.mobilebase.tents;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import drunkmafia.mobilebase.block.InfoBlock;
import drunkmafia.mobilebase.block.ModBlocks;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.tileentity.TentPostTile;

public class TentHelper {
	
	public static boolean buildTent(World world, int x, int y, int z, ItemStack stack, int direction, Tent tent){
		int tempX = x - (tent.getCenterX() - 1);
		int tempZ = z - (tent.getCenterZ() - 1);
		NBTTagCompound tag = stack.getTagCompound();
			
		if(isAreaClear(world, x, y, z, tag, tent, direction)){
			InfoBlock[][] blocks = getFloorBlocks(world, x, y, z, tent, direction);
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
	
	private static InfoBlock[][] getFloorBlocks(World world, int x, int y, int z, Tent tent, int direction) {
		int tempX = x - (tent.getCenterX() - 1);
		int tempZ = z - (tent.getCenterZ() - 1);
		int[][][][] structure = tent.getStructure();
		InfoBlock[][] blocks = new InfoBlock[structure[direction][0][0].length][structure[direction][0].length];
		for(int a1 = 0; a1 < blocks.length; a1++){
			for(int a2 = 0; a2 < blocks[a1].length; a2++){
				blocks[a1][a2] = new InfoBlock(world.getBlockId(a1 + tempX, y, a2 + tempZ), world.getBlockMetadata(a1 + tempX, y, a2 + tempZ));
			}
		}
		return blocks;
	}

	public static boolean isAreaClear(World world, int x, int y, int z, NBTTagCompound tag, Tent tent, int direction){
		int tempX = x - (tent.getCenterX() - 1);
		int tempZ = z - (tent.getCenterZ() - 1);
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
		int tempX = x - (tent.getCenterX() - 1);
		int tempZ = z - (tent.getCenterZ() - 1);
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
								placeBlockAt(world, a3 + tempX, a1 + y, a2 + tempZ, tag, index);
							}
						}
					}
				}
			}			
		}
		
		//loadEntities(world, x, y, z, tag);
		cleanUpArea(world, x, y, z, tent, direction, tag);
		hasBeenBuilt(world, x, y, z, tag, tent, direction);
		return true;
	}
	
	public static void loadEntities(World world, int x, int y, int z, NBTTagCompound tag){
		int loopSize = tag.getInteger("entities");
		
		int[] oldPos = tag.getIntArray("oldPosition");
		if(oldPos.length > 0){
		int xPos = oldPos[0];
		int yPos = oldPos[1];
		int zPos = oldPos[2];
		int deltaX = Math.min(x, xPos) - Math.max(x, xPos);
		int deltaY = Math.min(y, yPos) - Math.max(y, yPos);
		int deltaZ = Math.min(z, zPos) - Math.max(z, zPos);
		if(x > xPos) deltaX *= -1;
		if(y > yPos) deltaY *= -1;
		if(z > zPos) deltaZ *= -1;
		
		for(int i = 0; i < loopSize; i++){
			NBTTagCompound eTag = tag.getCompoundTag("Entity:"+i);
			Entity e = EntityList.createEntityFromNBT(eTag, world);
			if(e != null){
				e.setPosition(e.prevPosX = e.posX += deltaX, e.prevPosY = e.posY += deltaY, e.prevPosZ = e.posZ += deltaZ);
			    if(e instanceof EntityHanging){
			    	EntityHanging eH = (EntityHanging)e;
			    	eH.xPosition += deltaX;
			    	eH.yPosition += deltaY;
			    	eH.zPosition += deltaZ;
			        world.spawnEntityInWorld(eH);
			        world.updateEntity(eH);
			    }else{
			        world.spawnEntityInWorld(e);
			        world.updateEntity(e);
			    } 
			    }
			}
		}
	}
	
	public static void placeBlockAt(World world, int x, int y, int z, NBTTagCompound tag, int index){
		world.setBlock(x, y, z, tag.getInteger("blockID:" + index));
		if(tag.getBoolean("blockHasTile:" + index)){
			TileEntity tile = TileEntity.createAndLoadEntity(tag.getCompoundTag("blockTILE:" + index));
			if(tile != null){
				tile.xCoord = x;
				tile.yCoord = y;
				tile.zCoord = z;
				world.setBlockTileEntity(x, y, z, tile);
			}
		}
		world.setBlockMetadataWithNotify(x, y, z, tag.getInteger("blockMETA:" + index), 1);
	}
	
	public static void hasBeenBuilt(World world, int x, int y, int z, NBTTagCompound tag, Tent tent, int direction){
		int tempX = x - (tent.getCenterX() - 1);
		int tempZ = z - (tent.getCenterZ() - 1);
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
									placeBlockAt(world, a3 + tempX, a1 + y, a2 + tempZ, tag, index);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static boolean isTentStable(World world, int x, int y, int z, int woolType, Tent tent, int direction){
		int tempX = -(tent.getCenterX() - 1) + x;
		int tempZ = -(tent.getCenterZ() - 1) + z;
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
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox((double)x - (tent.getCenterX() - 1), (double)y, (double)z- (tent.getCenterZ() - 1), (double)(x + (tent.getCenterX() - 1)), (double)(y + tent.structure[0].length), (double)(z + (tent.getCenterZ() - 1))).expand((tent.getCenterX() - 1), tent.structure[0].length, (tent.getCenterZ() - 1));
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
		int tempX = x - (tent.getCenterX() - 1);
		int tempZ = z - (tent.getCenterZ() - 1);
		int[][][][] structure = tent.getStructure();
		int tempY = tent.getStructure()[direction].length;
		
		for(int a1 = 0; a1 < structure[direction].length; a1++){
			tempY--;
			for(int a2 = 0; a2 < structure[direction][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
					int temp = structure[direction][tempY][a2][a3];
					if(temp != 1 && temp != -1 && temp != 0){
						int id = world.getBlockId(a3 + tempX, tempY + y - 1, a2 + tempZ);
						if(world.blockExists(a3 + tempX, tempY + y - 1, a2 + tempZ) && !ModInfo.blackListedBlocks.contains(id)){
							try{
								world.removeBlockTileEntity(a3 + tempX, tempY + y - 1, a2 + tempZ);
								world.setBlock(a3 + tempX, tempY + y - 1, a2 + tempZ, 0);
							}catch(NullPointerException e){
								FMLLog.log(Level.WARNING, "[" + ModInfo.MODID + "] An null pointer occured when trying to break a block, block ID: " + id + " Block Name: " + Block.blocksList[id].getLocalizedName() + ", to prevent this from happening again please add this block to the blacklist.");
								FMLLog.log(Level.WARNING, "[" + ModInfo.MODID + "] Notice: Sometimes the blocks will throw a null pointer, but will still be saved into memory.");							
							}
						}else if(world.getBlockId(a3 + tempX, tempY + y - 1, a2 + tempZ) != 0 && ModInfo.blackListedBlocks.contains(id))
							printToClientSide("Black Listed block found: " + Block.blocksList[id].getUnlocalizedName());
					}
				}
			}
		}
		cleanUpArea(world, x, y, z, tent, direction, tag);
		
		try {
			Thread.sleep(5L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void printToClientSide(String text){
		Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromText(text));
	}
	
	public static void destoryTentOutside(World world, int x, int y, int z, Tent tent, int direction){
		int tempX = x - (tent.getCenterX() - 1);
		int tempZ = z - (tent.getCenterZ() - 1);
		int[][][][] structure = tent.getStructure();
		int tempY = tent.getStructure()[direction].length;
		for(int a1 = 0; a1 < structure[direction].length; a1++){
			for(int a2 = 0; a2 < structure[direction][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
					int temp = structure[direction][a1][a2][a3];
					if(temp != 0 && (temp == 1 || temp == 2)){
						if(world.blockHasTileEntity(a3 + tempX, a1 + y - 1, a2 + tempZ))
							world.removeBlockTileEntity(a3 + tempX, a1 + y - 1, a2 + tempZ);
						world.setBlockToAir(a3 + tempX, a1 + y - 1, a2 + tempZ);
					}
				}
			}
		}
	}
		
	private static void rebuildFloor(World world, int x, int y, int z, Tent tent, int direction) {
		int tempX = x - (tent.getCenterX() - 1);
		int tempZ = z - (tent.getCenterZ() - 1);
		TentPostTile tile = (TentPostTile)world.getBlockTileEntity(x, y, z);
		for(int a1 = 0; a1 < tile.blocks.length; a1++){
			for(int a2 = 0; a2 < tile.blocks[a1].length; a2++){
				world.setBlock(tempX + a1, y - 1, tempZ + a2, tile.blocks[a1][a2].id);
				world.setBlockMetadataWithNotify(tempX + a1, y - 1, tempZ + a2, tile.blocks[a1][a2].meta, 3);
			}
		}
	}

	public static ItemStack getItemVersionOfTent(World world, int x, int y, int z, int woolType, Tent tent, int direction){
		TentPostTile tile = (TentPostTile) world.getBlockTileEntity(x, y, z);
		ItemStack stack = new ItemStack(ModItems.tent, 1, woolType);
		NBTTagCompound tag = new NBTTagCompound();
		int tempX = x - (tent.getCenterX() - 1);
		int tempZ = z - (tent.getCenterZ() - 1);
		int index = 0;
		
		int[][][][] structure = tent.getStructure();
		tent.writeToNBT(tag);
		for(int a1 = 0; a1 < structure[direction].length; a1++){
			for(int a2 = 0; a2 < structure[direction][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
					int temp = structure[direction][a1][a2][a3];
					if(temp == 5){
						index++;
						int id = world.getBlockId(a3 + tempX, a1 + y - 1, a2 + tempZ);
						if(!world.isAirBlock(a3 + tempX, a1 + y - 1, a2 + tempZ)){
							if(!ModInfo.blackListedBlocks.contains(id)){
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
						}else
							tag.setBoolean("blockExists" + index, false);
					}
				}
			}
		}
						
		tag.setByte("direction", (byte) direction);
		tag.setString("directionName", !tile.directionName.isEmpty() ? tile.directionName : "NULL DIRECTION");
		//saveEntities(world, x, y, z, tent, direction, tag);
		
		stack.setTagCompound(tag);
		stack.setItemName(tile.itemName);
		return stack;
	}
	
	private static void saveEntities(World world, int x, int y, int z, Tent tent, int direction, NBTTagCompound tag) {
		int tempX = x - (tent.getCenterX() - 1);
		int tempZ = z - (tent.getCenterZ() - 1);
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox((double)x - (tent.getCenterX() - 1), (double)y, (double)z- (tent.getCenterZ() - 1), (double)(x + (tent.getCenterX() - 1)), (double)(y + tent.structure[0].length), (double)(z + (tent.getCenterZ() - 1))).expand((tent.getCenterX() - 1), tent.structure[0].length, (tent.getCenterZ() - 1));
        List<Entity> list = world.getEntitiesWithinAABB(Entity.class, axisalignedbb);
        int index = 0;
        tag.setIntArray("oldPosition", new int[]{x, y-1, z});
		for(Entity entity : list){
			if(entity != null && !(entity instanceof EntityPlayer)){
				NBTTagCompound eTag = new NBTTagCompound();
				entity.writeToNBT(eTag);
				int temp = EntityList.getEntityID(entity);
                String id = EntityList.getStringFromID(temp);
                entity.writeToNBT(eTag);
                eTag.setString("id", id);
				tag.setCompoundTag("Entity:"+index, eTag);
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
		int tempX = x - (tent.getCenterX() - 1);
		int tempZ = z - (tent.getCenterZ() - 1);
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
