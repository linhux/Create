package com.simibubi.create.modules.contraptions.components.contraptions.seat;

import java.util.function.Supplier;

import com.simibubi.create.foundation.packet.SimplePacketBase;
import com.simibubi.create.modules.contraptions.components.contraptions.ContraptionEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PlayerMountSeatPacket extends SimplePacketBase {

	private int entityId;
	private int seatId;

	public PlayerMountSeatPacket(int entityId, int seatId) {
		this.entityId = entityId;
		this.seatId = seatId;
	}

	public PlayerMountSeatPacket(PacketBuffer buffer) {
		entityId = buffer.readInt();
		seatId = buffer.readInt();
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeInt(entityId);
		buffer.writeInt(seatId);
	}

	@Override
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayerEntity sender = context.get().getSender();
			Entity entity = sender.world.getEntityByID(entityId);
			if (entity instanceof ContraptionEntity) {
				ContraptionEntity contraptionEntity = (ContraptionEntity) entity;
				contraptionEntity.occupySeat(sender, seatId);
			}
		});
		context.get().setPacketHandled(true);
	}

}
