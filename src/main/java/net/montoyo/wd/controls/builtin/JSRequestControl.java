package net.montoyo.wd.controls.builtin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.controls.ScreenControl;
import net.montoyo.wd.core.JSServerRequest;
import net.montoyo.wd.core.MissingPermissionException;
import net.montoyo.wd.core.ScreenRights;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.utilities.data.BlockSide;

import java.util.function.Function;

public class JSRequestControl extends ScreenControl {
    public static final ResourceLocation id = ResourceLocation.tryParse("webdisplays:js_req");

    private JSServerRequest req;
    private Object[] data;

    public JSRequestControl() {
        super(id);
    }

    public JSRequestControl(JSServerRequest r, Object... data) {
        super(id);
        this.req = r;
        this.data = data;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(req.ordinal());
        req.serialize(buf, data != null ? data : new Object[0]);
    }

    public JSRequestControl(FriendlyByteBuf buf) {
        super(id);
        int reqId = buf.readInt();
        req = JSServerRequest.fromID(reqId);
        if (req != null) {
            data = req.deserialize(buf);
        }
    }

    @Override
    public void handleServer(BlockPos pos, BlockSide side, ScreenBlockEntity tes,
                           CustomPayloadEvent.Context context,
                           Function<Integer, Boolean> permissionChecker) throws MissingPermissionException {
        checkPerms(ScreenRights.INTERACT, permissionChecker, (ServerPlayer) context.getSender());
        // TODO: Implement handleJSRequest in ScreenBlockEntity
        // tes.handleJSRequest(side, req, data);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClient(BlockPos pos, BlockSide side, ScreenBlockEntity tes, CustomPayloadEvent.Context context) {
        // Client-side handling (if needed)
    }
}
