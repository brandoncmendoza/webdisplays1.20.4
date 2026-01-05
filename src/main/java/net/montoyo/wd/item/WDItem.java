/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package net.montoyo.wd.item;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;


import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.montoyo.wd.WebDisplays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface WDItem {
    static void addInformation(@Nullable List<String> tt) {
        if (tt != null && (FMLEnvironment.dist == Dist.CLIENT ? ((net.montoyo.wd.client.ClientProxy) WebDisplays.PROXY).isShiftDown() : false))
            tt.add(ChatFormatting.GRAY + I18n.get("item.webdisplays.wiki"));
    }

    String getWikiName(@Nonnull ItemStack is);
}
