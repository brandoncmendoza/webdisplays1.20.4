package net.montoyo.wd.net.client_bound;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;
import net.montoyo.wd.client.ClientProxy;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.WebDisplays;
import net.montoyo.wd.data.GuiData;
import net.montoyo.wd.utilities.Log;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;

public class S2CMessageOpenGui {
    private GuiData guiData;

    public S2CMessageOpenGui(GuiData data) {
        this.guiData = data;
    }

    public S2CMessageOpenGui(FriendlyByteBuf buf) {
        String className = buf.readUtf();
        try {
            guiData = (GuiData) Class.forName(className).getDeclaredConstructor().newInstance();
            guiData.deserialize(buf);
        } catch (Exception e) {
            Log.error("Critical error deserializing GuiData: %s", className);
            e.printStackTrace();
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(guiData.getClass().getName());
        guiData.serialize(buf);
    }

    public static void handle(S2CMessageOpenGui msg, CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                if (FMLEnvironment.dist == Dist.CLIENT) ((ClientProxy) WebDisplays.PROXY).displayGui(msg.guiData);
            }
        });
        context.setPacketHandled(true);
    }
}
