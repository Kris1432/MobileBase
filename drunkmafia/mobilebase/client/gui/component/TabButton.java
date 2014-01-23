package drunkmafia.mobilebase.client.gui.component;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import drunkmafia.mobilebase.client.gui.GuiColour;

public class TabButton extends RectangleButton{

	public boolean isSelected, isHovering;
	private String text;
	
	public TabButton(int id, int x, int y, int sizeX, int sizeY, int textX, int textY, String text) {
		super(id, x, y, sizeX, sizeY, textX, textY);
		isSelected = false;
		this.text = text;
	}
	
	public TabButton(int id, int sizeX, int sizeY, int textX, int textY, String text){
		this(id, 0, 0, sizeX, sizeY, textX, textY, text);
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	@Override
	public void drawRect(GuiContainer gui, int left, int top, int mouseX, int mouseY) {
		if(isSelected)
			gui.drawTexturedModalRect(left + x, top + y, textX + 74, textY, sizeX, sizeY);
		else{
			if(isHovering(mouseX, mouseY, left, top)){
				gui.drawTexturedModalRect(left + x, top + y, textX + 37, textY, sizeX, sizeY);
				isHovering = true;
			}else{
				gui.drawTexturedModalRect(left + x, top + y, textX, textY, sizeX, sizeY);
				isHovering = false;
			}
		}
	}
	
	public void drawString(FontRenderer font, int left, int top){
		int drawX = x + 3;
		int drawY = y + 3;
		if(isSelected)
			font.drawString(text, drawX, drawY, GuiColour.WHITE.toRGB());
		else{
			if(isHovering)
				font.drawString(text, drawX, drawY, GuiColour.LIGHTGRAY.toRGB());
			else
				font.drawString(text, drawX, drawY, GuiColour.GRAY.toRGB());
		}
	}
}
