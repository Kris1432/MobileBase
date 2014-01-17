package drunkmafia.mobilebase.client.renderer.item;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import drunkmafia.mobilebase.tents.Tent;

public class ItemTentRenderer implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
	
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		NBTTagCompound tag = item.stackTagCompound;
		
		if(tag != null){
			GL11.glPushMatrix();
			
			int[][][] tempStruc = new int[tag.getInteger("tentY")][tag.getInteger("tentX")][tag.getInteger("tentZ")];
			for(int y1 = 0; y1 < tempStruc.length; y1++)
				for(int x1 = 0; x1 < tempStruc[y1].length; x1++)
					tempStruc[y1][x1] = tag.getIntArray("tentStructure:" + y1 + x1);
			
			switch (type) {
				case EQUIPPED:
					GL11.glTranslatef(0.4F, 1F, 0.6F);
					break;
				case EQUIPPED_FIRST_PERSON:
					GL11.glTranslatef(0, 0.7F, 0.5F);
					GL11.glRotatef(180, 0, 1, 0);
					break;
				default:
			}
			
			int tempX = -4;
			int tempZ = -4;
			
			for(int a1 = 0; a1 < tempStruc.length; a1++){
				for(int a2 = 0; a2 < tempStruc[a1].length; a2++){
					for(int a3 = 0; a3 < tempStruc[a1][a2].length; a3++){
						int temp = tempStruc[a1][a2][a3];
						if(temp != 0){
							renderStackAsBlock(new ItemStack(Block.cloth), tempX + a2, a1, tempZ + a3, 0.5F, 90F);
						}
					}
				}
			}
			GL11.glPopMatrix();
		}
	}
	
	public void renderStackAsBlock(ItemStack stack, int x, int y, int z, float scale, float rotation) { 
        TextureManager textMan = Minecraft.getMinecraft().getTextureManager();
        textMan.bindTexture(textMan.getResourceLocation(0));
        RenderManager.instance.itemRenderer.renderItem(null, stack, 10);
	}
}
