package ru.apliter.data.api

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.apliter.data.entities.DataBook

interface IRetrofitApi {

    @GET("api/v1/getAllBook")
    fun getAllBooks(): Observable<List<DataBook>>

    @GET("api/v1/getBook/{uuid}")
    fun getBook(@Path("uuid") uuid: String): Single<DataBook>

    @PUT("api/v1/book/{uuid}/rentBook/{returnDate}")
    fun rentBook(@Path("uuid") uuid: String, @Path("returnDate") returnDate: String): Single<Any>

    @PUT("api/v1/book/{uuid}/setRate/{rate}")
    fun setRate(@Path("uuid") uuid: String, @Path("rate") rate: Int): Single<Any>

    @PUT("api/v1/book/{uuid}/returnBook")
    fun returnBook(@Path("uuid") uuid: String): Single<Any>

}