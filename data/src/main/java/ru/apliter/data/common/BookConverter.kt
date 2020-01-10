package ru.apliter.data.common

import ru.apliter.data.entities.DataBook
import ru.apliter.domain.entities.DomainBook

object BookConverter {

    fun dataBookToDomainBook(dataBook: DataBook) = DomainBook(
        dataBook.bookId,
        dataBook.title,
        dataBook.author,
        dataBook.barcode,
        dataBook.rate,
        dataBook.preview,
        dataBook.counter,
        dataBook.vacant,
        dataBook.returnDate,
        dataBook.uri
    )

    fun domainBookToDataBook(domainBook: DomainBook) = DataBook(
        domainBook.bookId,
        domainBook.title,
        domainBook.author,
        domainBook.barcode,
        domainBook.rate,
        domainBook.preview,
        domainBook.counter,
        domainBook.isVacant,
        domainBook.returnDate,
        domainBook.uri
    )
}