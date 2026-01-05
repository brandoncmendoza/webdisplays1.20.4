/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package net.montoyo.wd.client.gui.controls;
import net.montoyo.wd.net.compat.NetworkContextCompat;
import net.minecraftforge.network.PacketDistributor;

public abstract class Event<T extends Control> {

    protected T source;

    public T getSource() {
        return source;
    }
}
