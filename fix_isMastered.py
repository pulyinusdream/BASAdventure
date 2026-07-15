import re

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'r') as f:
    content = f.read()

old_code = '''    val selectedLetterIdx = MutableStateFlow(0)
    val isLetterMastered = selectedLetterIdx.map { idx ->
        val letter = letters[idx].char
        userProgress.value.masteredLetters.split(",").contains(letter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)'''

new_code = '''    val selectedLetterIdx = MutableStateFlow(0)
    val isLetterMastered = combine(selectedLetterIdx, userProgress) { idx, progress ->
        val letter = letters[idx].char
        progress.masteredLetters.split(",").contains(letter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)'''

if old_code in content:
    content = content.replace(old_code, new_code)
    with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'w') as f:
        f.write(content)
    print("Replaced successfully.")
else:
    print("Old code not found.")
