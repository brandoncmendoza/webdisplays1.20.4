import re

# Leer el archivo
with open('src/main/java/net/montoyo/wd/entity/ScreenBlockEntity.java', 'r') as f:
    content = f.read()

# Patrón para capturar: send(PacketDistributor.X.with(...), mensaje);
# Necesitamos manejar paréntesis anidados correctamente
pattern = r'WDNetworkRegistry\.INSTANCE\.send\((PacketDistributor\.\w+\.with\([^)]+\)),\s+(.*?)\);'

def replace_func(match):
    distributor = match.group(1)
    message = match.group(2)
    return f'WDNetworkRegistry.INSTANCE.send({message}, {distributor});'

# Reemplazar
new_content = re.sub(pattern, replace_func, content)

# Escribir
with open('src/main/java/net/montoyo/wd/entity/ScreenBlockEntity.java', 'w') as f:
    f.write(new_content)

print("✅ Archivo arreglado")
