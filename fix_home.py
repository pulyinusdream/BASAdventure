import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    main = f.read()

old_home_start = '''@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    progress: UserProgress
) {
    val showMenu = remember { mutableStateOf(false) }'''

new_home_start = '''@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    progress: UserProgress
) {
    val showMenu = remember { mutableStateOf(false) }
    var showAlphabetDialog by remember { mutableStateOf(false) }
    
    if (showAlphabetDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showAlphabetDialog = false },
            title = { Text("ALPHABETONG PILIPINO", fontWeight = FontWeight.Bold, color = PrimaryPurple) },
            text = { Text("Ipapakita dito ang arranged the Filipino Alphabet") },
            confirmButton = {
                Button(onClick = { 
                    showAlphabetDialog = false 
                    viewModel.navigateTo(Screen.Level1Menu)
                }) {
                    Text("Ipagpatuloy")
                }
            }
        )
    }'''

main = main.replace(old_home_start, new_home_start)

old_alphabet_card = '''                    BentoLevelCard(
                        title = "ALPHABETO",
                        subtitle = "Mga Titik",
                        icon = Icons.Default.MenuBook,
                        bgColor = Color(0xFF4BA3E3), // Blue
                        onClick = { viewModel.navigateTo(Screen.Level1Menu) },
                        modifier = Modifier.weight(1f).testTag("level_1_btn"),
                        isLocked = progress.unlockedLevel < 1
                    )'''

new_alphabet_card = '''                    BentoLevelCard(
                        title = "ALPHABETO",
                        subtitle = "Mga Titik",
                        icon = Icons.Default.MenuBook,
                        bgColor = Color(0xFF4BA3E3), // Blue
                        onClick = { showAlphabetDialog = true },
                        modifier = Modifier.weight(1f).testTag("level_1_btn"),
                        isLocked = progress.unlockedLevel < 1
                    )'''

main = main.replace(old_alphabet_card, new_alphabet_card)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(main)

print("Updated HomeScreen")
