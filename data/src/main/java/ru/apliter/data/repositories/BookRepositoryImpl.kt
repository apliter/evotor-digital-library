package ru.apliter.data.repositories

import io.reactivex.Observable
import io.reactivex.Single
import ru.apliter.data.api.RetrofitClientImpl
import ru.apliter.data.common.BookConverter
import ru.apliter.domain.entities.DomainBook
import ru.apliter.domain.repositories.BookRepository
import java.util.*

class BookRepositoryImpl(private val bookApi: RetrofitClientImpl.BookApi) : BookRepository {

    override fun getAllBooks(): Observable<List<DomainBook>> =
        bookApi.getAllBooks().map { networkBooks ->
            networkBooks.map { BookConverter.DataBookToDomainBook(it) }
        }

    override fun getBook(uuid: String): Single<DomainBook> =
        bookApi.getBook(uuid).map { BookConverter.DataBookToDomainBook(it) }

    override fun rentBook(uuid: String, returnDate: Date):Single<Any> =
        bookApi.rentBook(uuid,returnDate.time.toString())

    override fun setBookRate(uuid: String, rate: Int): Single<Any> =
        bookApi.setRate(uuid,rate)


    override fun returnBook(uuid: String): Single<Any> =
        bookApi.returnBook(uuid)



}