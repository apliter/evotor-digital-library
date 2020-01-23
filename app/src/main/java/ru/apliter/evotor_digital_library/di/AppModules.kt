package ru.apliter.evotor_digital_library.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.apliter.evotor_digital_library.ui.main.MainViewModel

val appModule = module {

    viewModel {
        MainViewModel(get())
    }

}