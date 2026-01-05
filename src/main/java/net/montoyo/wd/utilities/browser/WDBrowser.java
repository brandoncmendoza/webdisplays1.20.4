package net.montoyo.wd.utilities.browser;
import com.cinemamod.mcef.MCEF;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.utilities.Log;
import net.montoyo.wd.utilities.browser.handlers.js.queries.ElementCenterQuery;
import net.montoyo.wd.utilities.browser.handlers.js.JSQueryHandler;
import net.montoyo.wd.utilities.data.BlockSide;
import org.cef.browser.CefBrowser;
import java.util.HashMap;
import java.util.Map;

public interface WDBrowser {
    static CefBrowser createBrowser(String url, boolean transparent) {
        if (!MCEF.isInitialized() || MCEF.getClient() == null) {
            Log.error("MCEF no listo: " + url);
            return null;
        }
        try {
            WDClientBrowser browser = new WDClientBrowser(MCEF.getClient(), url, transparent);
            browser.setCloseAllowed();
            browser.createImmediately();
            registerQueries(browser);
            return browser;
        } catch (Exception e) {
            Log.error("Error: " + e.getMessage());
            return null;
        }
    }
    static void registerQueries(WDBrowser browser) {
        Map<String, JSQueryHandler> handlerMap = browser.queryHandlers();
        handlerMap.put(browser.focusedElement().getName(), browser.focusedElement());
        handlerMap.put(browser.pointerLockElement().getName(), browser.pointerLockElement());
    }
    HashMap<String, JSQueryHandler> queryHandlers();
    ElementCenterQuery focusedElement();
    ElementCenterQuery pointerLockElement();
    void setBe(ScreenBlockEntity blockEntity, BlockSide side);
    ScreenBlockEntity getBe();
    BlockSide getSide();
}
