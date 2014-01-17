package drunkmafia.mobilebase.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import drunkmafia.mobilebase.client.gui.container.BluePrintContainer;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.network.PacketHandler;


public class BluePrintGui extends GuiContainer{
	
	private int xCoord, yCoord, zCoord;
	
	public BluePrintGui(int x, int y, int z) {
		super(new BluePrintContainer());
		xSize = 176;
		ySize = 50;
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
	}
	
	private ResourceLocation gui = new ResourceLocation(ModInfo.MODID, "textures/gui/BluePrint.png");
	
	private GuiTextField x, y, z;
	
	@Override
	public void initGui() {
		super.initGui();
		x = new GuiTextField(fontRenderer, guiLeft + 20, guiTop + 20, 20, 10);
		x.setFocused(false);
		x.setMaxStringLength(2);
	 	x.setText("8");
		 
		y = new GuiTextField(fontRenderer, guiLeft + 60, guiTop + 20, 20, 10);
		y.setFocused(false);
		y.setMaxStringLength(2);
		y.setText("8");
		 
		z = new GuiTextField(fontRenderer, guiLeft + 100, guiTop + 20, 20, 10);
		z.setFocused(false);
		z.setMaxStringLength(2);
		z.setText("8");
		
		GuiButton Save = new GuiButton(1, guiLeft + 130, guiTop + 14, 40, 20, "Save");
		buttonList.add(Save);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {
		GL11.glColor4f(1, 1, 1, 1);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		this.x.drawTextBox();
		this.y.drawTextBox();
		this.z.drawTextBox();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString("X:", 10, 20, GuiColour.GRAY.toRGB());
		fontRenderer.drawString("Y:", 50, 20, GuiColour.GRAY.toRGB());
		fontRenderer.drawString("Z:", 90, 20, GuiColour.GRAY.toRGB());
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if(!x.getText().isEmpty() && !y.getText().isEmpty() && !z.getText().isEmpty()){
			System.out.println("Sending Packet");
			PacketHandler.sendBlueprintPacket(
					xCoord, 
					yCoord, 
					zCoord, 
					Integer.parseInt(x.getText()), 
					Integer.parseInt(y.getText()), 
					Integer.parseInt(z.getText()));
		}
	}
	
}
