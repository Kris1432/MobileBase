package drunkmafia.mobilebase.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import drunkmafia.mobilebase.client.gui.container.TentBluePrinterContainer;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.tileentity.TentBluePrinterTile;

public class TentBluePrinterGui extends GuiContainer{

	private TentBluePrinterTile tile;
	
	public TentBluePrinterGui(TentBluePrinterTile tile, EntityPlayer player) {
		super(new TentBluePrinterContainer(tile, player));
		
		this.tile = tile;
		
		xSize = 176;
		ySize = 218;
	}
	
	private ResourceLocation gui = new ResourceLocation(ModInfo.MODID, "textures/gui/TentBuilder.png");
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1, 1, 1, 1);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
	}
}
