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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import drunkmafia.mobilebase.block.BlockBreaking;
import drunkmafia.mobilebase.block.InfoBlock;
import drunkmafia.mobilebase.block.ModBlocks;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.tileentity.*;

public class TentHelper {
	
	public static boolean buildTent(World world, int x, int y, int z, ItemStack stack, int direction, Tent tent){
		int tempX = x - (tent.getCenterX(direction) - 1);
		int tempZ = z - (tent.getCenterZ(direction) - 1);
		NBTTagCompound tag = stack.getTagCompound();
			
		if(isAreaClear(world, x, y, z, tag, tent, direction)){
			InfoBlock[][] blocks = getFloorBlocks(world, x, y, z, tent, direction);
			int[][][][] tStruct = tent.getStructure();
			
			TileEntity tentPostTile = null;
			for(int a1 = 0; a1 < tStruct[direction].length; a1++){
				for(int a2 = 0; a2 < tStruct[direction][0].length; a2++){
					for(int a3 = 0; a3 < tStruct[direction][0][0].length; a3++){
						int temp = tStruct[direction][a1][a2][a3];
						if(temp == -1){
							tentPostTile = new TentPostTile(); 
							world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, ModBlocks.tentPost.blockID);
                            world.setBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ, tentPostTile);
                            TentPostTile tile = (TentPostTile)world.getBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ);
                            tile.tentType = tent;
                            tile.woolType = stack.getItemDamage();
                            tile.direction = direction;
                            tile.directionName = tag.getString("directionName");
                            tile.blocks = blocks;
                            tile.itemName = stack.getDisplayName();
                            break;
						}
						if(tentPostTile != null)
							break;
					}
					if(tentPostTile != null)
						break;
				}
				if(tentPostTile != null)
					break;
			}
			
			for(int a1 = 0; a1 < tStruct[direction].length; a1++){
				for(int a2 = 0; a2 < tStruct[direction][0].length; a2++){
					for(int a3 = 0; a3 < tStruct[direction][0][0].length; a3++){
						int temp = tStruct[direction][a1][a2][a3];
						if(temp != 0){
							if(temp == 1){
								world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, ModBlocks.wool.blockID, stack.getItemDamage(), 3);
								((TentWoolTile) world.getBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ)).setPostPos(tentPostTile.xCoord, tentPostTile.yCoord, tentPostTile.zCoord);
							}else if(temp == 2){
								world.setBlock(a3 + tempX, a1 + y, a2 + tempZ, ModBlocks.tentPost.blockID);
								((TentPostTileDummy) world.getBlockTileEntity(a3 + tempX, a1 + y, a2 + tempZ)).setPostPos(tentPostTile.xCoord, tentPostTile.yCoord, tentPostTile.zCoord);
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
		int tempX = x - (tent.getCenterX(direction) - 1);
		int tempZ = z - (tent.getCenterZ(direction) - 1);
		int[][][][] structure = tent.getStructure();
		InfoBlock[][] blocks = new InfoBlock[structure[direction][0][0].length][structure[direction][0].length];
		for(int a1 = 0; a1 < blocks.length; a1++)
			for(int a2 = 0; a2 < blocks[a1].length; a2++)
				blocks[a1][a2] = new InfoBlock(world.getBlockId(a1 + tempX, y, a2 + tempZ), world.getBlockMetadata(a1 + tempX, y, a2 + tempZ));
		return blocks;
	}

	public static boolean isAreaClear(World world, int x, int y, int z, NBTTagCompound tag, Tent tent, int direction){
		int tempX = x - (tent.getCenterX(direction) - 1);
		int tempZ = z - (tent.getCenterZ(direction) - 1);
		int index = 0;
		boolean isBedrock = false;
		int[][][][] structure = tent.getStructure();
		for(int a1 = 0; a1 < tent.getStructure()[direction].length; a1++){
			for(int a2 = 0; a2 < tent.getStructure()[direction][0].length; a2++){
				for(int a3 = 0; a3 < tent.getStructure()[direction][0][0].length; a3++){
					int temp = structure[direction][a1][a2][a3];
					if((a1 == 0 || world.isAirBlock(a3 + tempX, a1 + y, a2 + tempZ)) && (temp == 1 || temp == -1 || temp == 2))
						index++;
					if(a1 == 0 && world.getBlockId(a3 + tempX, a1 + y, a2 + tempZ) == Block.bedrock.blockID){
						isBedrock = true;
						return false;
					}
				}
			}
		}		
		return index == tent.getStrucutureCount();
	}
	
	public static boolean reBuildInside(World world, int x, int y, int z, NBTTagCompound tag, Tent tent, int direction){
		if(world.isRemote) return false;
		int tempX = x - (tent.getCenterX(direction) - 1);
		int tempZ = z - (tent.getCenterZ(direction) - 1);
		int index = 0;
		if(tag != null){
			int[][][][] structure = tent.getStructure();
			for(int a1 = 0; a1 < structure[direction].length; a1++){
				for(int a2 = 0; a2 < structure[direction][0].length; a2++){
					for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
						int temp = structure[direction][a1][a2][a3];	
						if(temp == 5){
							index++;
							if(tag.hasKey("blockID:" + index)){
								placeBlockAt(world, a3 + tempX, a1 + y, a2 + tempZ, tag, index);
							}
						}
					}
				}
			}
			
			for(int a1 = 0; a1 < structure[direction].length; a1++){
				for(int a2 = 0; a2 < structure[direction][0].length; a2++){
					for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
						int temp = structure[direction][a1][a2][a3];	
						if(temp == 5){
							world.markBlockForUpdate(a3 + tempX, a1 + y, a2 + tempZ);
						}
					}
				}
			}
		}
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
		BlockBreaking.setBlock(world, x, y, z, tag.getInteger("blockID:" + index), tag.getInteger("blockMETA:" + index), 0);
		if(tag.hasKey("blockTILE:" + index)){
			TileEntity tile = TileEntity.createAndLoadEntity(tag.getCompoundTag("blockTILE:" + index));
			if(tile != null)
				world.setBlockTileEntity(x, y, z, tile);
		}
	}
	
	public static boolean isTentStable(World world, int x, int y, int z, int woolType, Tent tent, int direction){
		int tempX = x - (tent.getCenterX(direction) - 1);
		int tempZ = z - (tent.getCenterZ(direction) - 1);
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
		closeInventories(world, x, y, z, tent, direction, tag);
		destoryTentInside(world, x, y, z, tent, direction, tag);
		destoryTentOutside(world, x, y, z, tent, direction);
		rebuildFloor(world, x, y, z, tent, direction);
	}
	
	public static void destoryTentInside(World world, int x, int y, int z, Tent tent, int direction, NBTTagCompound tag){
		int tempX = x - (tent.getCenterX(direction) - 1);
		int tempZ = z - (tent.getCenterZ(direction) - 1);
		int[][][][] structure = tent.getStructure();
		
		for(int a1 = 0; a1 < structure[direction].length; a1++){
			for(int a2 = 0; a2 < structure[direction][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
					int temp = structure[direction][a1][a2][a3];
					if(temp != 1 && temp != -1 && temp != 0){
						int id = world.getBlockId(a3 + tempX, a1 + y - 1, a2 + tempZ);
						if(world.blockExists(a3 + tempX, a1 + y - 1, a2 + tempZ) && !ModInfo.blackListedBlocks.contains(id)){
							BlockBreaking.setBlock(world, a3 + tempX, a1 + y - 1, a2 + tempZ, 0, 0, 3);		
						}else if(world.getBlockId(a3 + tempX, a1 + y - 1, a2 + tempZ) != 0 && ModInfo.blackListedBlocks.contains(id))
							printToClientSide("Black Listed block found: " + Block.blocksList[id].getUnlocalizedName());
					}
				}
			}
		}
		
		for(int a1 = 0; a1 < structure[direction].length; a1++){
			for(int a2 = 0; a2 < structure[direction][0].length; a2++){
				for(int a3 = 0; a3 < structure[direction][0][0].length; a3++){
					int temp = structure[direction][a1][a2][a3];
					if(temp != 1 && temp != -1 && temp != 0){
						if(world.getBlockTileEntity(a3 + tempX, a1 + y - 1, a2 + tempZ) != null)
							world.removeBlockTileEntity(a3 + tempX, a1 + y - 1, a2 + tempZ);
					}
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void printToClientSide(String text){
		Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromText(text));
	}
	
	public static void destoryTentOutside(World world, int x, int y, int z, Tent tent, int direction){
		int tempX = x - (tent.getCenterX(direction) - 1);
		int tempZ = z - (tent.getCenterZ(direction) - 1);
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
		
	public static void rebuildFloor(World world, int x, int y, int z, Tent tent, int direction) {
		int tempX = x - (tent.getCenterX(direction) - 1);
		int tempZ = z - (tent.getCenterZ(direction) - 1);
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
		int tempX = x - (tent.getCenterX(direction) - 1);
		int tempZ = z - (tent.getCenterZ(direction) - 1);
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
								tag.setInteger("blockID:" + index, world.getBlockId(a3 + tempX, a1 + y - 1, a2 + tempZ));
								tag.setInteger("blockMETA:" + index, world.getBlockMetadata(a3 + tempX, a1 + y - 1, a2 + tempZ));
								if(world.blockHasTileEntity(a3 + tempX, a1 + y - 1, a2 + tempZ)){
									NBTTagCompound tileTag = new NBTTagCompound();
									world.getBlockTileEntity(a3 + tempX, a1 + y - 1, a2 + tempZ).writeToNBT(tileTag);
									tag.setCompoundTag("blockTILE:" + index, tileTag);
								}
							}
						}
					}
				}
			}
		}
						
		tag.setByte("direction", (byte) direction);
		tag.setString("directionName", !tile.directionName.isEmpty() ? tile.directionName : "NULL DIRECTION");
		stack.setTagCompound(tag);
		stack.setItemName(tile.itemName);
		return stack;
	}
	
	private static void saveEntities(World world, int x, int y, int z, Tent tent, int direction, NBTTagCompound tag) {
		int tempX = x - (tent.getCenterX(direction) - 1);
		int tempZ = z - (tent.getCenterZ(direction) - 1);
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox((double)x - (tent.getCenterX(direction) - 1), (double)y, (double)z- (tent.getCenterZ(direction) - 1), (double)(x + (tent.getCenterX(direction) - 1)), (double)(y + tent.structure[0].length), (double)(z + (tent.getCenterZ(direction) - 1))).expand((tent.getCenterX(direction) - 1), tent.structure[0].length, (tent.getCenterZ(direction) - 1));
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
	
	private static void closeInventories(World world, int x, int y, int z, Tent tent, int direction, NBTTagCompound tag) {
		int tempX = x - (tent.getCenterX(direction) - 1);
		int tempZ = z - (tent.getCenterZ(direction) - 1);
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox((double)x - (tent.getCenterX(direction) - 1), (double)y, (double)z- (tent.getCenterZ(direction) - 1), (double)(x + (tent.getCenterX(direction) - 1)), (double)(y + tent.structure[0].length), (double)(z + (tent.getCenterZ(direction) - 1))).expand((tent.getCenterX(direction) - 1), tent.structure[0].length, (tent.getCenterZ(direction) - 1));
        List<Entity> list = world.getEntitiesWithinAABB(Entity.class, axisalignedbb);
		for(Entity entity : list)
			if(entity != null && entity instanceof EntityPlayer)
				((EntityPlayer)entity).closeScreen();
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
}
