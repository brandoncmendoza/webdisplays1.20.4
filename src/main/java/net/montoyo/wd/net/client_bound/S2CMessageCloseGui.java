package net.montoyo.wd.net.client_bound;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;
import net.montoyo.wd.client.ClientProxy;


import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.WebDisplays;
import net.montoyo.wd.utilities.data.BlockSide;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;
import java.util.Arrays;

public class S2CMessageCloseGui {
    private final BlockPos blockPos;
    private final BlockSide blockSide;

    public S2CMessageCloseGui(BlockPos bp) { this.blockPos = bp; this.blockSide = null; }
    public S2CMessageCloseGui(BlockPos bp, BlockSide side) { this.blockPos = bp; this.blockSide = side; }

    public S2CMessageCloseGui(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
        byte b = buf.readByte();
        this.blockSide = (b <= 0) ? null : BlockSide.values()[b - 1];
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeByte((blockSide == null) ? 0 : blockSide.ordinal() + 1);
    }

    public static void handle(S2CMessageCloseGui msg, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                if (msg.blockSide == null) {
                    Arrays.stream(BlockSide.values()).forEach(s -> { if (FMLEnvironment.dist == Dist.CLIENT) ((ClientProxy) WebDisplays.PROXY).closeGui(msg.blockPos, s); });
                } else {
                    if (FMLEnvironment.dist == Dist.CLIENT) ((ClientProxy) WebDisplays.PROXY).closeGui(msg.blockPos, msg.blockSide);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
