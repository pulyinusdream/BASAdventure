import re

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'r') as f:
    app_vm = f.read()

# Fix syntax error in AppViewModel
app_vm = app_vm.replace("    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), lettersPatinig)\n    )", "    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), lettersPatinig)")

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'w') as f:
    f.write(app_vm)


with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    main_act = f.read()

# Fix LearnScreen signature
old_sig = '''fun LearnScreen(
    viewModel: AppViewModel,
    progress: UserProgress
) {
    val selectedIdx by viewModel.selectedLetterIdx.collectAsState()
    val isMastered by viewModel.isLetterMastered.collectAsState()
    val currentLetter = viewModel.letters[selectedIdx]'''

new_sig = '''fun LearnScreen(
    viewModel: AppViewModel,
    progress: UserProgress
) {
    val selectedIdx by viewModel.selectedLetterIdx.collectAsState()
    val isMastered by viewModel.isLetterMastered.collectAsState()
    val currentList by viewModel.currentLettersList.collectAsState()
    val currentLetter = if (selectedIdx in currentList.indices) currentList[selectedIdx] else currentList.firstOrNull() ?: viewModel.letters.first()'''

if old_sig in main_act:
    main_act = main_act.replace(old_sig, new_sig)
else:
    print("Could not find LearnScreen signature")

# Next and prev logic might have been wrongly replaced or is still wrong, but I already changed it, let's verify if currentList is available.
# But wait, in the navigation bar logic, I changed next/prev wrap-around logic but I did not change it to currentList if it wasn't already.

main_act = main_act.replace('val prev = (selectedIdx - 1 + 20) % 20', 'val prev = (selectedIdx - 1 + currentList.size) % currentList.size')
main_act = main_act.replace('val next = (selectedIdx + 1) % 20', 'val next = (selectedIdx + 1) % currentList.size')

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(main_act)
