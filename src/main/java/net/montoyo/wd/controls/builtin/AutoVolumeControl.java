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
import net.montoyo.wd.utilities.data.BlockSide;

import java.util.function.Function;

public class AutoVolumeControl extends ScreenControl {
    public static final ResourceLocation id = ResourceLocation.tryParse("webdisplays:auto_volume");

    private boolean autoVolume;

    public AutoVolumeControl() {
        super(id);
    }

    public AutoVolumeControl(boolean av) {
        super(id);
        autoVolume = av;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(autoVolume);
    }

    public AutoVolumeControl(FriendlyByteBuf buf) {
        super(id);
        autoVolume = buf.readBoolean();
    }

    @Override
    public void handleServer(BlockPos pos, BlockSide side, ScreenBlockEntity tes,
                           CustomPayloadEvent.Context context,
                           Function<Integer, Boolean> permissionChecker) throws MissingPermissionException {
        checkPerms(ScreenRights.CHANGE_URL, permissionChecker, (ServerPlayer) context.getSender());
        tes.setAutoVolume(side, autoVolume);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClient(BlockPos pos, BlockSide side, ScreenBlockEntity tes, CustomPayloadEvent.Context context) {
        // Client-side handling (if needed)
    }
}
