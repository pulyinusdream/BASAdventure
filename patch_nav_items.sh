#!/bin/bash
sed -i 's/NavigationItem("Bituin", Screen.Progress, Icons.Default.Star)/NavigationItem("Bituin", Screen.Progress, Icons.Default.Star),\n                NavigationItem("Settings", Screen.Settings, Icons.Default.Settings)/' app/src/main/java/com/example/MainActivity.kt
