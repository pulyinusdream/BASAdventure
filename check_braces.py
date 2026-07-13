with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    lines = f.readlines()

depth = 0
inside = False
for i, line in enumerate(lines):
    if "fun HomeScreen" in line:
        inside = True
        depth = 0
    if inside:
        depth += line.count('{') - line.count('}')
        if depth == 0 and "}" in line:
            print(f"HomeScreen ends at line {i+1}")
            inside = False
