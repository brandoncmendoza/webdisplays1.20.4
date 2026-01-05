package net.montoyo.wd.controls.builtin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.controls.ScreenControl;
import net.montoyo.wd.core.MissingPermissionException;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.utilities.data.BlockSide;
import net.montoyo.wd.utilities.math.Vector2i;

import java.util.function.Function;

public class LaserControl extends ScreenControl {
    public static final ResourceLocation id = ResourceLocation.tryParse("webdisplays:laser");

    public enum Action {
        MOVE,
        UP,
        DOWN
    }

    private Action action;
    private Vector2i coord;
    private int button;

    public LaserControl() {
        super(id);
    }

    public LaserControl(Action a, Vector2i c, int b) {
        super(id);
        action = a;
        coord = c;
        button = b;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeByte(action.ordinal());
        if (coord == null)
            buf.writeBoolean(false);
        else {
            buf.writeBoolean(true);
            coord.writeTo(buf);
        }
        buf.writeInt(button);
    }

    public LaserControl(FriendlyByteBuf buf) {
        super(id);
        action = Action.values()[buf.readByte()];
        coord = buf.readBoolean() ? new Vector2i(buf) : null;
        button = buf.readInt();
    }

    @Override
    public void handleServer(BlockPos pos, BlockSide side, ScreenBlockEntity tes,
                           CustomPayloadEvent.Context context,
                           Function<Integer, Boolean> permissionChecker) throws MissingPermissionException {
        // Laser doesn't need permission check
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClient(BlockPos pos, BlockSide side, ScreenBlockEntity tes, CustomPayloadEvent.Context context) {
        // Client-side handling (if needed)
    }
}
