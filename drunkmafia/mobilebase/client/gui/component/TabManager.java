package drunkmafia.mobilebase.client.gui.component;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;

public class TabManager {
	
	private int pageX, pageY, menuX, menuY;
	private HashMap<TabButton, Page> pages;
	private ArrayList<TabButton> menu = new ArrayList<TabButton>(); 
	
	private TabButton activeButton;
	
	public TabManager(int menuX, int menuY, int pageX, int pageY){
		this.menuX = menuX;
		this.menuY = menuY;
		this.pageX = pageX;
		this.pageY = pageY;
		
		pages = new HashMap<TabButton, Page>();
		menu  = new ArrayList<TabButton>();
		
		if(!menu.isEmpty())
			activeButton = menu.get(0);
	}
	
	public void addTab(TabButton button, Page page){
		pages.put(button, page);
		menu.add(button);
		setupTabBar();
		if(!menu.isEmpty())
			activeButton = menu.get(0);
	}
	
	public void setupTabBar(){
		int i = 0;
		for(TabButton button : menu){
			button.setX(menuX + (button.sizeX * i));
			button.setY(menuY);
			i++;
		}
	}
	
	public void drawTabBar(GuiContainer gui, int left, int top, int mouseX, int mouseY){
		for(TabButton button : menu){
			button.drawRect(gui, left, top, mouseX, mouseY);
		}
	}
	
	public void drawBackground(GuiContainer gui, int left, int top, int mouseX, int mouseY, float partialTickTime){
		drawTabBar(gui, left, top, mouseX, mouseY);
		if(activeButton != null)
			pages.get(activeButton).drawBackground(gui, pageX, pageY, partialTickTime);
	}
	
	public void drawForeground(GuiContainer gui, FontRenderer font, int left, int top){
		if(activeButton != null)
			pages.get(activeButton).drawForeGround(gui, font, pageX, pageY);
	}
	
	public void mouseClick(int x, int y){
		
	}
}
