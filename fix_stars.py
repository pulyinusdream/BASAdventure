import re

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'r') as f:
    content = f.read()

old_stars = '''    fun addStars(amount: Int) {

        viewModelScope.launch {
            val newStars = (userProgress.value.stars + amount).coerceAtLeast(0)
            repository.saveProgress(userProgress.value.copy(stars = newStars))
        }
    }'''

new_stars = '''    fun addStars(amount: Int) {
        viewModelScope.launch {
            val currentProgress = userProgress.value
            val newStars = (currentProgress.stars + amount).coerceAtLeast(0)
            val updatedProgress = currentProgress.copy(stars = newStars)
            userProgress.value = updatedProgress // Immediate update to prevent races
            repository.saveProgress(updatedProgress)
        }
    }'''

content = content.replace(old_stars, new_stars)

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'w') as f:
    f.write(content)
