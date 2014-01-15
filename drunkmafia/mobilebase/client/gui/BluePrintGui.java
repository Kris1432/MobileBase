package drunkmafia.mobilebase.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import drunkmafia.mobilebase.client.gui.container.BluePrintContainer;
import drunkmafia.mobilebase.lib.ModInfo;


public class BluePrintGui extends GuiContainer{
	
	public BluePrintGui() {
		super(new BluePrintContainer());
		xSize = 176;
		ySize = 50;
	}
	
	private ResourceLocation gui = new ResourceLocation(ModInfo.MODID, "textures/gui/BluePrint.png");
	
	private GuiTextField x, y, z;
	
	@Override
	public void initGui() {
		super.initGui();
		x = new GuiTextField(fontRenderer, guiLeft + 10, guiTop + 20, 20, 10);
		x.setFocused(false);
		x.setMaxStringLength(2);
	 	x.setText("8");
		 
		y = new GuiTextField(fontRenderer, guiLeft + 10, guiTop + 50, 20, 10);
		y.setFocused(false);
		y.setMaxStringLength(2);
		y.setText("8");
		 
		z = new GuiTextField(fontRenderer, guiLeft + 10, guiTop + 80, 20, 10);
		z.setFocused(false);
		z.setMaxStringLength(2);
		z.setText("8");
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {
		GL11.glColor4f(1, 1, 1, 1);
		
		System.out.println("Render");
		
		Minecraft.getMinecraft().renderEngine.bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		this.x.drawTextBox();
		this.y.drawTextBox();
		this.z.drawTextBox();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		
	}
	
}
