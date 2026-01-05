package net.montoyo.wd.net.client_bound;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;
import net.montoyo.wd.client.ClientProxy;

import net.montoyo.wd.utilities.serialization.NameUUIDPair;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.WebDisplays;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;

public class S2CMessageACResult {
    private final String query;
    private final NameUUIDPair[] result;

    public S2CMessageACResult(String q, NameUUIDPair[] res) {
        this.query = q;
        this.result = res;
    }

    public S2CMessageACResult(FriendlyByteBuf buf) {
        this.query = buf.readUtf(32767);
        int len = buf.readShort();
        this.result = new NameUUIDPair[len];
        for(int i = 0; i < len; i++)
            this.result[i] = new NameUUIDPair(buf);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(query);
        buf.writeShort(result.length);
        for(NameUUIDPair pair: result)
            pair.writeTo(buf);
    }

    public static void handle(S2CMessageACResult msg, CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                if (FMLEnvironment.dist == Dist.CLIENT) ((ClientProxy) WebDisplays.PROXY).onAutocompleteResult(msg.result);
            }
        });
        context.setPacketHandled(true);
    }
}
