package drunkmafia.mobilebase.client.gui;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import drunkmafia.mobilebase.client.gui.component.HoveringButton;
import drunkmafia.mobilebase.client.gui.container.TentBuilderContainer;
import drunkmafia.mobilebase.item.ItemBlueprint;
import drunkmafia.mobilebase.item.ModItems;
import drunkmafia.mobilebase.lib.ModInfo;
import drunkmafia.mobilebase.network.PacketHandler;
import drunkmafia.mobilebase.tileentity.TentBuilderTile;

public class TentBuilderGui extends GuiContainer{

	private TentBuilderTile tile;
	private HoveringButton[] slots = {
			new HoveringButton(0, 13, 18, 18, 18, 0, 0, "Blueprint Slot"),
			new HoveringButton(1, 13, 42, 18, 18, 0, 0, "Wool Slot "),
			new HoveringButton(2, 13, 66, 18, 18, 0, 0, "Fence Slot"),
			new HoveringButton(3, 13, 89, 18, 18, 0, 0, "Ender Pearl Slot")
	};
	private EntityPlayer player;
	private ItemStack tent;
	private int id = -1;
	private String text;
	
	public TentBuilderGui(TentBuilderTile tile, EntityPlayer player) {
		super(new TentBuilderContainer(tile, player));
		
		this.tile = tile;
		this.player = player;
		
		xSize = 176;
		ySize = 218;
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		updateSlots(1, tile.woolAmount, tile.deltaWool, tile.woolFinished);
		updateSlots(2, tile.fenceAmount, tile.deltaFence, tile.fenceFinished);
		updateSlots(3, tile.enderAmount, tile.deltaEnder, tile.enderFinished);

		int max = tile.enderAmount + tile.fenceAmount + tile.woolAmount;
		if(max != 0){
			int current = tile.deltaEnder + tile.deltaFence + tile.deltaWool;
			text = tile.assmebleTent ? "Progress: " + (current * 100 / max) + "/100" : "";
		}else
			text = "Idle";
		
		ItemStack blueprint = tile.getStackInSlot(0);
		
		if(blueprint != null && id != blueprint.itemID && blueprint.getItem() instanceof ItemBlueprint && blueprint.getTagCompound() != null && blueprint.getTagCompound().hasKey("tentY")){
			tent = new ItemStack(ModItems.tent);
			tent.setTagCompound(blueprint.getTagCompound());
		}else if(blueprint == null)
			tent = null;
		
		if(tile.getStackInSlot(1) != null && tent != null)
			tent.setItemDamage(tile.getStackInSlot(1).getItemDamage());
	}
	
	public void updateSlots(int slot, int maxAmount, int delta, boolean isFinished){
		slots[slot].required = maxAmount;
		slots[slot].done = delta;
		slots[slot].isFinished = isFinished;
	}
	
	private ResourceLocation gui = new ResourceLocation(ModInfo.MODID, "textures/gui/TentBuilder.png");
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1, 1, 1, 1);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		renderTent(guiLeft + 105, guiTop + 90, 100, partialTickTime);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		GuiButton build = new GuiButton(1, guiLeft + 5, guiTop + 110, 40, 20, "Build");
		buttonList.add(build);
		
		int max = tile.enderAmount + tile.fenceAmount + tile.woolAmount;
		if(max != 0){
			int current = (tile.deltaEnder + tile.deltaFence + tile.deltaWool) / max;
			text = tile.assmebleTent ? "Progress: " + current + "/100" : "";
		}else
			text = "Idle";
	}
	
	
	
	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if(button.id == 1)
			PacketHandler.sendBuildPacket(tile.xCoord, tile.yCoord, tile.zCoord);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRenderer.drawString("Tent Builder", 4, 4, GuiColour.GRAY.toRGB());
		
		for(HoveringButton rect : slots){
			if(rect.isHovering(x, y, guiLeft, guiTop)){
				ArrayList<String> list = new ArrayList<String>();
				if(rect.getId() == 0 || !tile.assmebleTent && tile.tent == null)
					list.add(rect.getText());
				else if((tile.assmebleTent || tile.tent != null) && !rect.isFinished)
					list.add(rect.getFullText());
				else if(tile.assmebleTent && rect.isFinished)
					list.add(rect.getText() + " Finished");
				drawHoveringText(list, x - guiLeft, y - guiTop, fontRenderer);
			}
		}
		
		fontRenderer.drawString(text, 60, 120, GuiColour.GRAY.toRGB());
	}
	
	private float rotation = 0;
		
	public void renderTent(float x, float y, float z, float tick) { 
		if(tent != null){
			GL11.glPushMatrix();
			GL11.glTranslatef(x, y, z);
			GL11.glScalef(35F, 35F, 35F);
			GL11.glRotatef(180, 0, 0, 1);
			GL11.glRotatef(rotation, 0, 1, 0);
	        RenderManager.instance.itemRenderer.renderItem(null, tent, (int) tick);
	        GL11.glPopMatrix();
	        
	        rotation += 0.3;
	        if(rotation == 360)
	        	rotation = 0;
		}
	}
}
