import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

new_progress_screen = '''@Composable
fun ProgressScreen(
    viewModel: AppViewModel,
    progress: UserProgress
) {
    val masteredLetters = progress.masteredLetters.split(",").filter { it.isNotEmpty() }
    val percent = (masteredLetters.size.toFloat() / 20f).coerceIn(0f, 1f)
    val gameRecords by viewModel.gameRecords.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("progress_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Star Scoreboard Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryContainerPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⭐️", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Aking Gantimpala",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = TextDark)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(BentoListenBg)
                        .border(1.dp, BentoListenBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = "⭐ ${progress.stars}", fontWeight = FontWeight.Black, color = BentoListenText)
                }
            }
        }

        item {
            // Student Information
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderLevel),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Impormasyon ng Estudyante",
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = "Pangalan: ${progress.firstName} ${progress.middleInitial} ${progress.surname}")
                    Text(text = "Baitang: ${progress.gradeLevel}")
                    Text(text = "Kasarian: ${progress.gender}")
                }
            }
        }

        item {
            // Circular Level Percentage Dial
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBgLevel),
                border = BorderStroke(1.dp, CardBorderLevel),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Kabuuang Antas ng Pagkatuto",
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Box(
                        modifier = Modifier.size(130.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(130.dp)) {
                            drawArc(
                                color = Color.White,
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 12.dp.toPx())
                            )
                            drawArc(
                                color = PrimaryPurple,
                                startAngle = -90f,
                                sweepAngle = percent * 360f,
                                useCenter = false,
                                style = Stroke(width = 12.dp.toPx())
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${(percent * 100).toInt()}%",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black, color = PrimaryPurple)
                            )
                            Text(
                                text = "${masteredLetters.size} / 20 Titik",
                                style = MaterialTheme.typography.labelSmall.copy(color = SubtitleGray)
                            )
                        }
                    }
                }
            }
        }
        
        item {
            Text(
                text = "Talaan ng Laro (Game Records)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        items(gameRecords) { record ->
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, CardBorderLevel),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = record.gameName, fontWeight = FontWeight.Bold, color = TextDark)
                        Text(text = "Puntos: ${record.score} / ${record.totalItems}", style = MaterialTheme.typography.bodySmall)
                    }
                    Text(text = "+${record.starsEarned} ⭐", color = BentoRewardsText, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}'''

# Replace from @Composable fun ProgressScreen to the end of its closing bracket
pattern = re.compile(r'@Composable\s*fun ProgressScreen\(\s*viewModel: AppViewModel,\s*progress: UserProgress\s*\)\s*\{.*?\n\}\n\n// --- SETTINGS SCREEN', re.DOTALL)
content = pattern.sub(new_progress_screen + '\n\n// --- SETTINGS SCREEN', content)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
