with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

level1_menu = '''
@Composable
fun Level1MenuScreen(viewModel: AppViewModel, progress: UserProgress) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
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
}
'''

content += level1_menu

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
