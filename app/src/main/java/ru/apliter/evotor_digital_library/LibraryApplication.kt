package ru.apliter.evotor_digital_library

import android.app.Application
import ru.apliter.evotor_digital_library.di.KoinStarter

class LibraryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinStarter.startDi()
    }
}