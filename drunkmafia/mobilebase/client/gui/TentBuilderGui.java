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
			new HoveringButton(1, 13, 42, 18, 18, 0, 0, "Wool Slot - Required: "),
			new HoveringButton(2, 13, 66, 18, 18, 0, 0, "Fence Slot - Required: "),
			new HoveringButton(2, 13, 89, 18, 18, 0, 0, "Ender Pearl Slot - Required: ")
	};
	private EntityPlayer player;
	private ItemStack tent;
	
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
		slots[1].required = tile.woolAmount;
		slots[1].done = tile.deltaWool;
		slots[2].required = tile.fenceAmount;
		slots[2].done = tile.deltaFence;
		slots[3].required = tile.enderAmount;
		slots[3].done = tile.deltaEnder;
	}
	
	private ResourceLocation gui = new ResourceLocation(ModInfo.MODID, "textures/gui/TentBuilder.png");
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1, 1, 1, 1);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(tile.getStackInSlot(0) != null && tile.getStackInSlot(0).getItem() instanceof ItemBlueprint && tile.getStackInSlot(0).getTagCompound().hasKey("tentY")){
			renderTent(tile.getStackInSlot(0), guiLeft + 105, guiTop + 90, 100, partialTickTime);
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		GuiButton build = new GuiButton(1, guiLeft + 5, guiTop + 110, 40, 20, "Build");
		buttonList.add(build);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if(button.id == 1)
			PacketHandler.sendBuildPacket(tile.xCoord, tile.yCoord, tile.zCoord);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRenderer.drawString("Tent Builder", 1, 1, GuiColour.GRAY.toRGB());
		
		for(HoveringButton rect : slots){
			if(rect.isHovering(x, y, guiLeft, guiTop) && tile.getStackInSlot(rect.getId()) == null){
				ArrayList<String> list = new ArrayList<String>();
				if(rect.getId() == 0)
					list.add(rect.getText());
				else
					list.add(rect.getFullText());
				drawHoveringText(list, x - guiLeft, y - guiTop, fontRenderer);
			}
		}
	}
	
	private float rotation = 0;
		
	public void renderTent(ItemStack stack, float x, float y, float z, float tick) { 
		if(tile.tent != null){
			GL11.glPushMatrix();
			GL11.glTranslatef(x, y, z);
			GL11.glScalef(35F, 35F, 35F);
			GL11.glRotatef(180, 0, 0, 1);
			GL11.glRotatef(rotation, 0, 1, 0);
	        RenderManager.instance.itemRenderer.renderItem(null, tile.tent, (int) tick);
	        GL11.glPopMatrix();
	        
	        rotation += 0.3;
	        if(rotation == 360)
	        	rotation = 0;
		}
	}
}
