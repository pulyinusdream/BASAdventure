import re

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'r') as f:
    main = f.read()

import_pattern = 'import java.util.Locale'
import_replacement = '''import java.util.Locale
import com.example.AppUpdateInfo
import com.example.UpdateChecker'''

if 'import com.example.AppUpdateInfo' not in main:
    main = main.replace(import_pattern, import_replacement)

# Add state variables
state_pattern = '    private var isTtsReady = false'
state_replacement = '''    private var isTtsReady = false
    
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
    }'''

main = main.replace(state_pattern, state_replacement)

with open('app/src/main/java/com/example/ui/AppViewModel.kt', 'w') as f:
    f.write(main)

print("Patched AppViewModel")
