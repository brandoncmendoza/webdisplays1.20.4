#!/bin/bash

echo "=== Corrigiendo errores de compilación ==="

# 1. MinePadRenderer.java - Cambiar 'continue' por 'return' y usar package correcto
sed -i '
/if (pd.view == null) {/,/continue; \/\/ Browser no creado aún/ {
    s/continue;/return;/
}
/if (!(pd.view instanceof net.montoyo.mcef.api.MCEFBrowser)) {/,/continue; \/\/ No es un MCEFBrowser/ {
    s/net\.montoyo\.mcef\.api\.MCEFBrowser/com.cinemamod.mcef.MCEFBrowser/g
    s/continue;/return;/
}
/net.montoyo.mcef.api.MCEFBrowser mcefBrowser = (net.montoyo.mcef.api.MCEFBrowser) pd.view;/ {
    s/net\.montoyo\.mcef\.api\.MCEFBrowser/com.cinemamod.mcef.MCEFBrowser/g
}
/if (mcefBrowser.getRenderer() == null) {/,/continue; \/\/ Renderer no inicializado/ {
    s/continue;/return;/
}
' src/main/java/net/montoyo/wd/client/renderers/MinePadRenderer.java

# 2. GuiMinePad.java - Cambiar package y arreglar returns
sed -i '
s/net\.montoyo\.mcef\.api\.MCEFBrowser/com.cinemamod.mcef.MCEFBrowser/g
' src/main/java/net/montoyo/wd/client/gui/GuiMinePad.java

# Arreglar los 'return' que deben devolver boolean
sed -i '
/public boolean charTyped(char codePoint, int modifiers) {/,/^    }/ {
    /if (!(pad.view instanceof com.cinemamod.mcef.MCEFBrowser)) return;/ {
        s/return;/return false;/
    }
}
/public boolean keyPressed(int keyCode, int scanCode, int modifiers) {/,/^    }/ {
    /if (!(pad.view instanceof com.cinemamod.mcef.MCEFBrowser)) return;/ {
        s/return;/return false;/
    }
}
' src/main/java/net/montoyo/wd/client/gui/GuiMinePad.java

echo "=== Correcciones aplicadas ==="
