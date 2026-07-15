import re

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'r') as f:
    content = f.read()

old_toggle = '''    // Master/Unmaster a letter
    fun toggleLetterMastery(letterChar: String) {
        viewModelScope.launch {
            val currentList = userProgress.value.masteredLetters
                .split(",")
                .filter { it.isNotEmpty() }
                .toMutableSet()

            val isAdding = !currentList.contains(letterChar)
            if (isAdding) {
                currentList.add(letterChar)
                addStars(1)
                speakText("Mahusay! Natutunan mo ang letrang $letterChar!")
            } else {
                currentList.remove(letterChar)
                addStars(-1)
            }

            val updatedStr = currentList.joinToString(",")
            val updatedProgress = userProgress.value.copy(masteredLetters = updatedStr)
            repository.saveProgress(updatedProgress)
        }
    }'''

new_toggle = '''    // Master/Unmaster a letter
    fun toggleLetterMastery(letterChar: String) {
        viewModelScope.launch {
            val currentProgress = userProgress.value
            val currentList = currentProgress.masteredLetters
                .split(",")
                .filter { it.isNotEmpty() }
                .toMutableSet()

            val isAdding = !currentList.contains(letterChar)
            var newStars = currentProgress.stars
            if (isAdding) {
                currentList.add(letterChar)
                newStars = (newStars + 1).coerceAtLeast(0)
                speakText("Mahusay! Natutunan mo ang letrang $letterChar!")
            } else {
                currentList.remove(letterChar)
                newStars = (newStars - 1).coerceAtLeast(0)
            }

            val updatedStr = currentList.joinToString(",")
            val updatedProgress = currentProgress.copy(
                masteredLetters = updatedStr,
                stars = newStars
            )
            userProgress.value = updatedProgress // Immediate update to prevent races
            repository.saveProgress(updatedProgress)
        }
    }'''

content = content.replace(old_toggle, new_toggle)

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'w') as f:
    f.write(content)
