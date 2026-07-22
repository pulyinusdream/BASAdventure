#!/bin/bash

# Update Exam 1 Top Bar
sed -i -e '/text = "Pagsasanay 1: Piliin ang Titik"/,/color = Color.White/!b' \
    -e 's/text = "Pagsasanay 1: Piliin ang Titik"/text = "Pagsasanay 1: Piliin ang Titik",\n                modifier = Modifier.weight(1f)/' app/src/main/java/com/example/MainActivity.kt

sed -i '/text = "Pagsasanay 1: Piliin ang Titik"/!b;n;n;n;n;a\
            Text(\
                text = "${timeLeft}s",\
                style = MaterialTheme.typography.titleLarge.copy(\
                    fontWeight = FontWeight.Bold,\
                    color = Color.Red\
                )\
            )' app/src/main/java/com/example/MainActivity.kt

# Insert Timer logic to Exam 2
sed -i 's/var isCorrect by remember { mutableStateOf<Boolean?>(null) }/var isCorrect by remember { mutableStateOf<Boolean?>(null) }\n\n    var timeLeft by remember { mutableStateOf(60) }\n    LaunchedEffect(Unit) {\n        while (timeLeft > 0) {\n            kotlinx.coroutines.delay(1000)\n            timeLeft--\n        }\n        viewModel.exam2Passed.value = true\n        viewModel.navigateTo(Screen.KatinigExam3)\n    }/' app/src/main/java/com/example/MainActivity.kt

# Update Exam 2 Top Bar
sed -i -e '/text = "Pagsasanay 2: Itugma ang Titik"/,/color = Color.White/!b' \
    -e 's/text = "Pagsasanay 2: Itugma ang Titik"/text = "Pagsasanay 2: Itugma ang Titik",\n                modifier = Modifier.weight(1f)/' app/src/main/java/com/example/MainActivity.kt

sed -i '/text = "Pagsasanay 2: Itugma ang Titik"/!b;n;n;n;n;a\
            Text(\
                text = "${timeLeft}s",\
                style = MaterialTheme.typography.titleLarge.copy(\
                    fontWeight = FontWeight.Bold,\
                    color = Color.Red\
                )\
            )' app/src/main/java/com/example/MainActivity.kt


# Insert Timer logic to Exam 3
sed -i 's/var isCorrect by remember { mutableStateOf<Boolean?>(null) }/var isCorrect by remember { mutableStateOf<Boolean?>(null) }\n\n    var timeLeft by remember { mutableStateOf(60) }\n    LaunchedEffect(Unit) {\n        while (timeLeft > 0) {\n            kotlinx.coroutines.delay(1000)\n            timeLeft--\n        }\n        viewModel.exam3Passed.value = true\n        viewModel.navigateTo(Screen.Level1Menu)\n    }/' app/src/main/java/com/example/MainActivity.kt

# Update Exam 3 Top Bar
sed -i -e '/text = "Pagsasanay 3: Nawawalang Titik"/,/color = Color.White/!b' \
    -e 's/text = "Pagsasanay 3: Nawawalang Titik"/text = "Pagsasanay 3: Nawawalang Titik",\n                modifier = Modifier.weight(1f)/' app/src/main/java/com/example/MainActivity.kt

sed -i '/text = "Pagsasanay 3: Nawawalang Titik"/!b;n;n;n;n;a\
            Text(\
                text = "${timeLeft}s",\
                style = MaterialTheme.typography.titleLarge.copy(\
                    fontWeight = FontWeight.Bold,\
                    color = Color.Red\
                )\
            )' app/src/main/java/com/example/MainActivity.kt

