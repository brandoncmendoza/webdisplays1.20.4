package net.montoyo.wd.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.net.Packet;

public class ClientPacketHandler {
    public static void handleS2C(final Object msg, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                if (msg instanceof Packet p) {
                    p.handle(ctx);
                }
            });
        });
        ctx.setPacketHandled(true);
    }
}
