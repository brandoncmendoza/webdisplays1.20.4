package net.montoyo.wd.utilities.browser.handlers.js;
import net.montoyo.wd.net.compat.NetworkContextCompat;
import net.minecraftforge.network.PacketDistributor;

import com.google.gson.JsonObject;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;

public abstract class JSQueryHandler {
    protected final String name;

    public JSQueryHandler(String name) {
        this.name = name;
    }

    public abstract boolean handle(CefBrowser browser, CefFrame frame, JsonObject data, boolean persistent, CefQueryCallback callback);

    public final String getName() {
        return name;
    }
}
