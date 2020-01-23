package ru.apliter.data.di

import org.koin.dsl.module
import ru.apliter.data.api.BookApiImpl
import ru.apliter.data.api.RetrofitClientImpl
import ru.apliter.data.repositories.BookRepositoryImpl
import ru.apliter.domain.repositories.BookRepository

val dataModule = module {

    //RetrofitClient<Retrofit>
    single {
        RetrofitClientImpl()
    }

    // Retrofit
    single {
        val client: RetrofitClientImpl = get()
        client.getRetrofitClient()
    }

    //IRetrofitApi
    single {
        BookApiImpl(get()).getBookApi()
    }

    factory<BookRepository> {
        BookRepositoryImpl(get())
    }
}