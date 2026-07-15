import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    main = f.read()

old_katinig_card = '''            BentoLevelCard(
                title = "II. MGA KATINIG",
                subtitle = "Mga katinig sa alpabetong Pilipino",
                icon = Icons.Default.SortByAlpha,
                bgColor = Color(0xFF4BA3E3),
                onClick = { 
                    viewModel.currentSubLevel.value = 2
                    viewModel.selectedLetterIdx.value = 0
                    viewModel.navigateTo(Screen.Learn) 
                },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                isLocked = false
            )'''

new_katinig_card = '''            BentoLevelCard(
                title = "II. MGA KATINIG",
                subtitle = "Mga katinig sa alpabetong Pilipino",
                icon = Icons.Default.SortByAlpha,
                bgColor = Color(0xFF4BA3E3),
                onClick = { 
                    viewModel.currentSubLevel.value = 2
                    viewModel.selectedLetterIdx.value = 0
                    viewModel.navigateTo(Screen.Learn) 
                },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                isLocked = false
            )
            
            // Pagsasanay para sa Katinig
            Text("PAGSASANAY (MGA KATINIG)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryPurple)

            BentoLevelCard(
                title = "Pagsasanay 1",
                subtitle = "Piliin ang Tamang Titik",
                icon = Icons.Default.Quiz,
                bgColor = Color(0xFF9061F9),
                onClick = { 
                    viewModel.navigateTo(Screen.KatinigExam1)
                },
                modifier = Modifier.fillMaxWidth().height(90.dp)
            )

            val exam1Passed by viewModel.exam1Passed.collectAsState()
            val exam2Passed by viewModel.exam2Passed.collectAsState()

            if (exam1Passed) {
                BentoLevelCard(
                    title = "Pagsasanay 2",
                    subtitle = "Itugma ang titik",
                    icon = Icons.Default.JoinInner,
                    bgColor = Color(0xFF9061F9),
                    onClick = { viewModel.navigateTo(Screen.KatinigExam2) },
                    modifier = Modifier.fillMaxWidth().height(90.dp)
                )
            } else {
                BentoLevelCard(
                    title = "Pagsasanay 2 (Naka-lock)",
                    subtitle = "Tapusin muna ang Pagsasanay 1",
                    icon = Icons.Default.Lock,
                    bgColor = Color.Gray,
                    onClick = { /* Locked */ },
                    modifier = Modifier.fillMaxWidth().height(90.dp),
                    isLocked = true
                )
            }

            if (exam2Passed) {
                BentoLevelCard(
                    title = "Pagsasanay 3",
                    subtitle = "Hanapin ang titik",
                    icon = Icons.Default.Search,
                    bgColor = Color(0xFF9061F9),
                    onClick = { viewModel.navigateTo(Screen.KatinigExam3) },
                    modifier = Modifier.fillMaxWidth().height(90.dp)
                )
            } else {
                BentoLevelCard(
                    title = "Pagsasanay 3 (Naka-lock)",
                    subtitle = "Tapusin muna ang Pagsasanay 2",
                    icon = Icons.Default.Lock,
                    bgColor = Color.Gray,
                    onClick = { /* Locked */ },
                    modifier = Modifier.fillMaxWidth().height(90.dp),
                    isLocked = true
                )
            }'''

if old_katinig_card in main:
    main = main.replace(old_katinig_card, new_katinig_card)
    print("Replaced Katinig card")
else:
    print("Katinig card not found")

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(main)

