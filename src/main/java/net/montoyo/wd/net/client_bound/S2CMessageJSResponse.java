package net.montoyo.wd.net.client_bound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.WebDisplays;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;

public class S2CMessageJSResponse {
    private final int queryID;
    private final String response;

    public S2CMessageJSResponse(int qid, String resp) {
        this.queryID = qid;
        this.response = resp;
    }

    public S2CMessageJSResponse(FriendlyByteBuf buf) {
        this.queryID = buf.readInt();
        this.response = buf.readUtf(32767);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(queryID);
        buf.writeUtf(response);
    }

    public static void handle(S2CMessageJSResponse msg, CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                WebDisplays.PROXY.onJSResponse(msg.queryID, msg.response);
            }
        });
        context.setPacketHandled(true);
    }
}
