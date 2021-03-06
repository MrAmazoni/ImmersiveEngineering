package blusunrize.immersiveengineering.common.blocks.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.IPostBlock;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.BlockIETileProvider;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWallmount;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWoodenPost;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;

import java.util.ArrayList;
import java.util.List;

public class BlockMetalDecoration2 extends BlockIETileProvider<BlockTypes_MetalDecoration2> implements IPostBlock
{
	public BlockMetalDecoration2()
	{
		super("metalDecoration2", Material.IRON, PropertyEnum.create("type", BlockTypes_MetalDecoration2.class), ItemBlockIEBase.class, IEProperties.FACING_ALL,IEProperties.MULTIBLOCKSLAVE,IEProperties.INT_4, Properties.AnimationProperty, IOBJModelCallback.PROPERTY, IEProperties.CONNECTIONS);
		this.setHardness(3.0F);
		this.setResistance(15.0F);
		this.setAllNotNormalBlock();
		this.setMetaBlockLayer(BlockTypes_MetalDecoration2.RAZOR_WIRE.getMeta(), BlockRenderLayer.CUTOUT);
		lightOpacity = 0;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		if(this.getMetaFromState(state)==BlockTypes_MetalDecoration2.ALUMINUM_POST.getMeta()||this.getMetaFromState(state)==BlockTypes_MetalDecoration2.STEEL_POST.getMeta())
			return new ArrayList<>();
		return super.getDrops(world, pos, state, fortune);
	}
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileEntityWoodenPost)
		{
			if(!((TileEntityWoodenPost)tileEntity).isDummy() && !world.isRemote && world.getGameRules().getBoolean("doTileDrops") && !world.restoringBlockSnapshots)
				world.spawnEntityInWorld(new EntityItem(world, pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5, new ItemStack(this,1,this.getMetaFromState(state))));
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityWoodenPost)
		{
			return ((TileEntityWoodenPost)te).dummy==0?side==EnumFacing.DOWN: ((TileEntityWoodenPost)te).dummy==3?side==EnumFacing.UP: ((TileEntityWoodenPost)te).dummy>3?side.getAxis()==Axis.Y: side.getAxis()!=Axis.Y;
		}
		return super.isSideSolid(state, world, pos, side);
	}

	@Override
	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack)
	{
		if(stack.getItemDamage()== BlockTypes_MetalDecoration2.STEEL_POST.getMeta() || stack.getItemDamage()== BlockTypes_MetalDecoration2.ALUMINUM_POST.getMeta())
		{
			for(int hh=1; hh<=3; hh++)
				if(!world.getBlockState(pos.add(0,hh,0)).getBlock().isReplaceable(world, pos.add(0,hh,0)))
					return false;
		}
		return true;
	}

	@Override
	public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
	{
		return (world.getTileEntity(pos) instanceof TileEntityWoodenPost);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		switch(BlockTypes_MetalDecoration2.values()[meta])
		{
			case STEEL_POST:
				return new TileEntityWoodenPost();
			case STEEL_WALLMOUNT:
				return new TileEntityWallmount();
			case ALUMINUM_POST:
				return new TileEntityWoodenPost();
			case ALUMINUM_WALLMOUNT:
				return new TileEntityWallmount();
			case LANTERN:
				return new TileEntityLantern();
			case RAZOR_WIRE:
				return new TileEntityRazorWire();
		}
		return null;
	}

	@Override
	public boolean canConnectTransformer(IBlockAccess world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		BlockTypes_MetalDecoration2 type = state.getValue(property);
		boolean slave = state.getValue(IEProperties.MULTIBLOCKSLAVE);
		return slave&&(type==BlockTypes_MetalDecoration2.STEEL_POST||type==BlockTypes_MetalDecoration2.ALUMINUM_POST);
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean allowWirecutterHarvest(IBlockState state)
	{
		return getMetaFromState(state)==BlockTypes_MetalDecoration2.RAZOR_WIRE.getMeta();
	}
}