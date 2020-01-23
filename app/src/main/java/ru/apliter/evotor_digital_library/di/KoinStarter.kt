package ru.apliter.evotor_digital_library.di

import org.koin.core.context.startKoin
import ru.apliter.data.di.dataModule

object KoinStarter {
    fun startDi() {
        startKoin {
            modules(appModule)
            modules(dataModule)
        }
    }
}