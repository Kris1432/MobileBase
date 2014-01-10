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
		if(!menu.isEmpty()){
			activeButton = menu.get(0);
			activeButton.isSelected = true;
		}
	}
	
	public void setupTabBar(){
		int i = 0;
		for(TabButton button : menu){
			button.setX(menuX + (button.sizeX * i));
			button.setY(menuY);
			i++;
		}
	}
	
	public void drawTabBarBackground(GuiContainer gui, int left, int top, int mouseX, int mouseY){
		for(TabButton button : menu){
			button.drawRect(gui, left, top, mouseX, mouseY);
		}
	}
	
	public void drawTabBarForeground(FontRenderer font, int left, int top){
		for(TabButton button : menu){
			button.drawString(font, left, top);
		}
	}
	
	public void drawBackground(GuiContainer gui, int left, int top, int mouseX, int mouseY, float partialTickTime){
		drawTabBarBackground(gui, left, top, mouseX, mouseY);
		if(activeButton != null)
			pages.get(activeButton).drawBackground(gui, pageX, pageY, partialTickTime);
	}
	
	public void drawForeground(GuiContainer gui, FontRenderer font, int left, int top){
		if(activeButton != null)
			pages.get(activeButton).drawForeGround(gui, font, pageX, pageY);
		drawTabBarForeground(font, left, top);
	}
	
	public void mouseClick(int x, int y, int left, int top){
		for(TabButton button : menu){
			if(button.isHovering(x, y, left, top) && button != activeButton){
				button.isSelected = true;
				activeButton.isSelected = false;
				activeButton = button;
				break;
			}
		}
	}
}
