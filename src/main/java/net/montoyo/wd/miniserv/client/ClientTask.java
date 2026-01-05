/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package net.montoyo.wd.miniserv.client;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;

import net.montoyo.wd.net.compat.NetworkContextCompat;
import net.minecraftforge.network.PacketDistributor;

import net.montoyo.wd.WebDisplays;

import java.util.function.Consumer;

public abstract class ClientTask<T extends ClientTask> {

    private Consumer<T> finishCallback;
    private volatile boolean canceled;
    protected boolean runCallbackOnMcThread;
    protected final Client client = Client.getInstance();

    public abstract void start();
    public abstract void abort();

    public void onFinished() {
        //Called by Client, don't call it from a ClientTask!
        if(finishCallback != null && !isCanceled()) {
            if(runCallbackOnMcThread)
                if (FMLEnvironment.dist == Dist.CLIENT) ((net.montoyo.wd.client.ClientProxy) WebDisplays.PROXY).enqueue(() -> finishCallback.accept((T) this));
            else
                finishCallback.accept((T) this);
        }
    }

    public void setFinishCallback(Consumer<T> finishCallback) {
        this.finishCallback = finishCallback;
    }

    public void setRunCallbackOnMinecraftThread(boolean runCallbackOnMcThread) {
        this.runCallbackOnMcThread = runCallbackOnMcThread;
    }

    public final void cancel() {
        synchronized(this) {
            canceled = true;
        }

        Client.getInstance().wakeup();
    }

    public final boolean isCanceled() {
        boolean ret;
        synchronized(this) {
            ret = canceled;
        }

        return ret;
    }

}
