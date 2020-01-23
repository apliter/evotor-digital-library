package ru.apliter.evotor_digital_library.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.apliter.data.api.BookApiImpl
import ru.apliter.data.api.IRetrofitApi
import ru.apliter.data.api.RetrofitClientImpl
import ru.apliter.data.repositories.BookRepositoryImpl
import ru.apliter.domain.api.IRetrofitClient
import ru.apliter.domain.repositories.IBookRepository
import ru.apliter.evotor_digital_library.ui.MainViewModel

val appModule = module {

    viewModel {
        MainViewModel(get())
    }

    single<IRetrofitClient<*>> {
        RetrofitClientImpl()
    }

    single<IRetrofitApi> {
        BookApiImpl(get()).getBookApi()
    }

    factory<IBookRepository> {
        BookRepositoryImpl(get())
    }
}