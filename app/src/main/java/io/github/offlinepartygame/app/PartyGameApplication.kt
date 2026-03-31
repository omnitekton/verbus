package io.github.offlinepartygame.app

import android.app.Application

class PartyGameApplication : Application() {
    val appContainer: AppContainer by lazy { AppContainer(this) }
}
