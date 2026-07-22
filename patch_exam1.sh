#!/bin/bash
sed -i 's/var isCorrect by remember { mutableStateOf<Boolean?>(null) }/var isCorrect by remember { mutableStateOf<Boolean?>(null) }\n\n    var timeLeft by remember { mutableStateOf(60) }\n    LaunchedEffect(Unit) {\n        while (timeLeft > 0) {\n            kotlinx.coroutines.delay(1000)\n            timeLeft--\n        }\n        viewModel.exam1Passed.value = true\n        viewModel.navigateTo(Screen.KatinigExam2)\n    }/' app/src/main/java/com/example/MainActivity.kt

sed -i 's/Text(/Text(/g' app/src/main/java/com/example/MainActivity.kt # just a dummy check
