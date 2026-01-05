package net.montoyo.wd.net.client_bound;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.WebDisplays;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.utilities.data.BlockSide;
import net.montoyo.wd.utilities.math.Vector2i;
import net.montoyo.wd.utilities.data.Rotation;
import net.montoyo.wd.utilities.serialization.NameUUIDPair;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;

public class S2CMessageScreenUpdate {
    public S2CMessageScreenUpdate() {}
    public S2CMessageScreenUpdate(BlockPos pos, BlockSide side) {}

    public S2CMessageScreenUpdate(FriendlyByteBuf buf) {
        buf.readBlockPos();
        buf.readByte();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(BlockPos.ZERO);
        buf.writeByte(0);
    }

    public static void handle(S2CMessageScreenUpdate msg, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                WebDisplays.PROXY.onScreenUpdate(msg);
            }
        });
        ctx.setPacketHandled(true);
    }

    public static S2CMessageScreenUpdate setURL(ScreenBlockEntity bev, BlockSide side, String url) { return new S2CMessageScreenUpdate(); }
    public static S2CMessageScreenUpdate setResolution(ScreenBlockEntity bev, BlockSide side, Vector2i res) { return new S2CMessageScreenUpdate(); }
    public static S2CMessageScreenUpdate click(ScreenBlockEntity bev, BlockSide side, Object type, Vector2i vec) { return new S2CMessageScreenUpdate(); }
    public static S2CMessageScreenUpdate type(ScreenBlockEntity bev, BlockSide side, String text) { return new S2CMessageScreenUpdate(); }
    public static S2CMessageScreenUpdate upgrade(ScreenBlockEntity bev, BlockSide side, boolean add, ItemStack is) { return new S2CMessageScreenUpdate(); }
    public static S2CMessageScreenUpdate turnOff(BlockPos pos, BlockSide side) { return new S2CMessageScreenUpdate(); }
    public static S2CMessageScreenUpdate owner(ScreenBlockEntity bev, BlockSide side, NameUUIDPair owner) { return new S2CMessageScreenUpdate(); }
    public static S2CMessageScreenUpdate rotation(ScreenBlockEntity bev, BlockSide side, Rotation rot) { return new S2CMessageScreenUpdate(); }
    public static S2CMessageScreenUpdate autoVolume(ScreenBlockEntity bev, BlockSide side, boolean av) { return new S2CMessageScreenUpdate(); }
}
