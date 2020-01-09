package ru.apliter.evotor_digital_library

import org.junit.Test
import ru.apliter.data.interactors.*
import ru.apliter.data.repositories.*
import ru.apliter.data.api.*
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun getBooksTest() {
        val bookRepository = BookRepositoryImpl(RetrofitClientImpl().getBookApi())
        val interactor = GetAllBooksInteractor(bookRepository)
        interactor.getAllBooks()
            .subscribe({
                it.forEach { book ->
                    println(book.bookId)
                }
            }, {

            })
    }
}
