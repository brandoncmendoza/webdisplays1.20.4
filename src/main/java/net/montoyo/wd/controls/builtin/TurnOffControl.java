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

public class TurnOffControl extends ScreenControl {
    public static final ResourceLocation id = ResourceLocation.tryParse("webdisplays:deactivate");
    public static final TurnOffControl INSTANCE = new TurnOffControl();

    public TurnOffControl() {
        super(id);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    public TurnOffControl(FriendlyByteBuf buf) {
        super(id);
    }

    @Override
    public void handleServer(BlockPos pos, BlockSide side, ScreenBlockEntity tes,
                           CustomPayloadEvent.Context context,
                           Function<Integer, Boolean> permissionChecker) throws MissingPermissionException {
        checkPerms(ScreenRights.CHANGE_URL, permissionChecker, (ServerPlayer) context.getSender());
        // Clear the screen by setting URL to empty string
        try {
            tes.setScreenURL(side, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClient(BlockPos pos, BlockSide side, ScreenBlockEntity tes, CustomPayloadEvent.Context context) {
        // Client-side handling (if needed)
    }
}
