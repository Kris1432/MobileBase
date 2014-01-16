package drunkmafia.mobilebase.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import drunkmafia.mobilebase.client.gui.container.TentBuilderContainer;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.tileentity.TentBuilderTile;

public class TentBuilderGui extends GuiContainer{

	private TentBuilderTile tile;
	
	public TentBuilderGui(TentBuilderTile tile, EntityPlayer player) {
		super(new TentBuilderContainer(tile, player));
		
		this.tile = tile;
		
		field_146999_f = 176;
		field_147000_g = 218;
	}
	
	private ResourceLocation gui = new ResourceLocation(ModInfo.MODID, "textures/gui/TentBuilder.png");
	
	//drawGuiContainerBackgroundLayer
	@Override
	protected void func_146976_a(float var1, int var2, int var3) {
		GL11.glColor4f(1, 1, 1, 1);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(gui);
		drawTexturedModalRect(field_147003_i, field_147009_r, 0, 0, field_146999_f, field_147000_g);
	}
}
