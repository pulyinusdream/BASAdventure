with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    lines = f.readlines()

depth = 0
inside = False
for i, line in enumerate(lines):
    if "LazyColumn(" in line and i > 200:
        inside = True
        depth = 0
    if inside:
        depth += line.count('{') - line.count('}')
        if depth == 0 and "}" in line:
            print(f"LazyColumn ends at line {i+1}")
            inside = False
