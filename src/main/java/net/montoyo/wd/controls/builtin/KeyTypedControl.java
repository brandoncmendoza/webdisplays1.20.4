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

import java.util.function.Function;

public class KeyTypedControl extends ScreenControl {
    public static final ResourceLocation id = ResourceLocation.tryParse("webdisplays:type");

    private String data;
    private BlockPos kbPos;

    public KeyTypedControl() {
        super(id);
    }

    public KeyTypedControl(String d, BlockPos pos) {
        super(id);
        data = d;
        kbPos = pos;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(data);
        buf.writeBlockPos(kbPos);
    }

    public KeyTypedControl(FriendlyByteBuf buf) {
        super(id);
        data = buf.readUtf();
        kbPos = buf.readBlockPos();
    }

    @Override
    public void handleServer(BlockPos pos, BlockSide side, ScreenBlockEntity tes,
                           CustomPayloadEvent.Context context,
                           Function<Integer, Boolean> permissionChecker) throws MissingPermissionException {
        tes.type(side, data, kbPos);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClient(BlockPos pos, BlockSide side, ScreenBlockEntity tes, CustomPayloadEvent.Context context) {
        // Client-side handling (if needed)
    }
}
