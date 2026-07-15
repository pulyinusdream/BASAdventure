import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    main = f.read()

exams = '''
@Composable
fun KatinigExam1Screen(viewModel: AppViewModel, progress: UserProgress) {
    var currentQuestionIdx by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }

    val katinigLetters = viewModel.lettersKatinig
    val currentLetter = remember(currentQuestionIdx) { katinigLetters.random() }
    val choices = remember(currentLetter) {
        val options = mutableSetOf(currentLetter.char)
        while (options.size < 4) {
            options.add(katinigLetters.random().char)
        }
        options.toList().shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryPurple)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Level1Menu) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Pagsasanay 1: Piliin ang Titik",
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Ano ang simulang titik ng larawang ito?", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(currentLetter.emoji, fontSize = 80.sp)
            }
            Spacer(modifier = Modifier.height(48.dp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(choices) { choice ->
                    val isSelected = choice == selectedAnswer
                    val color = if (isSelected) {
                        if (isCorrect == true) Color.Green else Color.Red
                    } else Color.White

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = color),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable {
                                if (selectedAnswer == null) {
                                    selectedAnswer = choice
                                    isCorrect = choice == currentLetter.char
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(choice, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (selectedAnswer != null) {
                Button(
                    onClick = {
                        if (isCorrect == true) {
                            if (currentQuestionIdx >= 4) {
                                viewModel.exam1Passed.value = true
                                viewModel.navigateTo(Screen.Level1Menu)
                            } else {
                                currentQuestionIdx++
                                selectedAnswer = null
                                isCorrect = null
                            }
                        } else {
                            selectedAnswer = null
                            isCorrect = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(if (isCorrect == true && currentQuestionIdx >= 4) "Tapusin" else if (isCorrect == true) "Susunod" else "Ulitin")
                }
            }
        }
    }
}

@Composable
fun KatinigExam2Screen(viewModel: AppViewModel, progress: UserProgress) {
    var currentQuestionIdx by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }

    val katinigLetters = viewModel.lettersKatinig
    val currentLetter = remember(currentQuestionIdx) { katinigLetters.random() }
    val choices = remember(currentLetter) {
        val options = mutableSetOf(currentLetter.char.lowercase(java.util.Locale.getDefault()))
        while (options.size < 4) {
            options.add(katinigLetters.random().char.lowercase(java.util.Locale.getDefault()))
        }
        options.toList().shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryPurple)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Level1Menu) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Pagsasanay 2: Itugma ang Titik",
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Ano ang maliit na titik nito?", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(currentLetter.char, fontSize = 80.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
            }
            Spacer(modifier = Modifier.height(48.dp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(choices) { choice ->
                    val isSelected = choice == selectedAnswer
                    val color = if (isSelected) {
                        if (isCorrect == true) Color.Green else Color.Red
                    } else Color.White

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = color),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable {
                                if (selectedAnswer == null) {
                                    selectedAnswer = choice
                                    isCorrect = choice == currentLetter.char.lowercase(java.util.Locale.getDefault())
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(choice, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (selectedAnswer != null) {
                Button(
                    onClick = {
                        if (isCorrect == true) {
                            if (currentQuestionIdx >= 4) {
                                viewModel.exam2Passed.value = true
                                viewModel.navigateTo(Screen.Level1Menu)
                            } else {
                                currentQuestionIdx++
                                selectedAnswer = null
                                isCorrect = null
                            }
                        } else {
                            selectedAnswer = null
                            isCorrect = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(if (isCorrect == true && currentQuestionIdx >= 4) "Tapusin" else if (isCorrect == true) "Susunod" else "Ulitin")
                }
            }
        }
    }
}

@Composable
fun KatinigExam3Screen(viewModel: AppViewModel, progress: UserProgress) {
    var currentQuestionIdx by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }

    val katinigLetters = viewModel.lettersKatinig
    val currentLetter = remember(currentQuestionIdx) { katinigLetters.random() }
    val missingWord = remember(currentLetter) {
        val w = currentLetter.word
        if (w.lowercase(java.util.Locale.getDefault()).startsWith(currentLetter.char.lowercase(java.util.Locale.getDefault()))) {
            "_ " + w.substring(currentLetter.char.length)
        } else {
            "_ " + w.substring(1)
        }
    }

    val choices = remember(currentLetter) {
        val options = mutableSetOf(currentLetter.char)
        while (options.size < 4) {
            options.add(katinigLetters.random().char)
        }
        options.toList().shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryPurple)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Level1Menu) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Pagsasanay 3: Nawawalang Titik",
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Ano ang nawawalang titik?", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(currentLetter.emoji, fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(missingWord, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(choices) { choice ->
                    val isSelected = choice == selectedAnswer
                    val color = if (isSelected) {
                        if (isCorrect == true) Color.Green else Color.Red
                    } else Color.White

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = color),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable {
                                if (selectedAnswer == null) {
                                    selectedAnswer = choice
                                    isCorrect = choice == currentLetter.char
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(choice, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (selectedAnswer != null) {
                Button(
                    onClick = {
                        if (isCorrect == true) {
                            if (currentQuestionIdx >= 4) {
                                viewModel.exam3Passed.value = true
                                viewModel.navigateTo(Screen.Level1Menu)
                            } else {
                                currentQuestionIdx++
                                selectedAnswer = null
                                isCorrect = null
                            }
                        } else {
                            selectedAnswer = null
                            isCorrect = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(if (isCorrect == true && currentQuestionIdx >= 4) "Tapusin" else if (isCorrect == true) "Susunod" else "Ulitin")
                }
            }
        }
    }
}
'''

# Now let's inject these exams into the navigation graph in MainActivity

old_nav = '''                        is Screen.Level1Menu -> Level1MenuScreen(viewModel = viewModel, progress = progressState)
                        is Screen.Learn -> LearnScreen(viewModel = viewModel, progress = progressState)'''

new_nav = '''                        is Screen.Level1Menu -> Level1MenuScreen(viewModel = viewModel, progress = progressState)
                        is Screen.Learn -> LearnScreen(viewModel = viewModel, progress = progressState)
                        is Screen.KatinigExam1 -> KatinigExam1Screen(viewModel = viewModel, progress = progressState)
                        is Screen.KatinigExam2 -> KatinigExam2Screen(viewModel = viewModel, progress = progressState)
                        is Screen.KatinigExam3 -> KatinigExam3Screen(viewModel = viewModel, progress = progressState)'''

main = main.replace(old_nav, new_nav)
main += exams

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(main)

