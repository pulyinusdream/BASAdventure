import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

new_card_def = '''
@Composable
fun MenuRowCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bgColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
            // Decorative background circles
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.15f),
                    radius = size.width * 0.4f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.95f, size.height * 0.8f)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    radius = size.width * 0.25f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.7f, 0f)
                )
            }
            
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon Box
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                // Texts
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 22.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                // Arrow
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
'''

old_screen = '''@Composable
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

new_screen = '''@Composable
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MenuRowCard(
                title = "I. MGA PATINIG",
                subtitle = "A E I O U",
                icon = Icons.Default.FontDownload,
                bgColor = Color(0xFFF06292),
                onClick = { 
                    viewModel.currentSubLevel.value = 1
                    viewModel.selectedLetterIdx.value = 0
                    viewModel.navigateTo(Screen.Learn) 
                }
            )

            MenuRowCard(
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

            MenuRowCard(
                title = "III. MGA HIRAM NA LETRA",
                subtitle = "C F J Q V X Z Ñ",
                icon = Icons.Default.Public,
                bgColor = Color(0xFF4BB543),
                onClick = { 
                    viewModel.currentSubLevel.value = 3
                    viewModel.selectedLetterIdx.value = 0
                    viewModel.navigateTo(Screen.Learn) 
                }
            )
        }
    }
}'''

if old_screen in content:
    content = content.replace(old_screen, new_card_def + '\n' + new_screen)
    with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
        f.write(content)
    print("Replaced screen.")
else:
    print("Screen code not found.")
