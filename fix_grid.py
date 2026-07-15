import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    main = f.read()

old_level1_screen = '''@Composable
fun Level1MenuScreen(viewModel: AppViewModel, progress: UserProgress) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryPurple)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mga Letra (Level 1)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BentoLevelCard(
                title = "I. MGA PATINIG",
                subtitle = "A E I O U",
                icon = Icons.Default.FontDownload,
                bgColor = Color(0xFFF06292),
                onClick = { 
                    viewModel.currentSubLevel.value = 1
                    viewModel.selectedLetterIdx.value = 0
                    viewModel.navigateTo(Screen.Learn) 
                },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                isLocked = false
            )

            BentoLevelCard(
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
            }

            BentoLevelCard(
                title = "III. MGA HIRAM NA LETRA",
                subtitle = "C F J Q V X Z Ñ",
                icon = Icons.Default.Public,
                bgColor = Color(0xFF4BB543),
                onClick = { 
                    viewModel.currentSubLevel.value = 3
                    viewModel.selectedLetterIdx.value = 0
                    viewModel.navigateTo(Screen.Learn) 
                },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                isLocked = false
            )
        }
    }
}'''

new_level1_screen = '''@Composable
fun Level1MenuScreen(viewModel: AppViewModel, progress: UserProgress) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryPurple)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mga Letra (Level 1)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("MGA URI NG LETRA", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryPurple)
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BentoLevelCard(
                        title = "I. PATINIG",
                        subtitle = "A E I O U",
                        icon = Icons.Default.FontDownload,
                        bgColor = Color(0xFFF06292),
                        onClick = { 
                            viewModel.currentSubLevel.value = 1
                            viewModel.selectedLetterIdx.value = 0
                            viewModel.navigateTo(Screen.Learn) 
                        },
                        modifier = Modifier.weight(1f),
                        isLocked = false
                    )

                    BentoLevelCard(
                        title = "II. KATINIG",
                        subtitle = "Mga katinig",
                        icon = Icons.Default.SortByAlpha,
                        bgColor = Color(0xFF4BA3E3),
                        onClick = { 
                            viewModel.currentSubLevel.value = 2
                            viewModel.selectedLetterIdx.value = 0
                            viewModel.navigateTo(Screen.Learn) 
                        },
                        modifier = Modifier.weight(1f),
                        isLocked = false
                    )
                }
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BentoLevelCard(
                        title = "III. HIRAM NA LETRA",
                        subtitle = "C F J Q V X Z Ñ",
                        icon = Icons.Default.Public,
                        bgColor = Color(0xFF4BB543),
                        onClick = { 
                            viewModel.currentSubLevel.value = 3
                            viewModel.selectedLetterIdx.value = 0
                            viewModel.navigateTo(Screen.Learn) 
                        },
                        modifier = Modifier.weight(1f),
                        isLocked = false
                    )
                    
                    Spacer(modifier = Modifier.weight(1f)) // Empty placeholder
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("PAGSASANAY (MGA KATINIG)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryPurple)
            }

            item {
                val exam1Passed by viewModel.exam1Passed.collectAsState()
                val exam2Passed by viewModel.exam2Passed.collectAsState()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BentoLevelCard(
                        title = "Pagsasanay 1",
                        subtitle = "Piliin ang Titik",
                        icon = Icons.Default.Quiz,
                        bgColor = Color(0xFF9061F9),
                        onClick = { 
                            viewModel.navigateTo(Screen.KatinigExam1)
                        },
                        modifier = Modifier.weight(1f)
                    )

                    if (exam1Passed) {
                        BentoLevelCard(
                            title = "Pagsasanay 2",
                            subtitle = "Itugma ang titik",
                            icon = Icons.Default.JoinInner,
                            bgColor = Color(0xFF9061F9),
                            onClick = { viewModel.navigateTo(Screen.KatinigExam2) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        BentoLevelCard(
                            title = "Pagsasanay 2",
                            subtitle = "Naka-lock",
                            icon = Icons.Default.Lock,
                            bgColor = Color.Gray,
                            onClick = { /* Locked */ },
                            modifier = Modifier.weight(1f),
                            isLocked = true
                        )
                    }
                }
            }
            
            item {
                val exam2Passed by viewModel.exam2Passed.collectAsState()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (exam2Passed) {
                        BentoLevelCard(
                            title = "Pagsasanay 3",
                            subtitle = "Hanapin ang titik",
                            icon = Icons.Default.Search,
                            bgColor = Color(0xFF9061F9),
                            onClick = { viewModel.navigateTo(Screen.KatinigExam3) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        BentoLevelCard(
                            title = "Pagsasanay 3",
                            subtitle = "Naka-lock",
                            icon = Icons.Default.Lock,
                            bgColor = Color.Gray,
                            onClick = { /* Locked */ },
                            modifier = Modifier.weight(1f),
                            isLocked = true
                        )
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}'''

if old_level1_screen in main:
    main = main.replace(old_level1_screen, new_level1_screen)
    print("Replaced old_level1_screen")
else:
    print("old_level1_screen not found")

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(main)

