package drunkmafia.mobilebase.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class BlockBreaking {
	
	public static boolean setBlock(World world, int x, int y, int z, int id, int meta, int flagID){
        if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000){
            if (y < 0){
                return false;
            }else if (y >= 256){
                return false;
            }else{
                Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
                int k1 = 0;

                if ((flagID & 1) != 0){
                    k1 = chunk.getBlockID(x & 15, y, z & 15);
                }

                boolean flag = setBlockIDWithMetadata(x & 15, y, z & 15, id, meta, chunk);
                world.theProfiler.startSection("checkLight");
                world.updateAllLightTypes(x, y, z);
                world.theProfiler.endSection();

                if (flag){
                    if ((flagID & 2) != 0 && (!world.isRemote || (flagID & 4) == 0)){
                        world.markBlockForUpdate(x, y, z);
                    }
                }
                return flag;
            }
        }else{
            return false;
        }
    }
	
	public static boolean setBlockIDWithMetadata(int x, int y, int z, int id, int meta, Chunk chunk){
        int j1 = z << 4 | x;

        if (y >= chunk.precipitationHeightMap[j1] - 1){
        	chunk.precipitationHeightMap[j1] = -999;
        }

        int k1 = chunk.heightMap[j1];
        int l1 = chunk.getBlockID(x, y, z);
        int i2 = chunk.getBlockMetadata(x, y, z);

        if (l1 == id && i2 == meta){
            return false;
        }
        else{
            ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[y >> 4];
            boolean flag = false;

            if (extendedblockstorage == null){
                if (id == 0){
                    return false;
                }

                extendedblockstorage = chunk.getBlockStorageArray()[y >> 4] = new ExtendedBlockStorage(y >> 4 << 4, !chunk.worldObj.provider.hasNoSky);
                flag = y >= k1;
            }

            int j2 = chunk.xPosition * 16 + x;
            int k2 = chunk.zPosition * 16 + z;
            
            extendedblockstorage.setExtBlockID(x, y & 15, z, id);
            
            if (extendedblockstorage.getExtBlockID(x, y & 15, z) != id){
                return false;
            }else{
                extendedblockstorage.setExtBlockMetadata(x, y & 15, z, meta);                
                if (flag){
                    chunk.generateSkylightMap();
                }

                chunk.isModified = true;
                return true;
            }
        }
    }
}
