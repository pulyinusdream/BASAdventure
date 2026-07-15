import re

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'r') as f:
    content = f.read()

# Change speakLetterAndWord to speakLetterSound
old_speak = '''    fun speakLetterAndWord(letter: AbakadaLetter) {
        val letterPh = phoneticMap[letter.char] ?: letter.char
        speakText("$letterPh, para sa ${letter.word}. ${letter.sampleSentence}")
    }'''

new_speak = '''    fun speakLetterSound(letter: AbakadaLetter) {
        val letterPh = phoneticMap[letter.char] ?: letter.char
        speakText(letterPh)
    }'''

if old_speak in content:
    content = content.replace(old_speak, new_speak)

# Add unlock states for Katinig exams
new_state = '''    // Exam Progress State
    val exam1Passed = MutableStateFlow(false)
    val exam2Passed = MutableStateFlow(false)
    val exam3Passed = MutableStateFlow(false)
'''

if 'val exam1Passed' not in content:
    content = content.replace('// Selected Avatar', new_state + '\n    // Selected Avatar')

# Add screens
old_screens = '''sealed interface Screen {
    object Landing : Screen
    object DataGathering : Screen
    object Home : Screen
    object Level1Menu : Screen
    object Learn : Screen'''

new_screens = '''sealed interface Screen {
    object Landing : Screen
    object DataGathering : Screen
    object Home : Screen
    object Level1Menu : Screen
    object Learn : Screen
    object KatinigExam1 : Screen
    object KatinigExam2 : Screen
    object KatinigExam3 : Screen'''

if 'object KatinigExam1' not in content:
    content = content.replace(old_screens, new_screens)

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'w') as f:
    f.write(content)
print("Updated ViewModel")

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    main = f.read()

# Remove the word and sentence from LearnScreen
old_learn_content = '''                    // Word Pronunciation Display
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFFFEBEE))
                            .clickable { viewModel.speakLetterAndWord(currentLetter) }
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = currentLetter.word,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD32F2F)
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(${currentLetter.englishWord})",
                            style = MaterialTheme.typography.bodyMedium.copy(color = SubtitleGray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Pakinggan ang salita",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Example Sentence
                    Text(
                        text = currentLetter.sampleSentence,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = TextDark,
                            lineHeight = 22.sp
                        )
                    )'''

new_learn_content = '''                    // Play Sound Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE8F5E9))
                            .clickable { viewModel.speakLetterSound(currentLetter) }
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Pakinggan ang tunog",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Pakinggan",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        )
                    }'''

if old_learn_content in main:
    main = main.replace(old_learn_content, new_learn_content)
else:
    print("Old learn content not found, might have been altered")

# Find Level1MenuScreen and add Katinig Exams
old_katinig_card = '''            MenuRowCard(
                title = "II. MGA KATINIG",
                subtitle = "Mga katinig sa alpabetong Pilipino",
                icon = Icons.Default.SortByAlpha,
                bgColor = Color(0xFF4BA3E3),
                onClick = { 
                    viewModel.currentSubLevel.value = 2
                    viewModel.selectedLetterIdx.value = 0
                    viewModel.navigateTo(Screen.Learn) 
                }
            )'''

new_katinig_card = '''            MenuRowCard(
                title = "II. MGA KATINIG",
                subtitle = "Mga katinig sa alpabetong Pilipino",
                icon = Icons.Default.SortByAlpha,
                bgColor = Color(0xFF4BA3E3),
                onClick = { 
                    viewModel.currentSubLevel.value = 2
                    viewModel.selectedLetterIdx.value = 0
                    viewModel.navigateTo(Screen.Learn) 
                }
            )
            
            // Pagsasanay para sa Katinig
            Text("PAGSASANAY (MGA KATINIG)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            MenuRowCard(
                title = "Pagsasanay 1",
                subtitle = "Piliin ang Tamang Titik",
                icon = Icons.Default.Quiz,
                bgColor = Color(0xFF9061F9),
                onClick = { 
                    viewModel.navigateTo(Screen.KatinigExam1)
                }
            )

            val exam1Passed by viewModel.exam1Passed.collectAsState()
            val exam2Passed by viewModel.exam2Passed.collectAsState()

            if (exam1Passed) {
                MenuRowCard(
                    title = "Pagsasanay 2",
                    subtitle = "Itugma ang malaking titik sa maliit",
                    icon = Icons.Default.JoinInner,
                    bgColor = Color(0xFF9061F9),
                    onClick = { viewModel.navigateTo(Screen.KatinigExam2) }
                )
            } else {
                MenuRowCard(
                    title = "Pagsasanay 2 (Naka-lock)",
                    subtitle = "Tapusin muna ang Pagsasanay 1",
                    icon = Icons.Default.Lock,
                    bgColor = Color.Gray,
                    onClick = { /* Locked */ }
                )
            }

            if (exam2Passed) {
                MenuRowCard(
                    title = "Pagsasanay 3",
                    subtitle = "Hanapin ang nawawalang titik",
                    icon = Icons.Default.Search,
                    bgColor = Color(0xFF9061F9),
                    onClick = { viewModel.navigateTo(Screen.KatinigExam3) }
                )
            } else {
                MenuRowCard(
                    title = "Pagsasanay 3 (Naka-lock)",
                    subtitle = "Tapusin muna ang Pagsasanay 2",
                    icon = Icons.Default.Lock,
                    bgColor = Color.Gray,
                    onClick = { /* Locked */ }
                )
            }'''

if old_katinig_card in main:
    main = main.replace(old_katinig_card, new_katinig_card)
else:
    print("Katinig card not found")

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(main)
print("Updated MainActivity")
