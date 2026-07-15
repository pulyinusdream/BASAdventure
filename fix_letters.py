import re

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'r') as f:
    content = f.read()

# Define the new lists
new_lists = '''    // Letters categorized
    val lettersPatinig = listOf(
        AbakadaLetter("A", "Aso", "Dog", "🐶", "Ang aso ay matapat na kaibigan."),
        AbakadaLetter("E", "Elepante", "Elephant", "🐘", "Napakalaki ng ilong ng elepante."),
        AbakadaLetter("I", "Isda", "Fish", "🐟", "Lumalangoy ang isda sa malinaw na tubig."),
        AbakadaLetter("O", "Orasan", "Clock", "⏰", "Ang orasan ay nagsasabi ng oras."),
        AbakadaLetter("U", "Ulan", "Rain", "🌧️", "Malamig ang patak ng ulan mula sa langit.")
    )

    val lettersKatinig = listOf(
        AbakadaLetter("M", "Manok", "Chicken", "🐔", "Tumatilaok ang manok tuwing umaga."),
        AbakadaLetter("S", "Saging", "Banana", "🍌", "Masustansya at matamis ang saging."),
        AbakadaLetter("B", "Baka", "Cow", "🐮", "Ang baka ay nagbibigay ng sariwang gatas."),
        AbakadaLetter("T", "Tinapay", "Bread", "🍞", "Masarap kainin ang mainit na tinapay."),
        AbakadaLetter("K", "Kalabaw", "Carabao", "🐃", "Ang kalabaw ay katulong sa bukid."),
        AbakadaLetter("L", "Laruan", "Toy", "🧸", "Masayang maglaro gamit ang laruan."),
        AbakadaLetter("Y", "Yoyo", "Yo-yo", "🪀", "Umiikot at bumababa ang yoyo ko."),
        AbakadaLetter("N", "Niyog", "Coconut", "🥥", "Masarap inumin ang sabaw ng niyog."),
        AbakadaLetter("G", "Gatas", "Milk", "🥛", "Uminom ng gatas para lumakas."),
        AbakadaLetter("R", "Relo", "Watch", "⌚", "May suot na bagong relo si Ate."),
        AbakadaLetter("P", "Pusa", "Cat", "🐱", "Mahilig maglaro ng sinulid ang pusa."),
        AbakadaLetter("Ng", "Ngipin", "Tooth", "🦷", "Sipilyuhin ang ngipin pagkatapos kumain."),
        AbakadaLetter("D", "Daga", "Mouse", "🐭", "Mabilis tumakbo ang maliit na daga."),
        AbakadaLetter("H", "Halaman", "Plant", "🌱", "Sariwa at berde ang halaman sa hardin."),
        AbakadaLetter("W", "Watawat", "Flag", "🇵🇭", "Igalang natin ang ating watawat.")
    )

    val lettersHiram = listOf(
        AbakadaLetter("C", "Carrot", "Carrot", "🥕", "Kumakain ng carrot ang kuneho."),
        AbakadaLetter("F", "Flower", "Flower", "🌻", "Napakaganda ng flower sa hardin."),
        AbakadaLetter("J", "Jeep", "Jeep", "🚙", "Sumakay kami ng jeep pauwi."),
        AbakadaLetter("Q", "Queso", "Cheese", "🧀", "Masarap ang queso sa pandesal."),
        AbakadaLetter("V", "Violin", "Violin", "🎻", "Tumutugtog ng violin si kuya."),
        AbakadaLetter("X", "Xylophone", "Xylophone", "🎹", "Tumutunog ang xylophone."),
        AbakadaLetter("Z", "Zebra", "Zebra", "🦓", "May guhit na itim at puti ang zebra."),
        AbakadaLetter("Ñ", "Niño", "Boy", "👦", "Mabait ang batang niño.")
    )

    val letters = lettersPatinig + lettersKatinig + lettersHiram
    val currentSubLevel = MutableStateFlow(1)

    val currentLettersList = currentSubLevel.map {
        when(it) {
            1 -> lettersPatinig
            2 -> lettersKatinig
            3 -> lettersHiram
            else -> lettersPatinig
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), lettersPatinig)
'''

# Find the old letters list to replace
old_letters_match = re.search(r'// List of 20 Abakada Letters.*?\)\n', content, re.DOTALL)
if old_letters_match:
    content = content.replace(old_letters_match.group(0), new_lists)

# Fix phonetic map to include new letters
old_phonetic = '''    private val phoneticMap = mapOf(
        "A" to "Ah", "B" to "Bah", "K" to "Kah", "D" to "Dah", "E" to "Eh",
        "G" to "Gah", "H" to "Hah", "I" to "Ee", "L" to "Lah", "M" to "Mah",
        "N" to "Nah", "Ng" to "Ngah", "O" to "Oh", "P" to "Pah", "R" to "Rah",
        "S" to "Sah", "T" to "Tah", "U" to "Oo", "W" to "Wah", "Y" to "Yah"
    )'''

new_phonetic = '''    private val phoneticMap = mapOf(
        "A" to "Ah", "B" to "Bah", "K" to "Kah", "D" to "Dah", "E" to "Eh",
        "G" to "Gah", "H" to "Hah", "I" to "Ee", "L" to "Lah", "M" to "Mah",
        "N" to "Nah", "Ng" to "Ngah", "O" to "Oh", "P" to "Pah", "R" to "Rah",
        "S" to "Sah", "T" to "Tah", "U" to "Oo", "W" to "Wah", "Y" to "Yah",
        "C" to "See", "F" to "Ef", "J" to "Jay", "Q" to "Kyu", "V" to "Vee",
        "X" to "Eks", "Z" to "Zee", "Ñ" to "Enye"
    )'''

content = content.replace(old_phonetic, new_phonetic)

# Fix isLetterMastered
old_is_letter_mastered = '''    val selectedLetterIdx = MutableStateFlow(0)
    val isLetterMastered = combine(selectedLetterIdx, userProgress) { idx, progress ->
        val letter = letters[idx].char
        progress.masteredLetters.split(",").contains(letter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)'''

new_is_letter_mastered = '''    val selectedLetterIdx = MutableStateFlow(0)
    val isLetterMastered = combine(selectedLetterIdx, userProgress, currentLettersList) { idx, progress, currentList ->
        if (idx in currentList.indices) {
            val letter = currentList[idx].char
            progress.masteredLetters.split(",").contains(letter)
        } else false
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)'''

content = content.replace(old_is_letter_mastered, new_is_letter_mastered)

# Update screen objects
old_screens = '''sealed interface Screen {
    object Landing : Screen
    object DataGathering : Screen
    object Home : Screen
    object Learn : Screen'''

new_screens = '''sealed interface Screen {
    object Landing : Screen
    object DataGathering : Screen
    object Home : Screen
    object Level1Menu : Screen
    object Learn : Screen'''

content = content.replace(old_screens, new_screens)

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'w') as f:
    f.write(content)
