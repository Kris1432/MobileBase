package drunkmafia.mobilebase.client.renderer.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import drunkmafia.mobilebase.lib.ModInfo;

public class DeskRenderer extends TileEntitySpecialRenderer {

	private IModelCustom model = AdvancedModelLoader.loadModel("/assets/mobilebase/models/desk.obj");
	private ResourceLocation texture = new ResourceLocation(ModInfo.MODID, "/textures/models/desk_texture.jpg");
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float paritalTickTime) {
		GL11.glPushMatrix();
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		model.renderAll();
		
		GL11.glPopMatrix();		
	}

}
