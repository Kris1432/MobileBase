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
		field_146999_f = 176;
		field_147000_g = 50;
	}
	
	private ResourceLocation gui = new ResourceLocation(ModInfo.MODID, "textures/gui/BluePrint.png");
	
	private GuiTextField x, y, z;
	
	@Override
	public void initGui() {
		super.initGui();
		x = new GuiTextField(Minecraft.getMinecraft().fontRenderer, field_147003_i + 10, field_147009_r + 20, 20, 10);
		x.func_146195_b(false); //setFocused
		x.func_146203_f(2); //setMaxStringLength
	 	x.func_146180_a("8"); //setText
		 
		y = new GuiTextField(Minecraft.getMinecraft().fontRenderer, field_147003_i + 10, field_147009_r + 50, 20, 10);
		y.func_146195_b(false); //setFocused
		y.func_146203_f(2); //setMaxStringLength
		y.func_146180_a("8"); //setText
		 
		z = new GuiTextField(Minecraft.getMinecraft().fontRenderer, field_147003_i + 10, field_147009_r + 80, 20, 10);
		z.func_146195_b(false); //setFocused
		z.func_146203_f(2); //setMaxStringLength
		z.func_146180_a("8"); //setText
	}

	@Override
	protected void func_146976_a(float var1, int var2, int var3) {		
	GL11.glColor4f(1, 1, 1, 1);
	
	System.out.println("Render");
	
	Minecraft.getMinecraft().renderEngine.bindTexture(gui);
	//drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	drawTexturedModalRect(field_147003_i, field_147009_r, 0, 0, field_146999_f, field_147000_g);
	
	this.x.func_146194_f(); //drawTextBox
	this.y.func_146194_f();
	this.z.func_146194_f();
	}
	
}
