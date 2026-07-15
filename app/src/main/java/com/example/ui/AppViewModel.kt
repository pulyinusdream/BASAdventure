package com.example.ui

import android.app.Application
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.UserProgress
import com.example.data.UserProgressRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale
import com.example.AppUpdateInfo
import com.example.UpdateChecker

// Custom Navigation Screens
sealed interface Screen {
    object Landing : Screen
    object DataGathering : Screen
    object Home : Screen
    object Level1Menu : Screen
    object Learn : Screen
    object KatinigExam1 : Screen
    object KatinigExam2 : Screen
    object KatinigExam3 : Screen
    object Games : Screen
    object Quiz : Screen
    object Progress : Screen
    object Settings : Screen
    object Pantig : Screen
    object Salita : Screen
    object Talata : Screen
    object Pangungusap : Screen
    object Kuwento : Screen
}

// Learning Item Model
data class AbakadaLetter(
    val char: String,
    val word: String,
    val englishWord: String,
    val emoji: String, // Decorative fallback emoji
    val sampleSentence: String // Kids-friendly Tagalog sentence
)

// Memory Game Card Model
data class MemoryCard(
    val id: Int,
    val content: String, // Can be "A" or "Aso"
    val matchId: String, // Common key like "A"
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false,
    val isImage: Boolean = false // Whether card displays the illustration or letter
)

// Bubble Pop Item Model
data class Bubble(
    val id: Int,
    val char: String,
    val xOffset: Float, // horizontal position 0..1
    val yOffset: Float, // vertical position 0..1 (bubbles float upwards)
    val speed: Float,
    val size: Float = 60f
)

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserProgressRepository
    private var textToSpeech: TextToSpeech? = null
    private var isTtsReady = false
    
    // In-app Update Checker State
    private val _updateInfo = MutableStateFlow<AppUpdateInfo?>(null)
    val updateInfo: StateFlow<AppUpdateInfo?> = _updateInfo.asStateFlow()
    
    fun checkForUpdates() {
        viewModelScope.launch {
            val update = UpdateChecker.checkForUpdates()
            if (update != null) {
                _updateInfo.value = update
            }
        }
    }
    
    fun dismissUpdate() {
        _updateInfo.value = null
    }

        // Letters categorized
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

    // Abakada traditional phonetic pronunciation mappings
    private val phoneticMap = mapOf(
        "A" to "Ah", "B" to "Bah", "K" to "Kah", "D" to "Dah", "E" to "Eh",
        "G" to "Gah", "H" to "Hah", "I" to "Ee", "L" to "Lah", "M" to "Mah",
        "N" to "Nah", "Ng" to "Ngah", "O" to "Oh", "P" to "Pah", "R" to "Rah",
        "S" to "Sah", "T" to "Tah", "U" to "Oo", "W" to "Wah", "Y" to "Yah",
        "C" to "See", "F" to "Ef", "J" to "Jay", "Q" to "Kyu", "V" to "Vee",
        "X" to "Eks", "Z" to "Zee", "Ñ" to "Enye"
    )

    // UI state holder
    val currentScreen = MutableStateFlow<Screen>(Screen.Landing)
    

    // User progress state loaded from Room
    val userProgress = MutableStateFlow(UserProgress())

    val gameRecords = MutableStateFlow<List<com.example.data.GameRecord>>(emptyList())


    // Learning Screen State
    val selectedLetterIdx = MutableStateFlow(0)
    val isLetterMastered = combine(selectedLetterIdx, userProgress, currentLettersList) { idx, progress, currentList ->
        if (idx in currentList.indices) {
            val letter = currentList[idx].char
            progress.masteredLetters.split(",").contains(letter)
        } else false
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

        // Exam Progress State
    val exam1Passed = MutableStateFlow(false)
    val exam2Passed = MutableStateFlow(false)
    val exam3Passed = MutableStateFlow(false)

    // Selected Avatar
    val avatars = listOf("👦", "👧", "🦊", "🦁", "🐼")

    // --- GAME 1: MATCH THE PICTURE STATE ---
    val matchQuestion = MutableStateFlow<AbakadaLetter?>(null)
    val matchOptions = MutableStateFlow<List<AbakadaLetter>>(emptyList())
    val matchFeedback = MutableStateFlow<String?>(null) // "TAMA!" or "SUBUKAN MULI!"
    val isMatchCorrect = MutableStateFlow<Boolean?>(null)

    // --- GAME 2: LISTEN & CHOOSE STATE ---
    val listenTargetLetter = MutableStateFlow<AbakadaLetter?>(null)
    val listenOptions = MutableStateFlow<List<AbakadaLetter>>(emptyList())
    val listenFeedback = MutableStateFlow<String?>(null)
    val isListenCorrect = MutableStateFlow<Boolean?>(null)

    // --- GAME 3: BUBBLE POP STATE ---
    val bubbleTargetChar = MutableStateFlow("A")
    val activeBubbles = mutableStateListOf<Bubble>()
    val bubbleScore = MutableStateFlow(0)
    val bubbleTargetCount = 10 // score to win
    val isBubbleGameWon = MutableStateFlow(false)

    // --- GAME 4: MEMORY GAME STATE ---
    val memoryCards = mutableStateListOf<MemoryCard>()
    val memoryFirstSelectedCardIdx = MutableStateFlow<Int?>(null)
    val memoryMatchMoves = MutableStateFlow(0)
    val isMemoryGameWon = MutableStateFlow(false)

    // --- GENERAL QUIZ STATE ---
    val quizCurrentQuestionIdx = MutableStateFlow(0)
    val quizTotalQuestions = 5
    val quizCorrectAnswers = MutableStateFlow(0)
    val quizTargetLetter = MutableStateFlow<AbakadaLetter?>(null)
    val quizOptions = MutableStateFlow<List<AbakadaLetter>>(emptyList())
    val quizSelectedOption = MutableStateFlow<AbakadaLetter?>(null)
    val quizIsAnswered = MutableStateFlow(false)
    val isQuizFinished = MutableStateFlow(false)

    private var mediaPlayer: android.media.MediaPlayer? = null
    val isMusicPlaying = MutableStateFlow(false)




    fun initMusic() {

        if (mediaPlayer == null) {
            viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                try {
                    val mp = android.media.MediaPlayer().apply {
                        setDataSource(getApplication<Application>(), android.net.Uri.parse("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3"))
                        setAudioAttributes(
                            android.media.AudioAttributes.Builder()
                                .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        isLooping = true
                        prepare()
                    }
                    mediaPlayer = mp
                    if (userProgress.value.soundEnabled && (currentScreen.value is Screen.Landing || currentScreen.value is Screen.DataGathering)) {
                        mp.start()
                        isMusicPlaying.value = true
                    }
                } catch (e: Exception) {
                    Log.e("AppViewModel", "Failed to initialize background music: ${e.message}")
                }
            }
        }
    }

    fun toggleMusic() {
        val nextSoundState = !userProgress.value.soundEnabled
        viewModelScope.launch {
            val updated = userProgress.value.copy(soundEnabled = nextSoundState)
            userProgress.value = updated
            repository.saveProgress(updated)
            
            if (nextSoundState) {
                if (mediaPlayer == null) {
                    initMusic()
                } else if (currentScreen.value is Screen.Landing || currentScreen.value is Screen.DataGathering) {
                    mediaPlayer?.start()
                    isMusicPlaying.value = true
                }
                speakText("Naka-on ang tunog")
            } else {
                mediaPlayer?.pause()
                isMusicPlaying.value = false
            }
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        repository = UserProgressRepository(database.userProgressDao(), database.gameRecordDao())

        // Collect database progress
        viewModelScope.launch {
            repository.progress.collect { progress ->
                if (progress != null) {
                    userProgress.value = progress
                } else {
                    // Create default progress in Room
                    repository.saveProgress(UserProgress())
                }
                initMusic()
            }
        }

        // Initialize TextToSpeech with Tagalog / Filipino locale
        textToSpeech = TextToSpeech(getApplication<Application>()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("fil", "PH"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    textToSpeech?.setLanguage(Locale.getDefault())
                }
                isTtsReady = true
                
                // Welcome speech
                speakText("Maligayang pagdating sa Basa Adventure! Halika at matutong magbasa!")
            }
        }
    }

    // Navigation helper
    fun navigateTo(screen: Screen) {
        currentScreen.value = screen
        
        // Handle background music
        if (screen is Screen.Landing || screen is Screen.DataGathering) {
            if (userProgress.value.soundEnabled && mediaPlayer?.isPlaying == false) {
                mediaPlayer?.start()
                isMusicPlaying.value = true
            }
        } else {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                isMusicPlaying.value = false
            }
        }

        // Initialize appropriate states when navigating
        when (screen) {
            is Screen.Games -> {
                // reset or prepare games
            }
            is Screen.Quiz -> {
                startNewQuiz()
            }
            else -> {}
        }
    }

    // TTS Speak Helper
    fun speakText(text: String) {
        if (!isTtsReady || !userProgress.value.soundEnabled) return
        try {
            textToSpeech?.setSpeechRate(userProgress.value.ttsSpeed)
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "basadventure_tts")
        } catch (e: Exception) {
            Log.e("AppViewModel", "TTS Speaking failed: ${e.message}")
        }
    }

    fun speakLetterPhonetic(char: String) {
        val phStr = phoneticMap[char] ?: char
        speakText(phStr)
    }

    fun speakLetterSound(letter: AbakadaLetter) {
        val letterPh = phoneticMap[letter.char] ?: letter.char
        speakText(letterPh)
    }

    // Master/Unmaster a letter
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
    }

    fun updateAvatar(avatar: String) {
        viewModelScope.launch {
            val updated = userProgress.value.copy(selectedAvatar = avatar)
            userProgress.value = updated
            repository.saveProgress(updated)
            speakText("Pinalitan mo ang iyong karakter!")
        }
    }

    fun saveGatheredData(
        gradeLevel: String,
        surname: String,
        firstName: String,
        middleInitial: String,
        gender: String
    ) {
        viewModelScope.launch {
            val updated = userProgress.value.copy(
                gradeLevel = gradeLevel,
                surname = surname,
                firstName = firstName,
                middleInitial = middleInitial,
                gender = gender
            )
            userProgress.value = updated
            repository.saveProgress(updated)
            speakText("Salamat! Nai-save na ang iyong detalye. Simulan na natin ang pag-aaral!")
        }
    }

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch {
            val updated = userProgress.value.copy(soundEnabled = enabled)
            userProgress.value = updated
            repository.saveProgress(updated)
        }
    }

    fun updateTtsSpeed(speed: Float) {
        viewModelScope.launch {
            val updated = userProgress.value.copy(ttsSpeed = speed)
            userProgress.value = updated
            repository.saveProgress(updated)
        }
    }


    fun saveGameRecord(gameName: String, score: Int, totalItems: Int, starsEarned: Int) {
        viewModelScope.launch {
            repository.saveGameRecord(
                com.example.data.GameRecord(
                    gameName = gameName,
                    score = score,
                    totalItems = totalItems,
                    starsEarned = starsEarned
                )
            )
        }
    }

    fun addStars(amount: Int) {
        viewModelScope.launch {
            val currentProgress = userProgress.value
            val newStars = (currentProgress.stars + amount).coerceAtLeast(0)
            val updatedProgress = currentProgress.copy(stars = newStars)
            userProgress.value = updatedProgress // Immediate update to prevent races
            repository.saveProgress(updatedProgress)
        }
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.saveProgress(UserProgress(id = 1))
            selectedLetterIdx.value = 0
            speakText("Na-reset ang iyong laro. Simulan natin muli!")
        }
    }

    // --- GAME 1: MATCH THE PICTURE LOGIC ---
    fun initMatchPictureGame() {
        matchFeedback.value = null
        isMatchCorrect.value = null
        
        val randomTarget = letters.random()
        matchQuestion.value = randomTarget

        val others = letters.filter { it.char != randomTarget.char }.shuffled().take(3)
        matchOptions.value = (others + randomTarget).shuffled()
        
        speakText("Aling larawan ang nagsisimula sa letrang ${phoneticMap[randomTarget.char] ?: randomTarget.char}?")
    }

    fun checkMatchAnswer(selected: AbakadaLetter) {
        val target = matchQuestion.value ?: return
        if (selected.char == target.char) {
            isMatchCorrect.value = true
            matchFeedback.value = "TAMA!"
            addStars(2)
            saveGameRecord("Hulaan ang Larawan", 1, 1, 2)
            speakText("Tama! ${target.word} ay nagsisimula sa letrang ${phoneticMap[target.char] ?: target.char}!")
        } else {
            isMatchCorrect.value = false
            matchFeedback.value = "SUBUKAN MULI!"
            speakText("Mali, subukan muli!")
        }
    }

    // --- GAME 2: LISTEN AND CHOOSE LOGIC ---
    fun initListenAndChooseGame() {
        listenFeedback.value = null
        isListenCorrect.value = null

        val randomTarget = letters.random()
        listenTargetLetter.value = randomTarget

        val others = letters.filter { it.char != randomTarget.char }.shuffled().take(3)
        listenOptions.value = (others + randomTarget).shuffled()

        playListenTargetSound()
    }

    fun playListenTargetSound() {
        val target = listenTargetLetter.value ?: return
        speakText("Hanapin ang letrang ${phoneticMap[target.char] ?: target.char}")
    }

    fun checkListenAnswer(selected: AbakadaLetter) {
        val target = listenTargetLetter.value ?: return
        if (selected.char == target.char) {
            isListenCorrect.value = true
            listenFeedback.value = "TAMA!"
            addStars(2)
            saveGameRecord("Makinig at Piliin", 1, 1, 2)
            speakText("Tama! Iyan ang letrang ${phoneticMap[target.char] ?: target.char}!")
        } else {
            isListenCorrect.value = false
            listenFeedback.value = "MALI"
            speakText("Oops! Hindi iyan, subukan muli!")
        }
    }

    // --- GAME 3: BUBBLE POP LOGIC ---
    fun initBubblePopGame() {
        isBubbleGameWon.value = false
        bubbleScore.value = 0
        activeBubbles.clear()

        val randomTarget = letters.random().char
        bubbleTargetChar.value = randomTarget

        // Generate initial bubbles
        for (i in 0 until 6) {
            spawnBubble()
        }

        speakText("Sabugin ang mga bula na may letrang ${phoneticMap[randomTarget] ?: randomTarget}!")
        startBubbleEngine()
    }

    private fun spawnBubble() {
        val isTarget = (0..100).random() < 35 // 35% chance to be target letter
        val letterChar = if (isTarget) {
            bubbleTargetChar.value
        } else {
            letters.filter { it.char != bubbleTargetChar.value }.random().char
        }

        activeBubbles.add(
            Bubble(
                id = (0..1000000).random(),
                char = letterChar,
                xOffset = (10..90).random() / 100f,
                yOffset = 1.1f, // starts from bottom of viewport
                speed = (15..35).random() / 1000f,
                size = (50..75).random().toFloat()
            )
        )
    }

    private fun startBubbleEngine() {
        viewModelScope.launch {
            while (currentScreen.value is Screen.Games && !isBubbleGameWon.value) {
                delay(100)
                
                // Update existing bubble heights
                val iterator = activeBubbles.listIterator()
                while (iterator.hasNext()) {
                    val b = iterator.next()
                    val newY = b.yOffset - b.speed
                    if (newY < -0.1f) {
                        iterator.remove() // remove off-screen bubble
                    } else {
                        iterator.set(b.copy(yOffset = newY))
                    }
                }

                // Keep 6 bubbles active
                while (activeBubbles.size < 6 && !isBubbleGameWon.value) {
                    spawnBubble()
                }
            }
        }
    }

    fun popBubble(bubble: Bubble) {
        activeBubbles.remove(bubble)
        if (bubble.char == bubbleTargetChar.value) {
            bubbleScore.value += 1
            speakText("Pak!")
            if (bubbleScore.value >= bubbleTargetCount) {
                isBubbleGameWon.value = true
                addStars(5)
                saveGameRecord("Putukin ang Bula", bubbleScore.value, bubbleTargetCount, 5)
                speakText("Magaling! Nakolekta mo ang lahat ng mga bula ng letrang ${phoneticMap[bubbleTargetChar.value] ?: bubbleTargetChar.value}!")
            }
        } else {
            // Negative feedback
            speakText("Hala!")
            bubbleScore.value = (bubbleScore.value - 1).coerceAtLeast(0)
        }
    }

    // --- GAME 4: MEMORY GAME LOGIC ---
    fun initMemoryGame() {
        isMemoryGameWon.value = false
        memoryMatchMoves.value = 0
        memoryFirstSelectedCardIdx.value = null
        memoryCards.clear()

        // Choose 4 random letters
        val selectedLetters = letters.shuffled().take(4)
        val tempCards = mutableListOf<MemoryCard>()

        selectedLetters.forEachIndexed { index, letter ->
            // Card 1: Letter
            tempCards.add(
                MemoryCard(
                    id = index * 2,
                    content = letter.char,
                    matchId = letter.char,
                    isImage = false
                )
            )
            // Card 2: Corresponding Word
            tempCards.add(
                MemoryCard(
                    id = index * 2 + 1,
                    content = letter.word,
                    matchId = letter.char,
                    isImage = true
                )
            )
        }

        // Shuffle and populate state list
        memoryCards.addAll(tempCards.shuffled())
        speakText("Ipares ang letrang tunog sa tamang salita at larawan!")
    }

    fun selectMemoryCard(clickedIdx: Int) {
        val clickedCard = memoryCards[clickedIdx]
        if (clickedCard.isFaceUp || clickedCard.isMatched) return

        // Flip up
        memoryCards[clickedIdx] = clickedCard.copy(isFaceUp = true)

        val firstIdx = memoryFirstSelectedCardIdx.value
        if (firstIdx == null) {
            // First card chosen
            memoryFirstSelectedCardIdx.value = clickedIdx
            // Speak the card name
            if (clickedCard.isImage) speakText(clickedCard.content) else speakLetterPhonetic(clickedCard.content)
        } else {
            // Second card chosen
            memoryMatchMoves.value += 1
            val firstCard = memoryCards[firstIdx]
            
            if (clickedCard.isImage) speakText(clickedCard.content) else speakLetterPhonetic(clickedCard.content)

            if (firstCard.matchId == clickedCard.matchId) {
                // MATCH!
                viewModelScope.launch {
                    delay(500)
                    memoryCards[firstIdx] = firstCard.copy(isMatched = true)
                    memoryCards[clickedIdx] = clickedCard.copy(isMatched = true)
                    
                    speakText("Nahanap mo ang kapareha!")

                    // Check if all matched
                    if (memoryCards.all { it.isMatched }) {
                        isMemoryGameWon.value = true
                        addStars(5)
                        saveGameRecord("Tugma-tugma", 1, 1, 5)
                        speakText("Kamangha-manghang laro! Nahanap mo ang lahat ng magkapareha!")
                    }
                }
            } else {
                // NO MATCH! Flip them back down
                viewModelScope.launch {
                    delay(1200)
                    if (clickedIdx < memoryCards.size && firstIdx < memoryCards.size) {
                        memoryCards[firstIdx] = memoryCards[firstIdx].copy(isFaceUp = false)
                        memoryCards[clickedIdx] = memoryCards[clickedIdx].copy(isFaceUp = false)
                    }
                }
            }
            memoryFirstSelectedCardIdx.value = null
        }
    }

    // --- QUIZ LOGIC ---
    fun startNewQuiz() {
        quizCurrentQuestionIdx.value = 0
        quizCorrectAnswers.value = 0
        isQuizFinished.value = false
        generateQuizQuestion()
    }

    fun generateQuizQuestion() {
        quizSelectedOption.value = null
        quizIsAnswered.value = false

        val target = letters.random()
        quizTargetLetter.value = target

        val others = letters.filter { it.char != target.char }.shuffled().take(3)
        quizOptions.value = (others + target).shuffled()

        speakText("Alin ang letrang ${phoneticMap[target.char] ?: target.char} para sa salitang ${target.word}?")
    }

    fun selectQuizOption(selected: AbakadaLetter) {
        if (quizIsAnswered.value) return
        quizSelectedOption.value = selected
        quizIsAnswered.value = true

        val target = quizTargetLetter.value ?: return
        if (selected.char == target.char) {
            quizCorrectAnswers.value += 1
            speakText("Tama! Napakahusay!")
        } else {
            speakText("Mali, ito ay letrang ${phoneticMap[target.char] ?: target.char}.")
        }
    }

    fun unlockLevel(level: Int) {
        viewModelScope.launch {
            if (level > userProgress.value.unlockedLevel) {
                repository.saveProgress(userProgress.value.copy(unlockedLevel = level))
            }
        }
    }

    fun nextQuizQuestion() {
        val currentIdx = quizCurrentQuestionIdx.value
        if (currentIdx + 1 < quizTotalQuestions) {
            quizCurrentQuestionIdx.value = currentIdx + 1
            generateQuizQuestion()
        } else {
            isQuizFinished.value = true
            val correct = quizCorrectAnswers.value
            addStars(correct * 2)
            saveGameRecord("Pagsusulit", correct, quizTotalQuestions, correct * 2)
            
            // Check if level should be unlocked (e.g. scored 4 or higher out of 5)
            if (correct >= 4) {
                // Since this quiz is for Alpabeto (Level 1), we unlock Level 2 (Pantig)
                unlockLevel(2)
                speakText("Natapos ang pagsusulit! Nakakuha ka ng $correct tamang sagot sa $quizTotalQuestions. Bukas na ang antas ng Pantig!")
            } else {
                speakText("Natapos ang pagsusulit! Nakakuha ka ng $correct tamang sagot. Kumuha ng 4 pataas para mabuksan ang antas ng Pantig.")
            }
        }
    }

    override fun onCleared() {
        textToSpeech?.shutdown()
        try {
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            Log.e("AppViewModel", "Failed to release MediaPlayer: ${e.message}")
        }
        super.onCleared()
    }
}
