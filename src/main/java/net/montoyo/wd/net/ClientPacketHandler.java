package net.montoyo.wd.net;

import net.minecraftforge.event.network.CustomPayloadEvent;
import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handlePacket(Object msg, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Aquí llamarás a tu lógica de cliente
            // Ejemplo: ClientNetFiller.handleSafe(msg, ctx);
        });
        ctx.setPacketHandled(true);
    }
}
