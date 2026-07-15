import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    main = f.read()

pantig_old = '''        // Level Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFB74D)) // Orange for Pantig
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "LEVEL 2",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "PANTIG (SYLLABLES)",
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    fontSize = 24.sp
                )
            }
        }'''
pantig_new = '''        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFB74D))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "PANTIG (SYLLABLES)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }'''

if pantig_old in main:
    main = main.replace(pantig_old, pantig_new)
    print("Replaced Pantig")
else:
    print("Pantig not found")


salita_old = '''        // Level Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFBA68C8)) // Purple for Salita
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "LEVEL 3",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "MGA SALITA",
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    fontSize = 24.sp
                )
            }
        }'''
salita_new = '''        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFBA68C8))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "MGA SALITA",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }'''

if salita_old in main:
    main = main.replace(salita_old, salita_new)
    print("Replaced Salita")
else:
    print("Salita not found")


talata_old = '''        // Level Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF81C784)) // Green for Talata
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "LEVEL 4",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "TALATA (PARAGRAPHS)",
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    fontSize = 24.sp
                )
            }
        }'''
talata_new = '''        // Level Title with Back Button
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
        }'''

if talata_old in main:
    main = main.replace(talata_old, talata_new)
    print("Replaced Talata")
else:
    print("Talata not found")

pangungusap_old = '''        // Level Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF06292)) // Pink for Pangungusap
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "LEVEL 5",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "PANGUNGUSAP",
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    fontSize = 24.sp
                )
            }
        }'''
pangungusap_new = '''        // Level Title with Back Button
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
        }'''

if pangungusap_old in main:
    main = main.replace(pangungusap_old, pangungusap_new)
    print("Replaced Pangungusap")
else:
    print("Pangungusap not found")

kuwento_old = '''        // Level Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4FC3F7)) // Light blue for Kuwento
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "LEVEL 6",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "MGA KUWENTO",
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    fontSize = 24.sp
                )
            }
        }'''
kuwento_new = '''        // Level Title with Back Button
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
        }'''

if kuwento_old in main:
    main = main.replace(kuwento_old, kuwento_new)
    print("Replaced Kuwento")
else:
    print("Kuwento not found")

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(main)

