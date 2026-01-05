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

public class ClickControl extends ScreenControl {
    public static final ResourceLocation id = ResourceLocation.tryParse("webdisplays:click");

    public enum ControlType {
        MOVE,
        UP,
        DOWN,
        CLICK
    }

    private int x;
    private int y;
    private int btn;

    public ClickControl() {
        super(id);
    }

    public ClickControl(Vector2i vec, int btn) {
        super(id);
        x = vec.x;
        y = vec.y;
        this.btn = btn;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(btn);
    }

    public ClickControl(FriendlyByteBuf buf) {
        super(id);
        x = buf.readInt();
        y = buf.readInt();
        btn = buf.readInt();
    }

    @Override
    public void handleServer(BlockPos pos, BlockSide side, ScreenBlockEntity tes,
                           CustomPayloadEvent.Context context,
                           Function<Integer, Boolean> permissionChecker) throws MissingPermissionException {
        tes.click(side, new Vector2i(x, y));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClient(BlockPos pos, BlockSide side, ScreenBlockEntity tes, CustomPayloadEvent.Context context) {
        // Client-side handling (if needed)
    }
}
