#!/bin/bash
sed -i '/val exam1Passed = MutableStateFlow(false)/i \
    val isLevel2Unlocked = userProgress.map { progress ->\
        val mastered = progress.masteredLetters.split(",")\
        lettersPatinig.all { it.char in mastered }\
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)\
\
    val isLevel3Unlocked = userProgress.map { progress ->\
        val mastered = progress.masteredLetters.split(",")\
        lettersKatinig.all { it.char in mastered }\
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)\
\
    val isPagsasanayUnlocked = userProgress.map { progress ->\
        val mastered = progress.masteredLetters.split(",")\
        lettersHiram.all { it.char in mastered }\
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)\
' app/src/main/java/com/example/ui/AppViewModel.kt
