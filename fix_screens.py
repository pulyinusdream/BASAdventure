import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    main = f.read()

# 1. Fix LearnScreen to have a Back Button
old_learn_header = '''        // Level Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFCE93D8))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "LEVEL 1",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "ALPABETONG PILIPINO",
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    fontSize = 24.sp
                )
            }
        }'''

new_learn_header = '''        // Level Title with Back Button
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
            Column {
                Text(
                    text = "MGA LETRA",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }'''

if old_learn_header in main:
    main = main.replace(old_learn_header, new_learn_header)
    print("Replaced LearnScreen header")
else:
    print("LearnScreen header not found")

# 2. Fix Katinig Exams logic and layout
def replace_exam(exam_name, is_small_letter=False, is_missing_letter=False):
    global main
    
    match = re.search(r'@Composable\s*fun ' + exam_name + r'Screen\(viewModel: AppViewModel, progress: UserProgress\) \{.*?(?=@Composable|$)', main, re.DOTALL)
    if not match:
        print(f"{exam_name} not found")
        return
        
    exam_code = match.group(0)
    
    new_exam = f'''@Composable
fun {exam_name}Screen(viewModel: AppViewModel, progress: UserProgress) {{
    var currentQuestionIdx by remember {{ mutableStateOf(0) }}
    var selectedAnswer by remember {{ mutableStateOf<String?>(null) }}
    var isCorrect by remember {{ mutableStateOf<Boolean?>(null) }}

    val katinigLetters = viewModel.lettersKatinig
    // Generate 5 distinct letters for the exam
    val examLetters = remember {{ katinigLetters.shuffled().take(5) }}
    val currentLetter = if (currentQuestionIdx < 5) examLetters[currentQuestionIdx] else examLetters.last()
'''
    
    if is_missing_letter:
        new_exam += '''    val missingWord = remember(currentLetter) {
        val w = currentLetter.word
        if (w.lowercase(java.util.Locale.getDefault()).startsWith(currentLetter.char.lowercase(java.util.Locale.getDefault()))) {
            "_ " + w.substring(currentLetter.char.length)
        } else {
            "_ " + w.substring(1)
        }
    }
'''

    if is_small_letter:
        new_exam += '''    val choices = remember(currentLetter) {
        val options = mutableSetOf(currentLetter.char.lowercase(java.util.Locale.getDefault()))
        val shuffledPool = katinigLetters.shuffled()
        var i = 0
        while (options.size < 4 && i < shuffledPool.size) {
            options.add(shuffledPool[i].char.lowercase(java.util.Locale.getDefault()))
            i++
        }
        options.toList().shuffled()
    }'''
    else:
        new_exam += '''    val choices = remember(currentLetter) {
        val options = mutableSetOf(currentLetter.char)
        val shuffledPool = katinigLetters.shuffled()
        var i = 0
        while (options.size < 4 && i < shuffledPool.size) {
            options.add(shuffledPool[i].char)
            i++
        }
        options.toList().shuffled()
    }'''

    title = ""
    desc = ""
    if exam_name == "KatinigExam1":
        title = "Pagsasanay 1: Piliin ang Titik"
        desc = "Ano ang simulang titik ng larawang ito?"
    elif exam_name == "KatinigExam2":
        title = "Pagsasanay 2: Itugma ang Titik"
        desc = "Ano ang maliit na titik nito?"
    else:
        title = "Pagsasanay 3: Nawawalang Titik"
        desc = "Ano ang nawawalang titik?"
        
    new_exam += f'''

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryPurple)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {{
            IconButton(onClick = {{ viewModel.navigateTo(Screen.Level1Menu) }}) {{
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.White)
            }}
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "{title}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }}

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {{
            Text("{desc}", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
'''

    if exam_name == "KatinigExam1":
        new_exam += '''            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(currentLetter.emoji, fontSize = 60.sp)
            }'''
    elif exam_name == "KatinigExam2":
        new_exam += '''            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(currentLetter.char, fontSize = 60.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
            }'''
    elif exam_name == "KatinigExam3":
        new_exam += '''            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(currentLetter.emoji, fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(missingWord, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
                }
            }'''
            
    new_exam += '''
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().height(220.dp)
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
                            .height(100.dp)
                            .clickable {
                                if (selectedAnswer == null) {
                                    selectedAnswer = choice
'''
    if is_small_letter:
        new_exam += '''                                    isCorrect = choice == currentLetter.char.lowercase(java.util.Locale.getDefault())'''
    else:
        new_exam += '''                                    isCorrect = choice == currentLetter.char'''

    new_exam += '''
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(choice, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (selectedAnswer != null) {
                Button(
                    onClick = {
                        if (isCorrect == true) {
                            if (currentQuestionIdx >= 4) {
'''
    if exam_name == "KatinigExam1":
        new_exam += '''                                viewModel.exam1Passed.value = true'''
    elif exam_name == "KatinigExam2":
        new_exam += '''                                viewModel.exam2Passed.value = true'''
    elif exam_name == "KatinigExam3":
        new_exam += '''                                viewModel.exam3Passed.value = true'''

    new_exam += '''
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
    main = main.replace(exam_code, new_exam)
    print(f"Replaced {exam_name}")

replace_exam("KatinigExam1")
replace_exam("KatinigExam2", is_small_letter=True)
replace_exam("KatinigExam3", is_missing_letter=True)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(main)

