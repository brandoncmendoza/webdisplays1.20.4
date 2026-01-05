package net.montoyo.wd;
import net.montoyo.wd.utilities.math.Vector3i;
import net.montoyo.wd.utilities.math.Vector2i;
import net.montoyo.wd.utilities.data.BlockSide;
import net.montoyo.wd.utilities.data.Rotation;
import net.montoyo.wd.utilities.serialization.NameUUIDPair;
import net.montoyo.wd.entity.ScreenBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;


import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.montoyo.wd.net.client_bound.S2CMessageScreenUpdate;

public class SharedProxy {
    public void preInit() {}
    public void init() {}
    public void postInit() {}
    public void onAutocompleteResult(NameUUIDPair[] result) {}
    public void onJSResponse(int queryID, String response) {}
    public void onScreenUpdate(S2CMessageScreenUpdate msg) {}
    
    public Level getWorld(ResourceKey<Level> dim) {
        return null;
    }

    public void registerHandlers() {}





    public double distanceTo(ScreenBlockEntity te, Vec3 camera) { return 0.0; }
    /* ---------- Métodos que faltaban ---------- */
    public net.minecraft.server.MinecraftServer getServer() {
        return net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
    }
    public void screenUpdateResolutionInGui(Vector3i pos, BlockSide side, Vector2i res) { /* paquete no existe */ }
    public void screenUpdateRotationInGui(Vector3i pos, BlockSide side, Rotation rot) { /* paquete no existe */ }
    public void screenUpdateAutoVolumeInGui(Vector3i pos, BlockSide side, boolean av) { /* paquete no existe */ }
    public void trackScreen(net.montoyo.wd.entity.ScreenBlockEntity te, boolean track) { }
}
