/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */
package net.montoyo.wd.net.server_bound;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.net.Packet;
import net.montoyo.wd.net.PacketSender;
import net.montoyo.wd.net.client_bound.S2CMessageACResult;
import net.montoyo.wd.utilities.serialization.NameUUIDPair;

import java.util.Arrays;
import java.util.List;

public class C2SMessageACQuery implements Packet, Runnable {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("webdisplays", "ac_query");

    private ServerPlayer player;
    private final String beginning;
    private final boolean matchExact;

    public C2SMessageACQuery(String beg, boolean exact) {
        beginning = beg;
        matchExact = exact;
    }

    public C2SMessageACQuery(FriendlyByteBuf buf) {
        beginning = buf.readUtf();
        matchExact = buf.readBoolean();
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(beginning);
        buf.writeBoolean(matchExact);
    }

    @Override
    public void run() {
        if (player == null) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        List<ServerPlayer> players = server.getPlayerList().getPlayers();

        NameUUIDPair[] result;
        if (matchExact) {
            result = players.stream()
                    .map(ServerPlayer::getGameProfile)
                    .filter(gp -> gp.getName().equalsIgnoreCase(beginning))
                    .map(NameUUIDPair::new)
                    .toArray(NameUUIDPair[]::new);
        } else {
            final String lBeg = beginning.toLowerCase();
            result = players.stream()
                    .map(ServerPlayer::getGameProfile)
                    .filter(gp -> gp.getName().toLowerCase().startsWith(lBeg))
                    .map(NameUUIDPair::new)
                    .toArray(NameUUIDPair[]::new);
        }

        PacketSender.sendToPlayer(new S2CMessageACResult("", result), player);
    }

    @Override
    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if (context.isServerSide()) {
                player = (ServerPlayer) context.getSender();
                if (player != null) {
                    new Thread(this, "AC-Query-" + player.getName().getString()).start();
                }
            }
        });
        context.setPacketHandled(true);
    }
}
