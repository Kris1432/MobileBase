package drunkmafia.mobilebase.client.gui.component;

import net.minecraft.client.gui.inventory.GuiContainer;
import drunkmafia.mobilebase.client.gui.TentBluePrinterGui;

public class TabButton extends RectangleButton{

	private boolean isSelected;
	
	public TabButton(int x, int y, int sizeX, int sizeY, int textX, int textY) {
		super(x, y, sizeX, sizeY, textX, textY);
		isSelected = false;
	}
	
	public TabButton(int sizeX, int sizeY, int textX, int textY){
		super(0, 0, sizeX, sizeY, textX, textY);
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	@Override
	public void mouseClick() {
		isSelected = !isSelected;
	}
	
	@Override
	public void drawRect(GuiContainer gui, int left, int top, int mouseX, int mouseY) {
		if(isSelected){
			System.out.println("Drawing Button: Selected");
			gui.drawTexturedModalRect(left + x, top + y, textX + 74, textY, sizeX, sizeY);
		}else{
			if(isHovering(mouseX, mouseY, left, top)){
				System.out.println("Drawing Button: Hovering");
				gui.drawTexturedModalRect(left + x, top + y, textX + 37, textY, sizeX, sizeY);
			}else{
				System.out.println("Drawing Button: Selected");
				gui.drawTexturedModalRect(left + x, top + y, textX, textY, sizeX, sizeY);
			}
		}
	}
	
}
