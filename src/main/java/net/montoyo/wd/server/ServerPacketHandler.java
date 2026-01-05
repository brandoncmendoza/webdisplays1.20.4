package net.montoyo.wd.server;

import net.montoyo.wd.net.compat.NetworkContextCompat;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.net.server_bound.*;
import net.montoyo.wd.net.server_bound.*;
import net.montoyo.wd.net.server_bound.*;
import net.montoyo.wd.net.*;

public class ServerPacketHandler {
    public static <T> void handleC2S(T msg, CustomPayloadEvent.Context forgeCtx) {
        NetworkContextCompat ctx = new NetworkContextCompat(forgeCtx);
        ctx.enqueueWork(() -> {
            if (msg instanceof C2SMessageMinepadUrl) {
                ((C2SMessageMinepadUrl) msg).handle((C2SMessageMinepadUrl) msg, ctx.get());
            } else if (msg instanceof C2SMessageMiniservConnect) {
                ((C2SMessageMiniservConnect) msg).handle(ctx.get());
            } else if (msg instanceof C2SMessageACQuery) {
                ((C2SMessageACQuery) msg).handle(ctx.get());
            } else if (msg instanceof C2SMessageScreenCtrl) {
                ((C2SMessageScreenCtrl) msg).handle((C2SMessageScreenCtrl) msg, ctx.get());
            } else if (msg instanceof C2SMessageRedstoneCtrl) {
                ((C2SMessageRedstoneCtrl) msg).handle(ctx.get());
            }
        });
        forgeCtx.setPacketHandled(true);
    }
}
