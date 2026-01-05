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
import net.montoyo.wd.utilities.math.Vector3i;

import java.util.function.Function;

public class SetURLControl extends ScreenControl {
    public static final ResourceLocation id = ResourceLocation.tryParse("webdisplays:set_url");

    private String url;
    private Vector3i vec;

    public SetURLControl() {
        super(id);
    }

    public SetURLControl(String u, Vector3i v) {
        super(id);
        url = u;
        vec = v;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(url);
        vec.writeTo(buf);
    }

    public SetURLControl(FriendlyByteBuf buf) {
        super(id);
        url = buf.readUtf();
        vec = new Vector3i(buf);
    }

    @Override
    public void handleServer(BlockPos pos, BlockSide side, ScreenBlockEntity tes,
                           CustomPayloadEvent.Context context,
                           Function<Integer, Boolean> permissionChecker) throws MissingPermissionException {
        checkPerms(ScreenRights.CHANGE_URL, permissionChecker, (ServerPlayer) context.getSender());
        try { tes.setScreenURL(side, url); } catch(Exception e) { e.printStackTrace(); }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClient(BlockPos pos, BlockSide side, ScreenBlockEntity tes, CustomPayloadEvent.Context context) {
        // Client-side handling (if needed)
    }
}
