package ru.apliter.evotor_digital_library

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import ru.apliter.data.api.RetrofitClientImpl
import ru.apliter.data.interactors.GetAllBooksInteractor
import ru.apliter.data.repositories.BookRepositoryImpl

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ru.apliter.evotor_digital_library", appContext.packageName)
    }

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
