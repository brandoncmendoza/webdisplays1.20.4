package net.montoyo.wd.net;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraftforge.network.PacketDistributor;

public class PacketSender {
    public static void sendToServer(Object packet) {
        WDNetworkRegistry.INSTANCE.send(packet, PacketDistributor.SERVER.noArg());
    }

    public static void sendToPlayer(Object packet, ServerPlayer player) {
        WDNetworkRegistry.INSTANCE.send(packet, PacketDistributor.PLAYER.with(player));
    }

    public static void sendToNear(Object packet, ServerLevel level, BlockPos pos, double range) {
        WDNetworkRegistry.INSTANCE.send(packet, PacketDistributor.NEAR.with(
            new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), range, level.dimension())
        ));
    }
}
