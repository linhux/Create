package com.simibubi.create.modules.contraptions.components.contraptions.seat;

import com.simibubi.create.modules.contraptions.components.contraptions.IPortableBlock;
import com.simibubi.create.modules.contraptions.components.contraptions.MovementBehaviour;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.BlockRenderLayer;

public class SeatBlock extends Block implements IPortableBlock {

	private static SeatMovementBehaviour MOVEMENT = new SeatMovementBehaviour();

	public SeatBlock() {
		super(Properties.from(Blocks.SPRUCE_PLANKS));
	}

	@Override
	public MovementBehaviour getMovementBehaviour() {
		return MOVEMENT;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

}
