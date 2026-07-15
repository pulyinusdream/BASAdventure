import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    main = f.read()

old_home = '''@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    progress: UserProgress
) {
    val context = LocalContext.current
    var showAvatarDialog by remember { mutableStateOf(false) }'''

new_home = '''@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    progress: UserProgress
) {
    val context = LocalContext.current
    var showAvatarDialog by remember { mutableStateOf(false) }
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

if old_home in main:
    main = main.replace(old_home, new_home)
    print("Fixed HomeScreen")
else:
    print("Not found")

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(main)

