package drunkmafia.mobilebase.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.tents.Tent;
import drunkmafia.mobilebase.tileentity.TentBuilderTile;

public class PacketHandler implements IPacketHandler{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		ByteArrayDataInput reader = ByteStreams.newDataInput(packet.data);
		
		EntityPlayer entityPlayer = (EntityPlayer) player;
		World world = entityPlayer.worldObj;
		int id = reader.readByte();
		
		switch(id){
			case 0:
				getTentStructure(reader, entityPlayer, world);
				break;
			case 1:
				saveBlueprintValues(reader, entityPlayer, world);
				break;
			case 2:
				TileEntity tile = world.getBlockTileEntity(reader.readInt(), reader.readInt(), reader.readInt());
				if(tile instanceof TentBuilderTile)
					((TentBuilderTile)tile).assembleTent();
				break;
		}
	}
	
	public void saveBlueprintValues(ByteArrayDataInput reader, EntityPlayer entityPlayer, World world){
		ItemStack stack = entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem];
		NBTTagCompound tag = stack.getTagCompound();
		if(tag != null){
			tag.setByte("xSize", reader.readByte());
			tag.setByte("ySize", reader.readByte());
			tag.setByte("zSize", reader.readByte());
			stack.setTagCompound(tag);
		}
	}
	
	public void getTentStructure(ByteArrayDataInput reader, EntityPlayer entityPlayer, World world){
		ItemStack stack = entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem];
		NBTTagCompound tag = stack.getTagCompound();	
		System.out.println("Recvied Packet");
		if(tag != null)	{
			int x = tag.getInteger("postX");
			int y = tag.getInteger("postY");
			int z = tag.getInteger("postZ");
			
			int xSize = reader.readByte();
			int ySize = reader.readByte();
			int zSize = reader.readByte();
			
			boolean controlPole = false;
			int tempX = x - xSize;
			int tempZ = z - zSize;
			int[][][] structure = new int[ySize][xSize * 2 - 2][zSize * 2 - 2];
			for(int a1 = 0; a1 < structure.length; a1++){
				for(int a2 = 0; a2 < structure[0].length; a2++){
					for(int a3 = 0; a3 < structure[0][0].length; a3++){
						int id = world.getBlockId(a2 + tempX, a1 + y, a3 + tempZ);
						if(id == Block.fence.blockID && !controlPole){
							structure[a1][a2][a3] = -1;
						}else if(id == Block.fence.blockID && controlPole){
							structure[a1][a2][a3] = 2;
						}else if(id == Block.cloth.blockID){
							structure[a1][a2][a3] = 1;
						}
					}
				}
			}
			
			Tent temp = new Tent(structure);
			temp.printStrucuture(); 
			
			/*
			
			ItemStack tent = new ItemStack(ModItems.tent);
			NBTTagCompound tentTag = new NBTTagCompound();
			tentTag.setInteger("tentY", structure.length);
			tentTag.setInteger("tentX", structure[0].length);
			tentTag.setInteger("tentZ", structure[0][0].length);
			for(int y1 = 0; y1 < structure.length; y1++)
				for(int x1 = 0; x1 < structure[0].length; x1++)
					tag.setIntArray("tentStructure:" + y1 + x1, structure[y1][x1]);
	    	
			tent.setItemName("Temp Tent");
			tent.setTagCompound(tag);
			entityPlayer.dropPlayerItem(tent);
			*/
	        System.out.println("Packet End");
		}
	}
	
	public static void sendTextBoxInfo(int id, int xSize, int ySize, int zSize){
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);
		System.out.println("Packet Sent");

		try {
			dataStream.writeByte((byte)id);
			dataStream.writeByte((byte)xSize);	
			dataStream.writeByte((byte)ySize);	
			dataStream.writeByte((byte)zSize);	
			
			PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(ModInfo.CHANNEL, byteStream.toByteArray()));
		}catch(IOException ex) {
			System.err.append("[" + ModInfo.MODID + "] Error! Failed to send blueprint packet.");
			ex.printStackTrace();
		}
	}
	
	public static void sendBuildPacket(int x, int y, int z){
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);
		
		try {
			dataStream.writeByte((byte)2);
			dataStream.writeInt(x);
			dataStream.writeInt(y);
			dataStream.writeInt(z);
			
			PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(ModInfo.CHANNEL, byteStream.toByteArray()));
		}catch(IOException ex) {
			System.err.append("[" + ModInfo.MODID + "] Error! Failed to send build packet");
			ex.printStackTrace();
		}
	}
}
