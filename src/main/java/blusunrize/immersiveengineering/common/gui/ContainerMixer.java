package blusunrize.immersiveengineering.common.gui;

import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMixer;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal.MultiblockProcess;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal.MultiblockProcessInMachine;
import blusunrize.immersiveengineering.common.gui.IESlot.ICallbackContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMixer extends ContainerIEBase<TileEntityMixer> implements ICallbackContainer
{
	public ContainerMixer(InventoryPlayer inventoryPlayer, TileEntityMixer tile)
	{
		super(inventoryPlayer, tile);

		for(int i=0; i<8; i++)
			this.addSlotToContainer(new IESlot.ContainerCallback(this, this.inv, i, 7+(i%2)*21,7+(i/2)*18));
		slotCount=8;

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 86+i*18));
		for (int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 144));
	}

	@Override
	public boolean canInsert(ItemStack stack, int slotNumer, Slot slotObject)
	{
		for(MultiblockProcess<MixerRecipe> process : tile.processQueue)
			if(process instanceof MultiblockProcessInMachine)
				for(int s : ((MultiblockProcessInMachine)process).getInputSlots())
					if(s==slotNumer)
						return false;
		return true;
	}
	@Override
	public boolean canTake(ItemStack stack, int slotNumer, Slot slotObject)
	{
		for(MultiblockProcess<MixerRecipe> process : tile.processQueue)
			if(process instanceof MultiblockProcessInMachine)
				for(int s : ((MultiblockProcessInMachine)process).getInputSlots())
					if(s==slotNumer)
						return false;
		return true;
	}
}