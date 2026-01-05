package net.montoyo.wd.controls.builtin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.controls.ScreenControl;
import net.montoyo.wd.core.MissingPermissionException;
import net.montoyo.wd.core.ScreenRights;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.utilities.serialization.NameUUIDPair;
import net.montoyo.wd.utilities.data.BlockSide;

import java.util.function.Function;

public class ModifyFriendListControl extends ScreenControl {
    public static final ResourceLocation id = ResourceLocation.tryParse("webdisplays:mod_friend_list");

    private NameUUIDPair player;
    private boolean add;

    public ModifyFriendListControl() {
        super(id);
    }

    public ModifyFriendListControl(NameUUIDPair player, boolean add) {
        super(id);
        this.player = player;
        this.add = add;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        player.writeTo(buf);
        buf.writeBoolean(add);
    }

    public ModifyFriendListControl(FriendlyByteBuf buf) {
        super(id);
        player = new NameUUIDPair(buf);
        add = buf.readBoolean();
    }

    @Override
    public void handleServer(BlockPos pos, BlockSide side, ScreenBlockEntity tes,
                           CustomPayloadEvent.Context context,
                           Function<Integer, Boolean> permissionChecker) throws MissingPermissionException {
        checkPerms(ScreenRights.MANAGE, permissionChecker, (ServerPlayer) context.getSender());
        if (add) tes.addFriend((ServerPlayer) context.getSender(), side, player);
        else tes.removeFriend((ServerPlayer) context.getSender(), side, player);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClient(BlockPos pos, BlockSide side, ScreenBlockEntity tes, CustomPayloadEvent.Context context) {
        // Client-side handling (if needed)
    }
}
