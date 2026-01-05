import re

files = {
    'src/main/java/net/montoyo/wd/entity/KeyboardBlockEntity.java': (76, 78),
    'src/main/java/net/montoyo/wd/item/ItemLinker.java': (76, 77),
    'src/main/java/net/montoyo/wd/item/ItemMinePad2.java': (103, 104),
    'src/main/java/net/montoyo/wd/block/ScreenBlock.java': (128, 129),
}

for filepath, (start, end) in files.items():
    with open(filepath, 'r') as f:
        lines = f.readlines()
    
    for i in range(start - 1, end):
        if not lines[i].strip().startswith('//'):
            lines[i] = lines[i].replace(lines[i].lstrip(), '// ' + lines[i].lstrip(), 1)
    
    with open(filepath, 'w') as f:
        f.writelines(lines)

# WebDisplays.java - comentar línea 131
with open('src/main/java/net/montoyo/wd/WebDisplays.java', 'r') as f:
    lines = f.readlines()
lines[130] = '        // ' + lines[130].lstrip()
with open('src/main/java/net/montoyo/wd/WebDisplays.java', 'w') as f:
    f.writelines(lines)

print("✅ Archivos corregidos")
