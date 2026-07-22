#!/bin/bash
sed -i -e '/title = "Pagsasanay 1"/,/modifier = Modifier.weight(1f)/!b' \
    -e 's/onClick = {/onClick = { if (isPagsasanayUnlocked) {/' \
    -e 's/viewModel.navigateTo(Screen.KatinigExam1)/viewModel.navigateTo(Screen.KatinigExam1) }/' \
    -e 's/modifier = Modifier.weight(1f)/modifier = Modifier.weight(1f),\n                        isLocked = !isPagsasanayUnlocked/' app/src/main/java/com/example/MainActivity.kt
