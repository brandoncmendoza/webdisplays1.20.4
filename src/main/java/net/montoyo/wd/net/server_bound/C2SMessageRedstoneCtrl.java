/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */
package net.montoyo.wd.net.server_bound;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.core.ScreenRights;
import net.montoyo.wd.entity.RedstoneControlBlockEntity;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.net.Packet;
import net.montoyo.wd.utilities.serialization.Util;
import net.montoyo.wd.utilities.math.Vector3i;

public class C2SMessageRedstoneCtrl implements Packet, Runnable {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("webdisplays", "redstone_ctrl");

    private Player player;
    private final Vector3i pos;
    private final String risingEdgeURL;
    private final String fallingEdgeURL;

    public C2SMessageRedstoneCtrl() {
        pos = null;
        risingEdgeURL = null;
        fallingEdgeURL = null;
    }

    public C2SMessageRedstoneCtrl(Vector3i p, String r, String f) {
        pos = p;
        risingEdgeURL = r;
        fallingEdgeURL = f;
    }

    public C2SMessageRedstoneCtrl(FriendlyByteBuf buf) {
        pos = new Vector3i(buf);
        risingEdgeURL = buf.readUtf();
        fallingEdgeURL = buf.readUtf();
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        pos.writeTo(buf);
        buf.writeUtf(risingEdgeURL);
        buf.writeUtf(fallingEdgeURL);
    }

    @Override
    public void run() {
        if (player == null) return;

        Level world = player.level();
        BlockPos blockPos = pos.toBlock();
        final double maxRange = player.getAttribute(ForgeMod.BLOCK_REACH.get()).getValue();

        if (player.distanceToSqr(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > maxRange * maxRange)
            return;

        BlockEntity te = world.getBlockEntity(blockPos);
        if (!(te instanceof RedstoneControlBlockEntity))
            return;

        RedstoneControlBlockEntity redCtrl = (RedstoneControlBlockEntity) te;
        if (!redCtrl.isScreenChunkLoaded()) {
            Util.toast(player, "chunkUnloaded");
            return;
        }

        ScreenBlockEntity tes = redCtrl.getConnectedScreen();
        if (tes == null)
            return;

        if ((tes.getScreen(redCtrl.getScreenSide()).rightsFor(player) & ScreenRights.CHANGE_URL) == 0)
            return;

        redCtrl.setURLs(risingEdgeURL, fallingEdgeURL);
    }

    @Override
    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if (context.isServerSide()) {
                player = context.getSender();
                if (player != null) {
                    new Thread(this, "RedstoneCtrl-" + player.getName().getString()).start();
                }
            }
        });
        context.setPacketHandled(true);
    }
}
