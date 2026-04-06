package io.github.verbus.data.preferences

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.verbus.domain.model.AppLanguage
import io.github.verbus.domain.model.AppSettings
import io.github.verbus.domain.model.SignalMethod
import io.github.verbus.domain.model.ThemeColorOption
import io.github.verbus.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.settingsDataStore by preferencesDataStore(
    name = "party_game_settings",
    corruptionHandler = ReplaceFileCorruptionHandler<Preferences> { emptyPreferences() },
)

class SettingsRepositoryImpl(
    private val appContext: Context,
) : SettingsRepository {
    override val settingsFlow: Flow<AppSettings> = appContext.settingsDataStore.data
        .catch { throwable ->
            if (throwable is IOException) {
                emit(emptyPreferences())
            } else {
                throw throwable
            }
        }
        .map { preferences ->
            AppSettings(
                signalMethod = SignalMethod.fromStorage(preferences[Keys.SIGNAL_METHOD]),
                language = AppLanguage.fromStorage(preferences[Keys.LANGUAGE]),
                topicsPerRound = preferences[Keys.TOPICS_PER_ROUND] ?: AppSettings.DEFAULT_TOPICS_PER_ROUND,
                topicDurationSec = preferences[Keys.TOPIC_DURATION_SEC] ?: AppSettings.DEFAULT_TOPIC_DURATION_SEC,
                preRoundCountdownSec = preferences[Keys.PRE_ROUND_COUNTDOWN_SEC]
                    ?: AppSettings.DEFAULT_PRE_ROUND_COUNTDOWN_SEC,
                timeoutMessageDurationSec = preferences[Keys.TIMEOUT_MESSAGE_DURATION_SEC]
                    ?: AppSettings.DEFAULT_TIMEOUT_MESSAGE_DURATION_SEC,
                hapticFeedbackEnabled = preferences[Keys.HAPTIC_FEEDBACK_ENABLED] ?: false,
                keepScreenAwake = preferences[Keys.KEEP_SCREEN_AWAKE] ?: true,
                backgroundColorPrimary = ThemeColorOption.fromStorage(
                    preferences[Keys.BACKGROUND_COLOR_PRIMARY],
                    AppSettings.DEFAULT_BACKGROUND_COLOR_PRIMARY,
                ),
                backgroundColorSecondary = ThemeColorOption.fromStorage(
                    preferences[Keys.BACKGROUND_COLOR_SECONDARY],
                    AppSettings.DEFAULT_BACKGROUND_COLOR_SECONDARY,
                ),
                fontColor = ThemeColorOption.fromStorage(
                    preferences[Keys.FONT_COLOR],
                    AppSettings.DEFAULT_FONT_COLOR,
                ),
                soundsEnabled = preferences[Keys.SOUNDS_ENABLED] ?: true,
                soundVolumeLevel = preferences[Keys.SOUND_VOLUME_LEVEL] ?: AppSettings.DEFAULT_SOUND_VOLUME_LEVEL,
            ).sanitized()
        }

    override suspend fun setSignalMethod(value: SignalMethod) {
        appContext.settingsDataStore.edit { it[Keys.SIGNAL_METHOD] = value.name }
    }

    override suspend fun setLanguage(value: AppLanguage) {
        appContext.settingsDataStore.edit { it[Keys.LANGUAGE] = value.name }
    }

    override suspend fun setTopicsPerRound(value: Int) {
        appContext.settingsDataStore.edit { it[Keys.TOPICS_PER_ROUND] = value }
    }

    override suspend fun setTopicDurationSec(value: Int) {
        appContext.settingsDataStore.edit { it[Keys.TOPIC_DURATION_SEC] = value }
    }

    override suspend fun setPreRoundCountdownSec(value: Int) {
        appContext.settingsDataStore.edit { it[Keys.PRE_ROUND_COUNTDOWN_SEC] = value }
    }

    override suspend fun setTimeoutMessageDurationSec(value: Int) {
        appContext.settingsDataStore.edit { it[Keys.TIMEOUT_MESSAGE_DURATION_SEC] = value }
    }

    override suspend fun setHapticFeedbackEnabled(enabled: Boolean) {
        appContext.settingsDataStore.edit { it[Keys.HAPTIC_FEEDBACK_ENABLED] = enabled }
    }

    override suspend fun setKeepScreenAwake(enabled: Boolean) {
        appContext.settingsDataStore.edit { it[Keys.KEEP_SCREEN_AWAKE] = enabled }
    }

    override suspend fun setBackgroundColorPrimary(value: ThemeColorOption) {
        appContext.settingsDataStore.edit { it[Keys.BACKGROUND_COLOR_PRIMARY] = value.storageKey }
    }

    override suspend fun setBackgroundColorSecondary(value: ThemeColorOption) {
        appContext.settingsDataStore.edit { it[Keys.BACKGROUND_COLOR_SECONDARY] = value.storageKey }
    }

    override suspend fun setFontColor(value: ThemeColorOption) {
        appContext.settingsDataStore.edit { it[Keys.FONT_COLOR] = value.storageKey }
    }

    override suspend fun setSoundsEnabled(enabled: Boolean) {
        appContext.settingsDataStore.edit { it[Keys.SOUNDS_ENABLED] = enabled }
    }

    override suspend fun setSoundVolumeLevel(value: Int) {
        appContext.settingsDataStore.edit { it[Keys.SOUND_VOLUME_LEVEL] = value }
    }

    private object Keys {
        val SIGNAL_METHOD = stringPreferencesKey("signal_method")
        val LANGUAGE = stringPreferencesKey("language")
        val TOPICS_PER_ROUND = intPreferencesKey("topics_per_round")
        val TOPIC_DURATION_SEC = intPreferencesKey("topic_duration_sec")
        val PRE_ROUND_COUNTDOWN_SEC = intPreferencesKey("pre_round_countdown_sec")
        val TIMEOUT_MESSAGE_DURATION_SEC = intPreferencesKey("timeout_message_duration_sec")
        val HAPTIC_FEEDBACK_ENABLED = booleanPreferencesKey("haptic_feedback_enabled")
        val KEEP_SCREEN_AWAKE = booleanPreferencesKey("keep_screen_awake")
        val BACKGROUND_COLOR_PRIMARY = stringPreferencesKey("background_color_primary")
        val BACKGROUND_COLOR_SECONDARY = stringPreferencesKey("background_color_secondary")
        val FONT_COLOR = stringPreferencesKey("font_color")
        val SOUNDS_ENABLED = booleanPreferencesKey("sounds_enabled")
        val SOUND_VOLUME_LEVEL = intPreferencesKey("sound_volume_level")
    }
}
