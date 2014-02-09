package drunkmafia.mobilebase.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import drunkmafia.mobilebase.client.gui.component.ProgressBar;
import drunkmafia.mobilebase.client.gui.container.BluePrintContainer;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.network.PacketHandler;


public class BluePrintGui extends GuiContainer{
	
	private int xCoord, yCoord, zCoord;
	private byte xTextSize = 0, yTextSize = 0, zTextSize = 0;
	private String text, error;
	
	public BluePrintGui(NBTTagCompound tag) {
		super(new BluePrintContainer());
		this.xSize = 176;
		this.ySize = 60;
		if(tag.hasKey("postX")){
			this.xCoord = tag.getInteger("postX");
			this.yCoord = tag.getInteger("postY");
			this.zCoord = tag.getInteger("postZ");
		}
		
		if(tag.hasKey("xSize")){
			this.xTextSize = tag.getByte("xSize");
			this.yTextSize = tag.getByte("ySize");
			this.zTextSize = tag.getByte("zSize");
			this.text = tag.getString("name");
		}
	}
	
	private ResourceLocation gui = new ResourceLocation(ModInfo.MODID, "textures/gui/BluePrint.png");
	private GuiTextField x, y, z, name;
	
	private ProgressBar bar = new ProgressBar(100, 100, ySize, 0, 101, 11, true);
	
	@Override
	public void initGui() {
		super.initGui();
		x = new GuiTextField(fontRenderer, guiLeft + 20, guiTop + 30, 20, 10);
		x.setFocused(false);
		x.setMaxStringLength(2);
	 	x.setText(String.valueOf(xTextSize));
		 
		y = new GuiTextField(fontRenderer, guiLeft + 60, guiTop + 30, 20, 10);
		y.setFocused(false);
		y.setMaxStringLength(2);
		y.setText(String.valueOf(yTextSize));
		 
		z = new GuiTextField(fontRenderer, guiLeft + 100, guiTop + 30, 20, 10);
		z.setFocused(false);
		z.setMaxStringLength(2);
		z.setText(String.valueOf(zTextSize));
		
		name = new GuiTextField(fontRenderer, guiLeft + 40, guiTop + 10, 80, 10);
		name.setFocused(false);
		name.setMaxStringLength(15);
		if(text != null)
			name.setText(text);
		else
			name.setText("Set Name");
		GuiButton Save = new GuiButton(1, guiLeft + 130, guiTop + 14, 40, 20, "Save");
		buttonList.add(Save);
	}
	
	@Override
	protected void keyTyped(char cha, int index) {
		x.textboxKeyTyped(cha, index);
		y.textboxKeyTyped(cha, index);
		z.textboxKeyTyped(cha, index);
		name.textboxKeyTyped(cha, index);
		
		if(!name.isFocused() && index == 1 || cha == this.mc.gameSettings.keyBindInventory.keyCode){
            this.mc.thePlayer.closeScreen();
        }
	}
	
	@Override
	protected void mouseClicked(int x, int y, int clickTime) {
		super.mouseClicked(x, y, clickTime);
		this.x.mouseClicked(x, y, clickTime);
		this.y.mouseClicked(x, y, clickTime);
		this.z.mouseClicked(x, y, clickTime);
		name.mouseClicked(x, y, clickTime);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {
		GL11.glColor4f(1, 1, 1, 1);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		this.x.drawTextBox();
		this.y.drawTextBox();
		this.z.drawTextBox();
		name.drawTextBox();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString("X:", 10, 30, GuiColour.GRAY.toRGB());
		fontRenderer.drawString("Y:", 50, 30, GuiColour.GRAY.toRGB());
		fontRenderer.drawString("Z:", 90, 30, GuiColour.GRAY.toRGB());
		fontRenderer.drawString("Name:", 10, 10, GuiColour.GRAY.toRGB());
		
		if(error != null){
			fontRenderer.drawString("Error:", 5, 45, GuiColour.RED.toRGB());
			fontRenderer.drawString(error, 40, 45, GuiColour.GRAY.toRGB());
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		error = null;
		if(z != null && !x.getText().isEmpty() && !y.getText().isEmpty() && !z.getText().isEmpty() && !name.getText().isEmpty()){
			int x = Integer.parseInt(this.x.getText());
			int y = Integer.parseInt(this.y.getText());
			int z = Integer.parseInt(this.z.getText());
			if((x <= 7 && x > 1) && (y <= 12 && y > 1) && (z <= 7 && z > 1)){
				PacketHandler.sendTextBoxInfo(0, x, y, z, name.getText());
				this.mc.thePlayer.closeScreen();
			}else if(x <= 7 || y <= 7 || z <= 7)
				error = "Must be bigger than 1";
			else if(x > 1 || y > 1 || z > 1)
				error = "Must be smaller than 11";
		}else
			error = "All fields must be filled";
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		if(z != null && !x.getText().isEmpty() && !y.getText().isEmpty() && !z.getText().isEmpty() && !name.getText().isEmpty()){
			PacketHandler.sendTextBoxInfo(1, Integer.parseInt(x.getText()), Integer.parseInt(y.getText()), Integer.parseInt(z.getText()), name.getText());
		}
	}
}
