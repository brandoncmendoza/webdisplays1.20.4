#!/bin/bash

echo "=== Aplicando parches de seguridad para NullPointerException ==="

# 1. MinePadRenderer.java - Agregar verificación antes de línea 109
echo "Parcheando MinePadRenderer.java..."
sed -i '109i\
                                // Verificar que el browser esté inicializado\
                                if (pd.view == null) {\
                                    continue; // Browser no creado aún\
                                }\
                                if (!(pd.view instanceof net.montoyo.mcef.api.MCEFBrowser)) {\
                                    continue; // No es un MCEFBrowser\
                                }\
                                net.montoyo.mcef.api.MCEFBrowser mcefBrowser = (net.montoyo.mcef.api.MCEFBrowser) pd.view;\
                                if (mcefBrowser.getRenderer() == null) {\
                                    continue; // Renderer no inicializado\
                                }' src/main/java/net/montoyo/wd/client/renderers/MinePadRenderer.java

# Reemplazar el cast directo con la variable segura
sed -i 's/((MCEFBrowser) pd\.view)\.getRenderer()/mcefBrowser.getRenderer()/g' src/main/java/net/montoyo/wd/client/renderers/MinePadRenderer.java

# 2. GuiMinePad.java - Agregar verificaciones en todos los usos de pad.view
echo "Parcheando GuiMinePad.java..."

# Línea 71: setCursor
sed -i '/((MCEFBrowser) pad.view).setCursor(CefCursorType.fromId(pad.activeCursor));/i\
            if (pad.view == null || !(pad.view instanceof net.montoyo.mcef.api.MCEFBrowser)) return;' src/main/java/net/montoyo/wd/client/gui/GuiMinePad.java

# Línea 110: getRenderer
sed -i '/RenderSystem.setShaderTexture(0, ((MCEFBrowser) pad.view).getRenderer().getTextureID());/i\
                    if (pad.view == null || !(pad.view instanceof net.montoyo.mcef.api.MCEFBrowser)) continue;\
                    net.montoyo.mcef.api.MCEFBrowser mcefBrowser = (net.montoyo.mcef.api.MCEFBrowser) pad.view;\
                    if (mcefBrowser.getRenderer() == null) continue;' src/main/java/net/montoyo/wd/client/gui/GuiMinePad.java

sed -i 's/((MCEFBrowser) pad.view).getRenderer().getTextureID()/mcefBrowser.getRenderer().getTextureID()/g' src/main/java/net/montoyo/wd/client/gui/GuiMinePad.java

echo "=== Parches aplicados ==="
