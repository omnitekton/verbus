package io.github.offlinepartygame.app

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import io.github.offlinepartygame.domain.model.AppLanguage

object AppLocaleController {
    fun applyLanguage(language: AppLanguage) {
        val target = when (language) {
            AppLanguage.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
            AppLanguage.POLISH -> LocaleListCompat.forLanguageTags("pl")
            AppLanguage.ENGLISH -> LocaleListCompat.forLanguageTags("en")
        }

        val current = AppCompatDelegate.getApplicationLocales()
        if (current.toLanguageTags() == target.toLanguageTags()) return

        AppCompatDelegate.setApplicationLocales(target)
    }
}
