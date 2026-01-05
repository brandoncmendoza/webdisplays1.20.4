/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */
package net.montoyo.wd.net.server_bound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.miniserv.server.ClientManager;
import net.montoyo.wd.miniserv.server.Server;
import net.montoyo.wd.net.BufferUtils;
import net.montoyo.wd.net.Packet;

public class C2SMessageMiniservConnect implements Packet {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("webdisplays", "miniserv_connect");
    
    private final byte[] modulus;
    private final byte[] exponent;

    public C2SMessageMiniservConnect(byte[] mod, byte[] exp) {
        modulus = mod;
        exponent = exp;
    }

    public C2SMessageMiniservConnect(FriendlyByteBuf buf) {
        modulus = BufferUtils.readBytes(buf);
        exponent = BufferUtils.readBytes(buf);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        BufferUtils.writeBytes(buf, modulus);
        BufferUtils.writeBytes(buf, exponent);
    }

    @Override
    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if (context.isServerSide()) {
                ServerPlayer player = context.getSender() instanceof ServerPlayer ? (ServerPlayer) context.getSender() : null;
                if (player != null) {
                    try {
                        // La conexión ahora es atómica a través de S2CMessageOpenGui
                        System.out.println("[WebDisplays] Handshake de Miniserv recibido (La clave ya fue enviada con el GUI)");
                    } catch (Throwable err) {
                        err.printStackTrace();
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
