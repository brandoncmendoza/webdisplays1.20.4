/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package net.montoyo.wd.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.montoyo.wd.miniserv.client.Client;
import net.montoyo.wd.utilities.math.Vector3i;
import net.montoyo.wd.utilities.serialization.NameUUIDPair;
import net.montoyo.wd.utilities.Log;
import java.util.ArrayList;

public class GuiServer extends Screen {
    private final Vector3i serverPos;
    private final NameUUIDPair owner;
    private final ArrayList<String> lines = new ArrayList<>();
    private String userPrompt;
    
    public GuiServer(Vector3i vec, NameUUIDPair owner, byte[] key) {
        super(Component.nullToEmpty(null));
        this.serverPos = vec;
        this.owner = owner;
        this.userPrompt = "> ";

        // Establecer la clave en el cliente de Miniserv
        Client c = Client.getInstance();
        if (c != null && key != null && key.length > 0) {
            c.setKey(key);
            Log.info("Miniserv Key establecida en GuiServer: %d bytes", key.length);
        } else if (key == null) {
            Log.warning("GuiServer abierto sin llave (posiblemente modo offline)");
        }

        lines.add("MiniServ 1.0");
        lines.add("Type 'help' for a list of commands.");
    }

    public GuiServer(Vector3i vec, NameUUIDPair owner) {
        this(vec, owner, null);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        int y = 10;
        for (String line : lines) {
            guiGraphics.drawString(this.font, line, 10, y, 0xFFFFFF);
            y += 10;
        }
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
