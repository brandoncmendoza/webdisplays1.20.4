package net.montoyo.wd.controls.builtin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.montoyo.wd.controls.ScreenControl;
import net.montoyo.wd.core.IUpgrade;
import net.montoyo.wd.core.MissingPermissionException;
import net.montoyo.wd.core.ScreenRights;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.utilities.data.BlockSide;
import net.montoyo.wd.utilities.serialization.Util;

import java.util.function.Function;

public class ManageRightsAndUpdgradesControl extends ScreenControl {
    public static final ResourceLocation id = ResourceLocation.tryParse("webdisplays:mod_rights_upgrades");

    private int friendRights;
    private int otherRights;
    private ItemStack itemStack;

    public ManageRightsAndUpdgradesControl() {
        super(id);
    }

    public ManageRightsAndUpdgradesControl(int fr, int or) {
        super(id);
        friendRights = fr;
        otherRights = or;
        itemStack = null;
    }

    public ManageRightsAndUpdgradesControl(ItemStack is) {
        super(id);
        itemStack = is;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        if (itemStack == null) {
            buf.writeBoolean(false);
            buf.writeInt(friendRights);
            buf.writeInt(otherRights);
        } else {
            buf.writeBoolean(true);
            buf.writeItem(itemStack);
        }
    }

    public ManageRightsAndUpdgradesControl(FriendlyByteBuf buf) {
        super(id);
        if (buf.readBoolean()) {
            itemStack = buf.readItem();
        } else {
            itemStack = null;
            friendRights = buf.readInt();
            otherRights = buf.readInt();
        }
    }

    @Override
    public void handleServer(BlockPos pos, BlockSide side, ScreenBlockEntity tes,
                           CustomPayloadEvent.Context context,
                           Function<Integer, Boolean> permissionChecker) throws MissingPermissionException {
        ServerPlayer player = (ServerPlayer) context.getSender();

        if (itemStack == null) {
            checkPerms(ScreenRights.MANAGE, permissionChecker, player);
            tes.setRights((ServerPlayer) context.getSender(), side, friendRights, otherRights);
        } else {
            checkPerms(ScreenRights.MANAGE_UPGRADES, permissionChecker, player);
            if (!(itemStack.getItem() instanceof IUpgrade)) return;
            tes.removeUpgrade(side, itemStack, player);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClient(BlockPos pos, BlockSide side, ScreenBlockEntity tes, CustomPayloadEvent.Context context) {
        // Client-side handling (if needed)
    }
}
