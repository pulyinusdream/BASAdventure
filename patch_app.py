import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    main = f.read()

# Add UpdateCheckerDialog
app_pattern = '''    val progressState by viewModel.userProgress.collectAsState()
    
    Box('''

app_replacement = '''    val progressState by viewModel.userProgress.collectAsState()
    val updateInfo by viewModel.updateInfo.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        viewModel.checkForUpdates()
    }
    
    if (updateInfo != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { viewModel.dismissUpdate() },
            title = { Text("Bagong Update!", fontWeight = FontWeight.Bold) },
            text = { 
                Column {
                    Text("Mayroong bagong bersyon ng app (v${updateInfo!!.versionName}).")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Release Notes:", fontWeight = FontWeight.SemiBold)
                    Text(updateInfo!!.releaseNotes)
                }
            },
            confirmButton = {
                Button(onClick = { 
                    UpdateChecker.openDownloadLink(context, updateInfo!!.downloadUrl)
                    viewModel.dismissUpdate() 
                }) {
                    Text("I-download")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissUpdate() }) {
                    Text("Mamaya na")
                }
            }
        )
    }
    
    Box('''

if 'val updateInfo by viewModel.updateInfo.collectAsState()' not in main:
    main = main.replace(app_pattern, app_replacement)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(main)

print("Patched BasaAdventureApp")
