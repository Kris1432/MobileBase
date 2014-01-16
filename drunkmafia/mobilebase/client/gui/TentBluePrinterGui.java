package drunkmafia.mobilebase.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import drunkmafia.mobilebase.client.gui.component.Page;
import drunkmafia.mobilebase.client.gui.component.RectangleButton;
import drunkmafia.mobilebase.client.gui.component.TabButton;
import drunkmafia.mobilebase.client.gui.component.TabManager;
import drunkmafia.mobilebase.client.gui.container.TentBluePrinterContainer;
import drunkmafia.mobilebase.client.gui.pages.blueprinter.BluePrintPage;
import drunkmafia.mobilebase.client.gui.pages.blueprinter.HelpPage;
import drunkmafia.mobilebase.client.gui.pages.blueprinter.TentPage;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.tileentity.TentBluePrinterTile;

public class TentBluePrinterGui extends GuiContainer{

	private TentBluePrinterTile tile;
	
	//Tabs Varibles
	private TabManager tabs;
	private Page bluePrintPage = new BluePrintPage();
	private Page tentPage = new TentPage();
	private Page helpPage = new HelpPage();
	private TabButton bluePrintButton, tentButton, helpButton;
	private RectangleButton[][][] grid;
	
	private int lastTick, tick = 0;
	
	//TextFields
	private GuiTextField x, y, z;
	
	public TentBluePrinterGui(TentBluePrinterTile tile, EntityPlayer player) {
		super(new TentBluePrinterContainer(tile, player));
		
		this.tile = tile;
		grid = new RectangleButton[16][16][16];
		
		field_146999_f = 176;
		field_147000_g = 218; 
		bluePrintButton = new TabButton(37, 14, 0, field_147000_g, "Design");
		tentButton = new TabButton(37, 14, 0, field_147000_g, "3D");
		helpButton = new TabButton(37, 14, 0, field_147000_g, "Help");
		
		tabs = new TabManager(50, 13, 50, 29);
		tabs.addTab(bluePrintButton, bluePrintPage);
		tabs.addTab(tentButton, tentPage);
		tabs.addTab(helpButton, helpPage);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		 x = new GuiTextField(Minecraft.getMinecraft().fontRenderer, field_147003_i + 10, field_147009_r + 20, 20, 10);
		 x.func_146195_b(false);
		 x.func_146203_f(2);
		 x.func_146180_a("8");
		 
		 y = new GuiTextField(Minecraft.getMinecraft().fontRenderer, field_147003_i + 10, field_147009_r + 50, 20, 10);
		 y.func_146195_b(false);
		 y.func_146203_f(2);
		 y.func_146180_a("8");
		 
		 z = new GuiTextField(Minecraft.getMinecraft().fontRenderer, field_147003_i + 10, field_147009_r + 80, 20, 10);
		 z.func_146195_b(false);
		 z.func_146203_f(2);
		 z.func_146180_a("8");
	}	
	
	@Override
	public void keyTyped(char c, int index){
		super.keyTyped(c, index);
		x.func_146201_a(c, index); //textBoxTyped
		y.func_146201_a(c, index); //textBoxTyped
		z.func_146201_a(c, index); //textBoxTyped
		updateGrid(); 
	}
	
	public void updateGrid(){
		//func_146179_b() == getText()
		System.out.println("X: " + x.func_146179_b());
		System.out.println("Y: " + y.func_146179_b());
		System.out.println("Z: " + z.func_146179_b());
		
		int x = Integer.getInteger(this.x.func_146179_b()) == null ? 1 : Integer.getInteger(this.x.func_146179_b());
		int y = Integer.getInteger(this.y.func_146179_b()) == null ? 1 : Integer.getInteger(this.y.func_146179_b());
		int z = Integer.getInteger(this.z.func_146179_b()) == null ? 1 : Integer.getInteger(this.z.func_146179_b());
		
		grid = new RectangleButton[y][x][z];
		for(int y1 = 0; y > grid.length; y++){
			for(int x1 = 0; y > grid[y].length; x++){
				for(int z1 = 0; y > grid[y][x].length; z++){
					grid[y1][x1][z1] = new RectangleButton(100, 100, 8, 8, field_146999_f, 0);
				}
			}
		}
	}
	
	private ResourceLocation gui = new ResourceLocation(ModInfo.MODID, "textures/gui/TentBluePrinter.png");
	
	//drawGuiContainerForeground
	@Override
	protected void func_146979_b(int x, int y) {
		Minecraft.getMinecraft().fontRenderer.drawString("X:", 10, 10, GuiColour.GRAY.toRGB());
		Minecraft.getMinecraft().fontRenderer.drawString("Y:", 10, 40, GuiColour.GRAY.toRGB());
		Minecraft.getMinecraft().fontRenderer.drawString("Z:", 10, 70, GuiColour.GRAY.toRGB());
		
		tabs.drawForeground(this, Minecraft.getMinecraft().fontRenderer, field_147003_i, field_147009_r);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int clickTime) {
		super.mouseClicked(x, y, clickTime);
		tabs.mouseClick(x, y, field_147003_i, field_147009_r);
		this.x.func_146192_a(x, y, clickTime); //mouseClicked
		this.y.func_146192_a(x, y, clickTime); //mouseClicked
		this.z.func_146192_a(x, y, clickTime); //mouseClicked
	}

	
	//drawGuiContainerBackgroundLayer
	@Override
	protected void func_146976_a(float var1, int var2, int var3) {
		GL11.glColor4f(1, 1, 1, 1);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(gui);
		drawTexturedModalRect(field_147003_i, field_147009_r, 0, 0, field_146999_f, field_147000_g);	
		
		tabs.drawBackground(this, field_147003_i, field_147009_r, var2, var3, var1);
		this.x.func_146194_f();
		this.y.func_146194_f();
		this.z.func_146194_f();
		
		for(int y = 0; y > grid.length; y++){
			for(int x = 0; y > grid[y].length; x++){
				for(int z = 0; y > grid[y][x].length; z++){
					grid[y][x][z].drawRect(this, field_147003_i, field_147009_r, var2, var3);
				}
			}
		}
		
	}
}
