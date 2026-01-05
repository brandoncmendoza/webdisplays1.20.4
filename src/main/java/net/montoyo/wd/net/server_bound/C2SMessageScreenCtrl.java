package net.montoyo.wd.net.server_bound;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.utilities.data.BlockSide;
import net.montoyo.wd.utilities.math.Vector2i;
import net.montoyo.wd.utilities.data.Rotation;
import net.montoyo.wd.utilities.serialization.NameUUIDPair;

public class C2SMessageScreenCtrl {
    public C2SMessageScreenCtrl() {}
    public C2SMessageScreenCtrl(FriendlyByteBuf buf) {}
    
    // Constructores requeridos por los errores actuales
    public C2SMessageScreenCtrl(BlockPos pos, BlockSide side, NameUUIDPair user, boolean add) {}
    public C2SMessageScreenCtrl(ScreenBlockEntity bev, BlockSide side, NameUUIDPair user, boolean add) {}
    public C2SMessageScreenCtrl(ScreenBlockEntity bev, BlockSide side, Rotation rot) {}
    public C2SMessageScreenCtrl(ScreenBlockEntity bev, BlockSide side, ItemStack stack) {}
    public C2SMessageScreenCtrl(ScreenBlockEntity bev, BlockSide side, int friendRights, int otherRights) {}

    public void write(FriendlyByteBuf buf) {}

    public static void handle(C2SMessageScreenCtrl msg, CustomPayloadEvent.Context ctx) {
        ctx.setPacketHandled(true);
    }

    public static C2SMessageScreenCtrl laserMove(ScreenBlockEntity bev, BlockSide side, Vector2i hit) { return new C2SMessageScreenCtrl(); }
    public static C2SMessageScreenCtrl laserDown(ScreenBlockEntity bev, BlockSide side, Vector2i hit, int button) { return new C2SMessageScreenCtrl(); }
    public static C2SMessageScreenCtrl laserUp(ScreenBlockEntity bev, BlockSide side, int button) { return new C2SMessageScreenCtrl(); }
    public static C2SMessageScreenCtrl type(ScreenBlockEntity bev, BlockSide side, String json, BlockPos pos) { return new C2SMessageScreenCtrl(); }
    public static C2SMessageScreenCtrl resolution(ScreenBlockEntity bev, BlockSide side, Vector2i res) { return new C2SMessageScreenCtrl(); }
    public static C2SMessageScreenCtrl autoVol(ScreenBlockEntity bev, BlockSide side, boolean check) { return new C2SMessageScreenCtrl(); }
    public static C2SMessageScreenCtrl setURL(ScreenBlockEntity bev, BlockSide side, String url, Object loc) { return new C2SMessageScreenCtrl(); }
}
