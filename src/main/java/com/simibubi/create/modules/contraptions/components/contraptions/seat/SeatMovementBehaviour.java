package com.simibubi.create.modules.contraptions.components.contraptions.seat;

import java.util.List;

import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.modules.contraptions.components.contraptions.ContraptionEntity;
import com.simibubi.create.modules.contraptions.components.contraptions.MovementBehaviour;
import com.simibubi.create.modules.contraptions.components.contraptions.MovementContext;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class SeatMovementBehaviour extends MovementBehaviour {

	@Override
	public void visitNewPosition(MovementContext context, BlockPos pos) {
		super.visitNewPosition(context, pos);
	}
	
	@Override
	public void tick(MovementContext context) {
		World world = context.world;
		super.tick(context);
		ContraptionEntity contraptionEntity = context.entity;
		int seatID = context.data.getInt("SeatId");
		Vec3d vec = context.position;

		List<Entity> passengers = contraptionEntity.getPassengers();
		List<Integer> seatMapping = contraptionEntity.seatMapping;

		for (int i = 0; i < seatMapping.size(); i++) {
			int occupiedSeat = seatMapping.get(i);
			if (occupiedSeat != seatID)
				continue;

			if (passengers.size() > i) {
				Entity passenger = passengers.get(i);

				if (!world.isRemote && context.motion.length() > 1.5f) {
					passenger.stopRiding();
					return;
				}

				float x = (float) vec.x;
				float y = (float) (vec.y + passenger.getYOffset());
				float z = (float) vec.z;
				if (world.isRemote && passenger instanceof LivingEntity) {
					LivingEntity livingEntity = (LivingEntity) passenger;
					DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
						float rotationYaw = livingEntity instanceof PlayerEntity ? livingEntity.rotationYaw
								: ContraptionEntity.yawFromVector(context.motion);
						float rotationPitch = livingEntity instanceof PlayerEntity ? livingEntity.rotationPitch
								: ContraptionEntity.pitchFromVector(context.motion) - 90;
						livingEntity.setPositionAndRotationDirect(x, y, z, rotationYaw, rotationPitch, 1, false);
					});
					passenger.setMotion(context.motion);
					return;
				}
				passenger.setMotion(context.motion);
				passenger.setPosition(x, y, z);
			}
			return;
		}

		if (context.motion.length() > 2)
			return;

		for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(contraptionEntity,
				new AxisAlignedBB(BlockPos.ZERO).offset(vec.subtract(VecHelper.getCenterOf(BlockPos.ZERO)))
						.grow(1 / 4f))) {
			if (!EntityPredicates.pushableBy(contraptionEntity).test(entity))
				return;
			if (entity.isPassenger())
				return;
			if (entity.isSneaking())
				return;
			if (entity instanceof ItemEntity)
				return;
			if (entity instanceof IronGolemEntity)
				return;
			if (entity instanceof AbstractMinecartEntity)
				return;

			if (entity instanceof PlayerEntity) {
				if (world.isRemote)
					AllPackets.channel.sendToServer(new PlayerMountSeatPacket(contraptionEntity.getEntityId(), seatID));
				return;
			}

			if (world.isRemote)
				return;

			contraptionEntity.occupySeat(entity, seatID);
		}
	}

}
