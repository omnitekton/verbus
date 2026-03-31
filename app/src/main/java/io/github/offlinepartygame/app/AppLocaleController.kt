package io.github.offlinepartygame.app

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import io.github.offlinepartygame.domain.model.AppLanguage

object AppLocaleController {
    fun applyLanguage(language: AppLanguage) {
        val locales = when (language) {
            AppLanguage.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
            AppLanguage.POLISH -> LocaleListCompat.forLanguageTags("pl")
            AppLanguage.ENGLISH -> LocaleListCompat.forLanguageTags("en")
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }
}
