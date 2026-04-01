package io.github.offlinepartygame.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.offlinepartygame.app.ShakeSupportChecker
import io.github.offlinepartygame.domain.model.AppLanguage
import io.github.offlinepartygame.domain.model.AppSettings
import io.github.offlinepartygame.domain.model.SignalMethod
import io.github.offlinepartygame.domain.model.ThemeColorOption
import io.github.offlinepartygame.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OptionsUiState(
    val settings: AppSettings = AppSettings(),
    val isShakeSupported: Boolean = true,
    val isLoaded: Boolean = false,
)

class OptionsViewModel(
    private val settingsRepository: SettingsRepository,
    shakeSupportChecker: ShakeSupportChecker,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        OptionsUiState(isShakeSupported = shakeSupportChecker.isShakeSupported()),
    )
    val uiState: StateFlow<OptionsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                _uiState.update { current ->
                    current.copy(
                        settings = settings,
                        isLoaded = true,
                    )
                }
            }
        }
    }

    fun setSignalMethod(signalMethod: SignalMethod) {
        if (signalMethod == SignalMethod.SHAKE && !_uiState.value.isShakeSupported) return
        viewModelScope.launch { settingsRepository.setSignalMethod(signalMethod) }
    }

    fun cycleLanguage(delta: Int) {
        val current = _uiState.value.settings.language
        val values = AppLanguage.entries
        val currentIndex = values.indexOf(current).coerceAtLeast(0)
        val nextIndex = ((currentIndex + delta) % values.size + values.size) % values.size
        val next = values[nextIndex]
        viewModelScope.launch { settingsRepository.setLanguage(next) }
    }

    fun changeTopicsPerRound(delta: Int) {
        val nextValue = (_uiState.value.settings.topicsPerRound + delta).coerceIn(1, 100)
        viewModelScope.launch { settingsRepository.setTopicsPerRound(nextValue) }
    }

    fun changeTopicDuration(delta: Int) {
        val nextValue = (_uiState.value.settings.topicDurationSec + delta).coerceIn(5, 300)
        viewModelScope.launch { settingsRepository.setTopicDurationSec(nextValue) }
    }

    fun changePreRoundCountdown(delta: Int) {
        val nextValue = (_uiState.value.settings.preRoundCountdownSec + delta).coerceIn(0, 60)
        viewModelScope.launch { settingsRepository.setPreRoundCountdownSec(nextValue) }
    }

    fun changeTimeoutDuration(delta: Int) {
        val nextValue = (_uiState.value.settings.timeoutMessageDurationSec + delta).coerceIn(1, 30)
        viewModelScope.launch { settingsRepository.setTimeoutMessageDurationSec(nextValue) }
    }

    fun setHapticFeedback(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setHapticFeedbackEnabled(enabled) }
    }

    fun setKeepScreenAwake(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setKeepScreenAwake(enabled) }
    }

    fun cycleBackgroundColorPrimary(delta: Int) {
        val next = ThemeColorOption.next(
            current = _uiState.value.settings.backgroundColorPrimary,
            choices = ThemeColorOption.background1Defaults,
            delta = delta,
        )
        viewModelScope.launch { settingsRepository.setBackgroundColorPrimary(next) }
    }

    fun cycleBackgroundColorSecondary(delta: Int) {
        val next = ThemeColorOption.next(
            current = _uiState.value.settings.backgroundColorSecondary,
            choices = ThemeColorOption.background2Defaults,
            delta = delta,
        )
        viewModelScope.launch { settingsRepository.setBackgroundColorSecondary(next) }
    }

    fun cycleFontColor(delta: Int) {
        val next = ThemeColorOption.next(
            current = _uiState.value.settings.fontColor,
            choices = ThemeColorOption.fontDefaults,
            delta = delta,
        )
        viewModelScope.launch { settingsRepository.setFontColor(next) }
    }

    fun setSoundsEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setSoundsEnabled(enabled) }
    }

    fun changeSoundVolume(delta: Int) {
        val nextValue = (_uiState.value.settings.soundVolumeLevel + delta).coerceIn(1, 10)
        viewModelScope.launch { settingsRepository.setSoundVolumeLevel(nextValue) }
    }

    companion object {
        fun factory(
            settingsRepository: SettingsRepository,
            shakeSupportChecker: ShakeSupportChecker,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                OptionsViewModel(settingsRepository, shakeSupportChecker) as T
        }
    }
}