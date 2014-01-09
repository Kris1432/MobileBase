package drunkmafia.mobilebase.client.gui.component;

import net.minecraft.client.gui.inventory.GuiContainer;

public class RectangleButton {
	
	protected int x, y, sizeX, sizeY, textX, textY;
	
	public RectangleButton(int x, int y, int sizeX, int sizeY, int textX, int textY){
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.textX = textX;
		this.textY = textY;
	}
	
	public void mouseClick(){
		
	}
	
	public boolean isHovering(int mouseX, int mouseY, int left, int top){
		if(mouseX > (left + x) && mouseX < ((left + x) + sizeX) && mouseY > (top + y) && mouseY < ((top + y) + sizeY))
			return true;
		return false;
	}
	
	public void drawRect(GuiContainer gui, int left, int top, int mouseX, int mouseY){
		gui.drawTexturedModalRect(left + x, top + y, textX, textY, sizeX, sizeY);
	}
}
