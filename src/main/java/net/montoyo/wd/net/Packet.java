package net.montoyo.wd.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.network.CustomPayloadEvent;

public interface Packet {

    ResourceLocation id();

    void write(FriendlyByteBuf buf);

    void handle(CustomPayloadEvent.Context context);
}
