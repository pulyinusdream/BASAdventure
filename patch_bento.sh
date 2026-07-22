#!/bin/bash
sed -i -e '/title = "II. KATINIG"/,/isLocked = false/!b' \
    -e 's/onClick = {/onClick = { if (isLevel2Unlocked) {/' \
    -e 's/viewModel.navigateTo(Screen.Learn)/viewModel.navigateTo(Screen.Learn) }/' \
    -e 's/isLocked = false/isLocked = !isLevel2Unlocked/' app/src/main/java/com/example/MainActivity.kt

sed -i -e '/title = "III. HIRAM NA LETRA"/,/isLocked = false/!b' \
    -e 's/onClick = {/onClick = { if (isLevel3Unlocked) {/' \
    -e 's/viewModel.navigateTo(Screen.Learn)/viewModel.navigateTo(Screen.Learn) }/' \
    -e 's/isLocked = false/isLocked = !isLevel3Unlocked/' app/src/main/java/com/example/MainActivity.kt
