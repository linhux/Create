package com.simibubi.create.modules.contraptions.components.contraptions.seat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.simibubi.create.foundation.packet.SimplePacketBase;
import com.simibubi.create.modules.contraptions.components.contraptions.ContraptionEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SendSeatMappingPacket extends SimplePacketBase {

	private int entityId;
	private List<Integer> seatMapping;

	public SendSeatMappingPacket(int entityId, List<Integer> seatMapping) {
		this.entityId = entityId;
		this.seatMapping = seatMapping;
	}

	public SendSeatMappingPacket(PacketBuffer buffer) {
		entityId = buffer.readInt();
		int size = buffer.readInt();
		seatMapping = new ArrayList<>();
		for (int i = 0; i < size; i++)
			seatMapping.add(buffer.readInt());
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeInt(entityId);
		buffer.writeInt(seatMapping.size());
		for (Integer integer : seatMapping)
			buffer.writeInt(integer);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			Entity entity = Minecraft.getInstance().world.getEntityByID(entityId);
			if (entity instanceof ContraptionEntity) {
				ContraptionEntity contraptionEntity = (ContraptionEntity) entity;
				contraptionEntity.seatMapping = this.seatMapping;
			}
		});
		context.get().setPacketHandled(true);
	}

}
