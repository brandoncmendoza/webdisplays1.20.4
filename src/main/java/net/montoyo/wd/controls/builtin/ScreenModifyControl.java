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
import net.montoyo.wd.utilities.data.Rotation;
import net.montoyo.wd.utilities.math.Vector2i;

import java.util.function.Function;

public class ScreenModifyControl extends ScreenControl {
    public static final ResourceLocation id = ResourceLocation.tryParse("webdisplays:mod_screen");

    private Vector2i resolution;
    private Rotation rotation;

    public ScreenModifyControl() {
        super(id);
    }

    public ScreenModifyControl(Vector2i res) {
        super(id);
        resolution = res;
        rotation = null;
    }

    public ScreenModifyControl(Rotation rot) {
        super(id);
        rotation = rot;
        resolution = null;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        if (resolution == null) {
            buf.writeBoolean(true);
            buf.writeByte(rotation.ordinal());
        } else {
            buf.writeBoolean(false);
            resolution.writeTo(buf);
        }
    }

    public ScreenModifyControl(FriendlyByteBuf buf) {
        super(id);
        if (buf.readBoolean()) {
            rotation = Rotation.values()[buf.readByte()];
            resolution = null;
        } else {
            resolution = new Vector2i(buf);
            rotation = null;
        }
    }

    @Override
    public void handleServer(BlockPos pos, BlockSide side, ScreenBlockEntity tes,
                           CustomPayloadEvent.Context context,
                           Function<Integer, Boolean> permissionChecker) throws MissingPermissionException {
        checkPerms(ScreenRights.CHANGE_URL, permissionChecker, (ServerPlayer) context.getSender());
        if (resolution == null)
            tes.setRotation(side, rotation);
        else
            tes.setResolution(side, resolution);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClient(BlockPos pos, BlockSide side, ScreenBlockEntity tes, CustomPayloadEvent.Context context) {
        // Client-side handling (if needed)
    }
}
