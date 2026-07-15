import re

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'r') as f:
    content = f.read()

def replace_saving(old, new):
    global content
    content = content.replace(old, new)

replace_saving(
    'repository.saveProgress(userProgress.value.copy(selectedAvatar = avatar))',
    'val updated = userProgress.value.copy(selectedAvatar = avatar)\n            userProgress.value = updated\n            repository.saveProgress(updated)'
)

replace_saving(
    'repository.saveProgress(userProgress.value.copy(soundEnabled = enabled))',
    'val updated = userProgress.value.copy(soundEnabled = enabled)\n            userProgress.value = updated\n            repository.saveProgress(updated)'
)

replace_saving(
    'repository.saveProgress(userProgress.value.copy(ttsSpeed = speed))',
    'val updated = userProgress.value.copy(ttsSpeed = speed)\n            userProgress.value = updated\n            repository.saveProgress(updated)'
)

replace_saving(
    '''val updatedProgress = userProgress.value.copy(
            gradeLevel = gradeLevel,
            surname = surname,
            firstName = firstName,
            middleInitial = middleInitial,
            gender = gender
        )
        viewModelScope.launch {
            repository.saveProgress(updatedProgress)
        }''',
    '''val updatedProgress = userProgress.value.copy(
            gradeLevel = gradeLevel,
            surname = surname,
            firstName = firstName,
            middleInitial = middleInitial,
            gender = gender
        )
        userProgress.value = updatedProgress
        viewModelScope.launch {
            repository.saveProgress(updatedProgress)
        }'''
)

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'w') as f:
    f.write(content)
