package net.montoyo.wd.data;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.montoyo.wd.client.gui.GuiServer;
import net.montoyo.wd.net.PacketSender;
import net.montoyo.wd.net.client_bound.S2CMessageOpenGui;
import net.montoyo.wd.utilities.math.Vector3i;
import net.montoyo.wd.utilities.serialization.NameUUIDPair;
import net.montoyo.wd.miniserv.server.Server;
import net.montoyo.wd.net.BufferUtils;

public class ServerData extends GuiData {
    private Vector3i pos;
    private NameUUIDPair owner;
    private byte[] key;

    public ServerData() {}

    public ServerData(BlockPos bp, NameUUIDPair owner) {
        this.pos = new Vector3i(bp);
        this.owner = owner;
        if (Server.getInstance() != null) {
            this.key = Server.getInstance().getKey();
        }
    }

    @Override
    public String getName() { return "Server"; }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Screen createGui(Screen old, Level world) {
        return new GuiServer(pos, owner, key);
    }

    @Override
    public void serialize(FriendlyByteBuf buf) {
        BufferUtils.writeVec3i(buf, pos);
        owner.writeTo(buf);
        if (key != null && key.length > 0) {
            buf.writeBoolean(true);
            buf.writeByteArray(key);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public void deserialize(FriendlyByteBuf buf) {
        pos = BufferUtils.readVec3i(buf);
        owner = new NameUUIDPair(buf);
        if (buf.readBoolean()) {
            key = buf.readByteArray();
        } else {
            key = null;
        }
    }

    public void sendTo(ServerPlayer player) {
        PacketSender.sendToPlayer(new S2CMessageOpenGui(this), player);
    }
}
