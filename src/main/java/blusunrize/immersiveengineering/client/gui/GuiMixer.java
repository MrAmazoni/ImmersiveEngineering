package blusunrize.immersiveengineering.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMixer;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal.MultiblockProcess;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal.MultiblockProcessInMachine;
import blusunrize.immersiveengineering.common.gui.ContainerMixer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GuiMixer extends GuiContainer
{
	TileEntityMixer tile;
	public GuiMixer(InventoryPlayer inventoryPlayer, TileEntityMixer tile)
	{
		super(new ContainerMixer(inventoryPlayer, tile));
		this.tile=tile;
		this.ySize = 167;
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		ArrayList<String> tooltip = new ArrayList<String>();

		if(mx >= guiLeft+76&&mx <= guiLeft+134&&my >= guiTop+11&&my <= guiTop+58)
		{
			float capacity = tile.tank.getCapacity();
			int yy = guiTop+58;
			if(tile.tank.getFluidTypes()==0)
				tooltip.add(I18n.format("gui.immersiveengineering.empty"));
			else
				for(int i = tile.tank.getFluidTypes()-1; i >= 0; i--)
				{
					FluidStack fs = tile.tank.fluids.get(i);
					if(fs!=null&&fs.getFluid()!=null)
					{
						int fluidHeight = (int)(47*(fs.amount/capacity));
						yy -= fluidHeight;
						if(my >= yy&&my < yy+fluidHeight)
							ClientUtils.addFluidTooltip(fs, tooltip, (int)capacity);
					}
				}
		}
		if(mx > guiLeft+158&&mx < guiLeft+165&&my > guiTop+22&&my < guiTop+68)
			tooltip.add(tile.getEnergyStored(null)+"/"+tile.getMaxEnergyStored(null)+" RF");
		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRendererObj, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture("immersiveengineering:textures/gui/mixer.png");
		this.drawTexturedModalRect(guiLeft,guiTop, 0, 0, xSize, ySize);

		for(MultiblockProcess process : tile.processQueue)
			if(process instanceof MultiblockProcessInMachine)
			{
				float mod = 1-(process.processTick/(float)process.maxTicks);
				for(int slot : ((MultiblockProcessInMachine)process).getInputSlots())
				{
					int h = (int)Math.max(1, mod*16);
					this.drawTexturedModalRect(guiLeft+24+slot%2*21, guiTop+7+slot/2*18+(16-h), 176, 16-h, 2, h);
				}
			}

		int stored = (int)(46*(tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null)));
		ClientUtils.drawGradientRect(guiLeft+158,guiTop+22+(46-stored), guiLeft+165,guiTop+68, 0xffb51500, 0xff600b00);

		float capacity = tile.tank.getCapacity();
		int yy = guiTop+58;
		for(int i=tile.tank.getFluidTypes()-1; i>=0; i--)
		{
			FluidStack fs = tile.tank.fluids.get(i);
			if(fs!=null && fs.getFluid()!=null)
			{
				int fluidHeight = (int)(47*(fs.amount/capacity));
				yy -= fluidHeight;
				ClientUtils.drawRepeatedFluidSprite(fs, guiLeft+76,yy, 58,fluidHeight);
			}
		}
	}
}
