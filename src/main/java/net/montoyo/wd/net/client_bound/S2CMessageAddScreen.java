package net.montoyo.wd.net.client_bound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.entity.ScreenBlockEntity;

public class S2CMessageAddScreen {
    public S2CMessageAddScreen(ScreenBlockEntity bev, Object data) {}
    public S2CMessageAddScreen(FriendlyByteBuf buf) {}
    public void write(FriendlyByteBuf buf) {}
    public static void handle(S2CMessageAddScreen msg, CustomPayloadEvent.Context ctx) {
        ctx.setPacketHandled(true);
    }
}
