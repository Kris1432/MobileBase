package drunkmafia.mobilebase.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import drunkmafia.mobilebase.client.gui.container.BluePrintContainer;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.network.PacketHandler;


public class BluePrintGui extends GuiContainer{
	
	private int xCoord, yCoord, zCoord;
	private byte xTextSize = 0, yTextSize = 0, zTextSize = 0;
	
	public BluePrintGui(NBTTagCompound tag) {
		super(new BluePrintContainer());
		this.xSize = 176;
		this.ySize = 50;
		System.out.println("GUI Opened");
		if(tag.hasKey("postX")){
			this.xCoord = tag.getInteger("postX");
			this.yCoord = tag.getInteger("postY");
			this.zCoord = tag.getInteger("postZ");
		}
		
		if(tag.hasKey("xSize")){
			this.xTextSize = tag.getByte("xSize");
			this.yTextSize = tag.getByte("ySize");
			this.zTextSize = tag.getByte("zSize");
		}
	}
	
	private ResourceLocation gui = new ResourceLocation(ModInfo.MODID, "textures/gui/BluePrint.png");
	
	private GuiTextField x, y, z;
	
	@Override
	public void initGui() {
		super.initGui();
		x = new GuiTextField(fontRenderer, guiLeft + 20, guiTop + 20, 20, 10);
		x.setFocused(false);
		x.setMaxStringLength(2);
	 	x.setText(String.valueOf(xTextSize));
		 
		y = new GuiTextField(fontRenderer, guiLeft + 60, guiTop + 20, 20, 10);
		y.setFocused(false);
		y.setMaxStringLength(2);
		y.setText(String.valueOf(yTextSize));
		 
		z = new GuiTextField(fontRenderer, guiLeft + 100, guiTop + 20, 20, 10);
		z.setFocused(false);
		z.setMaxStringLength(2);
		z.setText(String.valueOf(zTextSize));
		
		GuiButton Save = new GuiButton(1, guiLeft + 130, guiTop + 14, 40, 20, "Save");
		buttonList.add(Save);
	}
	
	@Override
	protected void keyTyped(char cha, int index) {
		super.keyTyped(cha, index);
		x.textboxKeyTyped(cha, index);
		y.textboxKeyTyped(cha, index);
		z.textboxKeyTyped(cha, index);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int clickTime) {
		super.mouseClicked(x, y, clickTime);
		this.x.mouseClicked(x, y, clickTime);
		this.y.mouseClicked(x, y, clickTime);
		this.z.mouseClicked(x, y, clickTime);
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
		if(z != null && !x.getText().isEmpty() && !y.getText().isEmpty() && !z.getText().isEmpty()){
			System.out.println("Sending Packet");
			PacketHandler.sendTextBoxInfo(0, Integer.parseInt(x.getText()), Integer.parseInt(y.getText()), Integer.parseInt(z.getText()));
		}
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		if(z != null && !x.getText().isEmpty() && !y.getText().isEmpty() && !z.getText().isEmpty()){
			System.out.println("Sending Packet");
			PacketHandler.sendTextBoxInfo(1, Integer.parseInt(x.getText()), Integer.parseInt(y.getText()), Integer.parseInt(z.getText()));
		}
	}
}
