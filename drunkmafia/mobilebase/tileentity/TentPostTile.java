package drunkmafia.mobilebase.tileentity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import drunkmafia.mobilebase.tents.Tent;
import drunkmafia.mobilebase.tents.TentHelper;

public class TentPostTile extends TileEntity{
	
	public Tent tentType;
	private int tick;
	public int woolType, tentID, direction;
	public int[][] blocks;
	public String itemName, directionName;
	public boolean isDummyTile;
	
	public TentPostTile() {
		tick = 0;
	}
	
	@Override
	public void func_145845_h() {
		if(field_145850_b.isRemote || isDummyTile) return;
		tick++;
		if(tick <= 30){
			tick = 0;
			if(!TentHelper.isTentStable(field_145850_b, field_145851_c, field_145848_d, field_145849_e, woolType, tentType, direction)){
				field_145850_b.func_147468_f(field_145851_c, field_145848_d, field_145849_e); //set block to air
			}
		}
	}
	
	public void destoryThis(){
		ItemStack stack = TentHelper.getItemVersionOfTent(field_145850_b, field_145851_c, field_145848_d, field_145849_e, woolType, tentType, direction);
		EntityItem item = new EntityItem(field_145850_b, field_145851_c, field_145848_d, field_145849_e, stack);
		TentHelper.breakTent(field_145850_b, field_145851_c, field_145848_d, field_145849_e, tentType, direction, stack.getTagCompound());        
		field_145850_b.spawnEntityInWorld(item);
	}
	
	@Override
	public void func_145841_b(NBTTagCompound tag) {
		super.func_145841_b(tag);
		if(!isDummyTile){
			tag.setByte("direction", (byte) direction);
			tag.setString("directionName", directionName);
			
			tag.setInteger("tentY", tentType.getTentY());
			tag.setInteger("tentX", tentType.getTentX());
			tag.setInteger("tentZ", tentType.getTentZ());
			for(int y = 0; y < tentType.getTentY(); y++)
				for(int x = 0; x < tentType.getTentX(); x++)
					tag.setIntArray("tentStructure:" + y + x, tentType.getStructure()[0][y][x]);
			
			/*
			for(int y = 0; y < tentType.getStructure().length; y++)
				for(int x = 0; x < tentType.getStructure()[y].length; x++)
					for(int z = 0; z < tentType.getStructure()[y][x].length; z++)
						tag.setByte("tentStructure:" + y + x + z, (byte) tentType.getStructure()[0][y][x][z]);
			*/
			tag.setInteger("woolType", woolType);
			tag.setInteger("blocksLength", blocks.length);
			tag.setInteger("blocksLength0", blocks[0].length);
			tag.setInteger("tentID", tentID);
			tag.setString("itemName", itemName);
			for(int i = 0; i < blocks.length; i++){
				tag.setIntArray("blocks:" + i, blocks[i]);
			}
		}
		tag.setBoolean("isDummy", isDummyTile);
	}
	
	@Override
	public void func_145839_a(NBTTagCompound tag) {
		super.func_145839_a(tag);
		if(!isDummyTile){
			direction = tag.getByte("direction");
			directionName = tag.getString("directionName");
			
			int[][][] temp = new int[tag.getInteger("tentY")][tag.getInteger("tentX")][tag.getInteger("tentZ")];
			for(int y = 0; y < temp.length; y++)
				for(int x = 0; x < temp[y].length; x++)
					temp[y][x] = tag.getIntArray("tentStructure:" + y + x);
			
			tentType = new Tent(temp);
			woolType = tag.getInteger("woolType");
			tentID = tag.getInteger("tentID");
			blocks = new int[tag.getInteger("blocksLength")][tag.getInteger("blocksLength0")];
			itemName = tag.getString("itemName");
			for(int i = 0; i < blocks.length; i++){
				blocks[i] = tag.getIntArray("blocks:" + i);
			}
		}
		isDummyTile = tag.getBoolean("isDummyTile");
	}
}
