/*
 * Copyright (C) 2019 BARBOTIN Nicolas
 */

package net.montoyo.wd.core;
import net.montoyo.wd.net.compat.NetworkContextCompat;
import net.minecraftforge.network.PacketDistributor;

import java.util.Map;

public interface IComputerArgs {
    String checkString(int i);
    int checkInteger(int i);
    Map checkTable(int i);
    int count();
}
