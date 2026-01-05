#!/bin/bash

echo "======================================"
echo "DIAGNÓSTICO WEB DISPLAYS 1.20.4"
echo "======================================"
echo ""

echo "1. ARCHIVOS GENERADOS:"
echo "----------------------"
ls -lh build/libs/*.jar 2>/dev/null || echo "❌ No hay JARs en build/libs/"
echo ""

echo "2. TAMAÑO DE LOS MODS:"
echo "----------------------"
echo "MCEF:"
ls -lh libs/mcef-forge-1-20-4-1.0.jar 2>/dev/null || echo "❌ MCEF no encontrado"
echo "WebDisplays:"
ls -lh build/libs/webdisplays*.jar 2>/dev/null || echo "❌ WebDisplays no encontrado"
echo ""

echo "3. VERIFICAR CÓDIGO CRÍTICO:"
echo "----------------------------"

echo "✓ Verificando inicialización de MCEF..."
grep -q "MCEF.initialize()" src/main/java/net/montoyo/wd/client/ClientProxy.java && echo "  ✓ MCEF.initialize() presente" || echo "  ❌ MCEF.initialize() NO encontrado"

echo "✓ Verificando onCommonSetup..."
grep -q "public static void onCommonSetup" src/main/java/net/montoyo/wd/client/ClientProxy.java && echo "  ✓ onCommonSetup() presente" || echo "  ❌ onCommonSetup() NO encontrado"

echo "✓ Verificando verificaciones en WDBrowser..."
grep -q "if (!MCEF.isInitialized()" src/main/java/net/montoyo/wd/utilities/browser/WDBrowser.java && echo "  ✓ Verificación de MCEF en WDBrowser" || echo "  ❌ Sin verificación en WDBrowser"

echo "✓ Verificando logs de debug..."
grep -q 'Log.info("Creando browser' src/main/java/net/montoyo/wd/utilities/browser/WDBrowser.java && echo "  ✓ Logs de creación de browser" || echo "  ⚠ Sin logs de debug"

echo ""

echo "4. CONTENIDO DEL JAR DE WEBDISPLAYS:"
echo "------------------------------------"
if [ -f build/libs/webdisplays*.jar ]; then
    JAR_FILE=$(ls build/libs/webdisplays*.jar | head -1)
    echo "Archivo: $JAR_FILE"
    echo ""
    echo "Clases principales:"
    jar -tf "$JAR_FILE" | grep -E "(ClientProxy|WDBrowser|ScreenRenderer|WebDisplays)" | head -10
    echo ""
    echo "Total de archivos en el JAR:"
    jar -tf "$JAR_FILE" | wc -l
else
    echo "❌ No se encontró el JAR de WebDisplays"
fi

echo ""
echo "5. MODS.TOML (Metadatos del mod):"
echo "----------------------------------"
if [ -f src/main/resources/META-INF/mods.toml ]; then
    echo "Versión del mod:"
    grep "version" src/main/resources/META-INF/mods.toml | head -1
    echo ""
    echo "Dependencias:"
    grep -A 5 "dependencies" src/main/resources/META-INF/mods.toml | head -10
else
    echo "❌ mods.toml no encontrado"
fi

echo ""
echo "6. RESUMEN:"
echo "-----------"
echo "Para probar el mod completamente necesitas:"
echo ""
echo "A) EN SERVIDOR:"
echo "   1. Copiar ambos JARs a la carpeta mods/ del servidor:"
echo "      - mcef-forge-1-20-4-1.0.jar"
echo "      - webdisplays-1.20.4-X.X.X.jar"
echo "   2. Iniciar el servidor"
echo "   3. Buscar en logs: 'Inicializando MCEF'"
echo ""
echo "B) PRUEBAS EN JUEGO:"
echo "   1. Colocar un bloque de pantalla"
echo "   2. Click derecho con Linker Tool"
echo "   3. Configurar URL (ej: https://www.youtube.com)"
echo "   4. Verificar que la pantalla:"
echo "      - NO sale blanca"
echo "      - Muestra contenido web"
echo "      - Reproduce video de YouTube"
echo ""
echo "C) LOGS A REVISAR:"
echo "   En logs/latest.log buscar:"
echo "   - 'Inicializando MCEF...'"
echo "   - 'MCEF OK' o 'MCEF TIMEOUT'"
echo "   - 'Creando browser para URL:'"
echo "   - Cualquier error con 'MCEF' o 'WebDisplays'"
echo ""
echo "======================================"
