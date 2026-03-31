package io.github.offlinepartygame

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import io.github.offlinepartygame.app.PartyGameApplication
import io.github.offlinepartygame.ui.PartyGameApp
import io.github.offlinepartygame.ui.viewmodel.CatalogViewModel
import io.github.offlinepartygame.ui.viewmodel.OptionsViewModel
import io.github.offlinepartygame.ui.viewmodel.RoundViewModel

class MainActivity : AppCompatActivity() {
    private val appContainer by lazy {
        (application as PartyGameApplication).appContainer
    }

    private val catalogViewModel by viewModels<CatalogViewModel> {
        CatalogViewModel.factory(appContainer.contentRepository)
    }

    private val optionsViewModel by viewModels<OptionsViewModel> {
        OptionsViewModel.factory(
            settingsRepository = appContainer.settingsRepository,
            shakeSupportChecker = appContainer.shakeSupportChecker,
        )
    }

    private val roundViewModel by viewModels<RoundViewModel> {
        RoundViewModel.factory(
            roundCoordinator = appContainer.roundCoordinator,
            settingsRepository = appContainer.settingsRepository,
            shakeSupportChecker = appContainer.shakeSupportChecker,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PartyGameApp(
                catalogViewModel = catalogViewModel,
                optionsViewModel = optionsViewModel,
                roundViewModel = roundViewModel,
                soundPlayer = appContainer.soundPlayer,
                onExit = { finishAffinity() },
            )
        }
    }
}
