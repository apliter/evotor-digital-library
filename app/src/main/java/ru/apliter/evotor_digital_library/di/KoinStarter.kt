package ru.apliter.evotor_digital_library.di

import org.koin.core.context.startKoin

object KoinStarter {
    fun startDi() {
        startKoin {
            modules(appModule)
        }
    }
}