package drunkmafia.mobilebase.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import drunkmafia.mobilebase.client.gui.component.Page;
import drunkmafia.mobilebase.client.gui.component.TabButton;
import drunkmafia.mobilebase.client.gui.component.TabManager;
import drunkmafia.mobilebase.client.gui.container.TentBluePrinterContainer;
import drunkmafia.mobilebase.client.gui.pages.blueprinter.BluePrintPage;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.tileentity.TentBluePrinterTile;

public class TentBluePrinterGui extends GuiContainer{

	private TentBluePrinterTile tile;
	
	//Tabs Varibles
	private TabManager tabs;
	private Page bluePrintPage = new BluePrintPage();
	private TabButton bluePrintButton;
	
	public TentBluePrinterGui(TentBluePrinterTile tile, EntityPlayer player) {
		super(new TentBluePrinterContainer(tile, player));
		
		this.tile = tile;
		
		xSize = 176;
		ySize = 218; 
		bluePrintButton = new TabButton(37, 14, 0, ySize);
		
		tabs = new TabManager(50, 13, 50, 29);
		tabs.addTab(bluePrintButton, bluePrintPage);
	}
	
	private ResourceLocation gui = new ResourceLocation(ModInfo.MODID, "textures/gui/TentBluePrinter.png");

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1, 1, 1, 1);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);	
		tabs.drawBackground(this, guiLeft, guiTop, x, y, partialTickTime);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		tabs.drawForeground(this, fontRenderer, guiLeft, guiTop);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int clickTime) {
		super.mouseClicked(x, y, clickTime);
		tabs.mouseClick(x, y);
	}
}
