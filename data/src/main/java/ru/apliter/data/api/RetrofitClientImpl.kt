package ru.apliter.data.api

import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.apliter.data.entities.DataBook
import ru.apliter.domain.api.RetrofitClientApi

class RetrofitClientImpl : RetrofitClientApi<Retrofit> {

    interface BookApi {

        @GET("api/v1/getAllBook")
        fun getAllBooks(): Observable<List<DataBook>>

        @GET("api/v1/getBook/{uuid}")
        fun getBook(@Path("uuid") uuid: String): Single<DataBook>

        @PUT("api/v1/book/{uuid}/rentBook/{returnDate}")
        fun rentBook(@Path("uuid") uuid: String, @Path("returnDate") returnDate:String): Single<Any>

        @PUT("api/v1/book/{uuid}/setRate/{rate}")
        fun setRate(@Path("uuid") uuid: String, @Path("rate") rate:Int): Single<Any>

        @PUT("api/v1/book/{uuid}/returnBook")
        fun returnBook(@Path("uuid") uuid: String): Single<Any>

    }

    fun getBookApi(): BookApi = getRetrofitClient().create(BookApi::class.java)

    override fun getRetrofitClient(): Retrofit = Retrofit.Builder().apply {
        baseUrl("base_URL")
        addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd").create()))
    }.build()

}