with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    lines = f.readlines()

depth = 0
for i, line in enumerate(lines):
    if 210 <= i + 1 <= 380:
        d = line.count('{') - line.count('}')
        depth += d
        print(f"{i+1:3d} | {depth:2d} | {line.strip()}")
