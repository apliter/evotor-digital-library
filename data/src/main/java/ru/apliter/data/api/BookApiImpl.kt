package ru.apliter.data.api

import retrofit2.Retrofit
import ru.apliter.domain.api.BookApi

class BookApiImpl(private val retrofit: Retrofit) : BookApi<IRetrofitApi> {

    override fun getBookApi(): IRetrofitApi = retrofit.create(IRetrofitApi::class.java)

}