package ru.apliter.domain.repositories

import io.reactivex.Observable
import io.reactivex.Single
import ru.apliter.domain.entities.DomainBook
import java.util.*


interface BookRepository {

    fun getAllBooks(): Observable<List<DomainBook>>

    fun getBook(uuid: String): Single<DomainBook>

    fun rentBook(uuid: String, returnDate: Date): Single<Any>

    fun setBookRate(uuid: String, rate: Int): Single<Any>

    fun returnBook(uuid: String): Single<Any>
}