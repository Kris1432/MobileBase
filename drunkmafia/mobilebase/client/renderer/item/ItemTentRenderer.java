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
			int[][][] tempStruc = new int[tag.getInteger("tentY")][tag.getInteger("tentX")][tag.getInteger("tentZ")];
			for(int y1 = 0; y1 < tempStruc.length; y1++)
				for(int x1 = 0; x1 < tempStruc[y1].length; x1++)
					tempStruc[y1][x1] = tag.getIntArray("tentStructure:" + y1 + x1);
			
			int tempX = -4;
			int tempZ = -4;		
			int index = 0;
			for(int a1 = 0; a1 < tempStruc.length; a1++){
				for(int a2 = 0; a2 < tempStruc[a1].length; a2++){
					for(int a3 = 0; a3 < tempStruc[a1][a2].length; a3++){
						int temp = tempStruc[a1][a2][a3];
						if(temp == 1){
							
							GL11.glPushMatrix();
							
							switch (type) {
								case EQUIPPED:
									GL11.glTranslatef(0F, 0.7F, 0.5F);
									GL11.glScalef(0.2F, 0.2F, 0.2F);
									break;
								case EQUIPPED_FIRST_PERSON:
									GL11.glScalef(0.2F, 0.2F, 0.2F);
									GL11.glTranslatef(0F, 0.7F, 0.5F);
									GL11.glRotatef(180, 0, 1, 0);
									break;
								case INVENTORY:
									GL11.glTranslatef(0.6F, 0.3F, 0.6F);
									GL11.glScalef(0.12F, 0.12F, 0.12F);
									GL11.glRotatef(180, 0, 1, 0);
									break;
								default:
									GL11.glTranslatef(0F, 0F, 0F);
									GL11.glScalef(0.1F, 0.1F, 0.1F);
							}
							renderStackAsBlock(new ItemStack(Block.cloth.blockID, 0, item.getItemDamage()), a3 + tempX, a1, a2 + tempZ, 0.9F, 0F);
							GL11.glPopMatrix();
						}
					}
				}
			}
		}
	}
	
	public void renderStackAsBlock(ItemStack stack, float x, float y, float z, float scale, float rotation) { 
		GL11.glTranslatef(x, y, z);
        TextureManager textMan = Minecraft.getMinecraft().getTextureManager();
        textMan.bindTexture(textMan.getResourceLocation(0));
        RenderManager.instance.itemRenderer.renderItem(null, stack, 10);
	}
}
