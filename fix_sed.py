with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

text = text.replace("onClick = onClick,\n        shape = RoundedCornerShape(24.dp),", "shape = RoundedCornerShape(24.dp),")
text = text.replace("onClick = onClick,\n                    shape = RoundedCornerShape(24.dp),", "shape = RoundedCornerShape(24.dp),")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
