package ru.apliter.data.api

import retrofit2.Retrofit
import ru.apliter.domain.api.IBookApi
import ru.apliter.domain.api.IRetrofitClient

class BookApiImpl(private val retrofit: IRetrofitClient<Retrofit>) : IBookApi<IRetrofitApi> {

    override fun getBookApi(): IRetrofitApi = retrofit.getRetrofitClient().create(IRetrofitApi::class.java)

}