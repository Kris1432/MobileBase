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
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import drunkmafia.mobilebase.block.ModBlocks;
import drunkmafia.mobilebase.lib.ModInfo;

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
		}
	}
	
	public int[][][] getTentStructure(ByteArrayDataInput reader, EntityPlayer entityPlayer, World world){
		ItemStack stack = entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem];
		NBTTagCompound tag = stack.getTagCompound();	
		
		if(tag != null)	{
			int x = tag.getInteger("postX");
			int y = tag.getInteger("postY");
			int z = tag.getInteger("postZ");
			
			int xSize = reader.readByte();
			int ySize = reader.readByte();
			int zSize = reader.readByte();
			
			int i = 0;
			
			for(int a1 = -xSize; a1 < xSize; a1++){
				for(int a2 = -ySize; a2 < ySize; a2++){
					for(int a3 = -zSize; a3 < zSize; a3++){
						if(world.getBlockId(a2 + x, a1 + y, a3 + z) == ModBlocks.wool.blockID){
							i++;
						}
					}
				}
			}
			
			System.out.println(i);
		}
		return null;
	}
	
	public static void sendBlueprintPacket(int x, int y, int z, int xSize, int ySize, int zSize){
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);
		System.out.println("Packet Sent");

		try {
			dataStream.writeByte((byte)0);
			dataStream.writeByte((byte)xSize);	
			dataStream.writeByte((byte)ySize);	
			dataStream.writeByte((byte)zSize);	
			
			PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(ModInfo.CHANNEL, byteStream.toByteArray()));
		}catch(IOException ex) {
			System.err.append("[" + ModInfo.MODID + "] Error! Failed to send blueprint packet.");
			ex.printStackTrace();
		}
	}

}
