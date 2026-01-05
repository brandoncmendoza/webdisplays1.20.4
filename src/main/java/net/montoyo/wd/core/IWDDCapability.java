/*
 * Copyright (C) 2019 BARBOTIN Nicolas
 */

package net.montoyo.wd.core;
import net.montoyo.wd.net.compat.NetworkContextCompat;
import net.minecraftforge.network.PacketDistributor;

public interface IWDDCapability {
    boolean isFirstRun();
    void clearFirstRun();
    void cloneTo(IWDDCapability dst);
}
