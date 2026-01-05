package net.montoyo.wd.net.client_bound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.WebDisplays;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;

public class S2CMessageServerInfo {
    private final int port;

    public S2CMessageServerInfo(int port) {
        this.port = port;
    }

    public S2CMessageServerInfo(FriendlyByteBuf buf) {
        this.port = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(port);
    }

    public static void handle(S2CMessageServerInfo msg, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                WebDisplays.INSTANCE.miniservPort = msg.port;
            }
        });
        ctx.setPacketHandled(true);
    }
}
