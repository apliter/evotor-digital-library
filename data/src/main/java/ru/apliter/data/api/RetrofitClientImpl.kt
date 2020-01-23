package ru.apliter.data.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.apliter.domain.api.RetrofitClient


class RetrofitClientImpl : RetrofitClient<Retrofit> {

    override fun getRetrofitClient(): Retrofit = Retrofit.Builder().apply {
        baseUrl("base_url")
        addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd").create()))
    }.build()
}