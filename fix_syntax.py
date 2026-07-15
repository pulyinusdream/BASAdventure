import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    main = f.read()

# Fix Salita
salita_broken = '''        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFCE93D8))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "SALITA (WORDS)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) OnPrimaryContainerPurple else TextDark,
                        fontSize = 14.sp
                    )
                }
            }
        }'''

salita_fixed = '''        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFCE93D8))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "SALITA (WORDS)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) PrimaryPurple else Color.Transparent)
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextDark,
                            fontSize = 14.sp
                        )
                    }
                }
            }
'''

if salita_broken in main:
    main = main.replace(salita_broken, salita_fixed)
    print("Fixed Salita")

# Fix Talata
talata_broken = '''        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF81C784))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "TALATA (PARAGRAPHS)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Talata ${idx + 1}",
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) OnPrimaryContainerPurple else TextDark,
                        fontSize = 14.sp
                    )
                }
            }
        }'''

talata_fixed = '''        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF81C784))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "TALATA (PARAGRAPHS)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
        
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                paragraphs.forEachIndexed { idx, _ ->
                    val isSelected = currentIdx == idx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) PrimaryPurple else Color.Transparent)
                            .clickable { currentIdx = idx }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Talata ${idx + 1}",
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextDark,
                            fontSize = 14.sp
                        )
                    }
                }
            }
'''

if talata_broken in main:
    main = main.replace(talata_broken, talata_fixed)
    print("Fixed Talata")

# Fix Pangungusap
pangungusap_broken = '''        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF06292))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "PANGUNGUSAP",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Bilang ${idx + 1}",
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) OnPrimaryContainerPurple else TextDark,
                        fontSize = 14.sp
                    )
                }
            }
        }'''

pangungusap_fixed = '''        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF06292))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "PANGUNGUSAP",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sentences.size) { idx ->
                    val isSelected = currentIdx == idx
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) PrimaryPurple else Color.Transparent)
                            .clickable { currentIdx = idx }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Bilang ${idx + 1}",
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextDark,
                            fontSize = 14.sp
                        )
                    }
                }
            }
'''

if pangungusap_broken in main:
    main = main.replace(pangungusap_broken, pangungusap_fixed)
    print("Fixed Pangungusap")

# Fix Kuwento
kuwento_broken = '''        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4FC3F7))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "MGA KUWENTO",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Kuwento ${idx + 1}",
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) OnPrimaryContainerPurple else TextDark,
                        fontSize = 14.sp
                    )
                }
            }
        }'''

kuwento_fixed = '''        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4FC3F7))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "MGA KUWENTO",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stories.forEachIndexed { idx, _ ->
                    val isSelected = currentIdx == idx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) PrimaryPurple else Color.Transparent)
                            .clickable { currentIdx = idx }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Kuwento ${idx + 1}",
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextDark,
                            fontSize = 14.sp
                        )
                    }
                }
            }
'''

if kuwento_broken in main:
    main = main.replace(kuwento_broken, kuwento_fixed)
    print("Fixed Kuwento")

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(main)

