#!/bin/bash
sed -i 's/audioPlayer?.release()/audioPlayer?.release()\n            mediaPlayer?.release()/' app/src/main/java/com/example/ui/AppViewModel.kt
