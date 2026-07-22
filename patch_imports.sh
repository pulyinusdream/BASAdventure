#!/bin/bash
sed -i 's/import android.os.Bundle/import android.os.Bundle\nimport androidx.activity.compose.rememberLauncherForActivityResult\nimport androidx.activity.result.contract.ActivityResultContracts/' app/src/main/java/com/example/MainActivity.kt
