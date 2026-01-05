package net.montoyo.wd.net.server_bound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import java.util.UUID;

public class C2SMessageMinepadUrl {
    public String url;

    public C2SMessageMinepadUrl() {}
    public C2SMessageMinepadUrl(String url) { this.url = url; }
    public C2SMessageMinepadUrl(UUID id, String url) { this.url = url; }
    public C2SMessageMinepadUrl(FriendlyByteBuf buf) {
        this.url = buf.readUtf();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(url);
    }

    public static void handle(C2SMessageMinepadUrl msg, CustomPayloadEvent.Context ctx) {
        ctx.setPacketHandled(true);
    }
}
