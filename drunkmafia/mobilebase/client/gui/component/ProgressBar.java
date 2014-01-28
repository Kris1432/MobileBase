package drunkmafia.mobilebase.client.gui.component;

import net.minecraft.client.gui.inventory.GuiContainer;

public class ProgressBar {
	
	private int x, y, textX, textY, sizeX, sizeY;
	public boolean isHorizontal;
	public int progress = 0;
	
	public ProgressBar(int x, int y, int textX, int textY, int sizeX, int sizeY, boolean val) {
		this.x = x;
		this.y = y;
		this.textX = textX;
		this.textY = textY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		isHorizontal = val;
	}
	
	public void drawBar(GuiContainer gui, int left, int top){
		float filled = progress / 192F;
		int barHeight = (int)(filled * 27);
		if (barHeight > 0) {
			int srcX = sizeX;
			int srcY = 27 - barHeight;
			
			gui.drawTexturedModalRect(left + 157, top + 40 + 27 - barHeight, srcX, srcY, 7, barHeight);
		}
	}
}
