package net.montoyo.wd.net.compat;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class NetworkContextCompat {
    private final CustomPayloadEvent.Context context;

    public NetworkContextCompat(CustomPayloadEvent.Context context) {
        this.context = context;
    }

    public void enqueueWork(Runnable runnable) {
        context.enqueueWork(runnable);
    }

    public void setPacketHandled(boolean handled) {
        context.setPacketHandled(handled);
    }

    public ServerPlayer getSender() {
        return context.getSender();
    }

    public CustomPayloadEvent.Context get() {
        return context;
    }
}
