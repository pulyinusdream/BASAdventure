import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# Replace viewModel.letters with currentList
old_learn_screen = '''@Composable
fun LearnScreen(viewModel: AppViewModel, progress: UserProgress) {
    val selectedIdx by viewModel.selectedLetterIdx.collectAsState()
    val isMastered by viewModel.isLetterMastered.collectAsState()

    val currentLetter = viewModel.letters[selectedIdx]'''

new_learn_screen = '''@Composable
fun LearnScreen(viewModel: AppViewModel, progress: UserProgress) {
    val selectedIdx by viewModel.selectedLetterIdx.collectAsState()
    val isMastered by viewModel.isLetterMastered.collectAsState()
    val currentList by viewModel.currentLettersList.collectAsState()

    val currentLetter = if (selectedIdx in currentList.indices) currentList[selectedIdx] else currentList.firstOrNull() ?: viewModel.letters.first()'''

content = content.replace(old_learn_screen, new_learn_screen)

content = content.replace('itemsIndexed(viewModel.letters) { idx, item ->', 'itemsIndexed(currentList) { idx, item ->')

# Replace the next/prev wrap-around limits
content = re.sub(r'val prev = \(selectedIdx - 1 \+ 20\) % 20', 'val prev = (selectedIdx - 1 + currentList.size) % currentList.size', content)
content = re.sub(r'val next = \(selectedIdx \+ 1\) % 20', 'val next = (selectedIdx + 1) % currentList.size', content)

# Replace navigation from Home to Level1Menu
content = content.replace('onClick = { viewModel.navigateTo(Screen.Learn) },', 'onClick = { viewModel.navigateTo(Screen.Level1Menu) },')

# Add Level1Menu navigation case
old_nav = '''                        is Screen.Home -> HomeScreen(viewModel = viewModel, progress = progressState)
                        is Screen.Learn -> LearnScreen(viewModel = viewModel, progress = progressState)'''

new_nav = '''                        is Screen.Home -> HomeScreen(viewModel = viewModel, progress = progressState)
                        is Screen.Level1Menu -> Level1MenuScreen(viewModel = viewModel, progress = progressState)
                        is Screen.Learn -> LearnScreen(viewModel = viewModel, progress = progressState)'''

content = content.replace(old_nav, new_nav)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
