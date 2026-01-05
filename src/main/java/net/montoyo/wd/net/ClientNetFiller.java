package net.montoyo.wd.net;

import net.minecraftforge.event.network.CustomPayloadEvent;

public class ClientNetFiller {
    public static void handleSafe(Object msg, CustomPayloadEvent.Context ctx) {
        // Esta llamada es segura porque ClientPacketHandler solo existe en el cliente
        // y esta clase solo se invoca si FMLEnvironment.dist == Dist.CLIENT
        net.montoyo.wd.client.ClientPacketHandler.handleS2C(msg, ctx);
    }
}
