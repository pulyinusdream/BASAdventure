#!/bin/bash
sed -i 's/is Screen.Kuwento -> KuwentoScreen(viewModel = viewModel)/is Screen.Kuwento -> KuwentoScreen(viewModel = viewModel)\n                        is Screen.VoiceRecording -> VoiceRecordingScreen(viewModel = viewModel)/' app/src/main/java/com/example/MainActivity.kt
