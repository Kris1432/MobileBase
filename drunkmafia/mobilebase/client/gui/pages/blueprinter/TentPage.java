package drunkmafia.mobilebase.client.gui.pages.blueprinter;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import drunkmafia.mobilebase.client.gui.GuiColour;
import drunkmafia.mobilebase.client.gui.component.Page;

public class TentPage extends Page {
	@Override
	public void drawBackground(GuiContainer gui, int x, int y, float partialTickTime) {
		
	}
	
	@Override
	public void drawForeGround(GuiContainer gui, FontRenderer font, int x, int y) {
		gui.drawString(font, "3D View:", x, y + 2, GuiColour.WHITE.toRGB());
	}
}
