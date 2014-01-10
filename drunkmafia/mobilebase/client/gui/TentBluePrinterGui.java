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
		
		xSize = 176;
		ySize = 218; 
		bluePrintButton = new TabButton(37, 14, 0, ySize, "Design");
		tentButton = new TabButton(37, 14, 0, ySize, "3D");
		helpButton = new TabButton(37, 14, 0, ySize, "Help");
		
		tabs = new TabManager(50, 13, 50, 29);
		tabs.addTab(bluePrintButton, bluePrintPage);
		tabs.addTab(tentButton, tentPage);
		tabs.addTab(helpButton, helpPage);
	}
	
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
	public void keyTyped(char c, int index){
		super.keyTyped(c, index);
		x.textboxKeyTyped(c, index);
		y.textboxKeyTyped(c, index);
		z.textboxKeyTyped(c, index);
		updateGrid(); 
	}
	
	public void updateGrid(){
		System.out.println("X: " + x.getText());
		System.out.println("Y: " + y.getText());
		System.out.println("Z: " + z.getText());
		
		int x = Integer.getInteger(this.x.getText()) == null ? 1 : Integer.getInteger(this.x.getText());
		int y = Integer.getInteger(this.y.getText()) == null ? 1 : Integer.getInteger(this.y.getText());
		int z = Integer.getInteger(this.z.getText()) == null ? 1 : Integer.getInteger(this.z.getText());
		
		grid = new RectangleButton[y][x][z];
		for(int y1 = 0; y > grid.length; y++){
			for(int x1 = 0; y > grid[y].length; x++){
				for(int z1 = 0; y > grid[y][x].length; z++){
					grid[y1][x1][z1] = new RectangleButton(100, 100, 8, 8, xSize, 0);
				}
			}
		}
	}
	
	private ResourceLocation gui = new ResourceLocation(ModInfo.MODID, "textures/gui/TentBluePrinter.png");

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int mouseX, int mouseY) {
		GL11.glColor4f(1, 1, 1, 1);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);	
		
		tabs.drawBackground(this, guiLeft, guiTop, mouseX, mouseY, partialTickTime);
		this.x.drawTextBox();
		this.y.drawTextBox();
		this.z.drawTextBox();
		
		for(int y = 0; y > grid.length; y++){
			for(int x = 0; y > grid[y].length; x++){
				for(int z = 0; y > grid[y][x].length; z++){
					grid[y][x][z].drawRect(this, guiLeft, guiTop, mouseX, mouseY);
				}
			}
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRenderer.drawString("X:", 10, 10, GuiColour.GRAY.toRGB());
		fontRenderer.drawString("Y:", 10, 40, GuiColour.GRAY.toRGB());
		fontRenderer.drawString("Z:", 10, 70, GuiColour.GRAY.toRGB());
		
		tabs.drawForeground(this, fontRenderer, guiLeft, guiTop);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int clickTime) {
		super.mouseClicked(x, y, clickTime);
		tabs.mouseClick(x, y, guiLeft, guiTop);
		this.x.mouseClicked(x, y, clickTime);
		this.y.mouseClicked(x, y, clickTime);
		this.z.mouseClicked(x, y, clickTime);
	}
}
