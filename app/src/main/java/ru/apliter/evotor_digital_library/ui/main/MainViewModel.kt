package ru.apliter.evotor_digital_library.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.apliter.data.api.RetrofitClientImpl
import ru.apliter.data.common.BookConverter
import ru.apliter.data.entities.DataBook
import ru.apliter.data.repositories.BookRepositoryImpl
import ru.evotor.devices.commons.DeviceServiceConnector
import ru.evotor.devices.commons.printer.PrinterDocument
import ru.evotor.devices.commons.printer.printable.PrintableBarcode
import ru.evotor.devices.commons.printer.printable.PrintableText
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel : ViewModel() {

    private val bookRepository = BookRepositoryImpl(RetrofitClientImpl().getBookApi())

    private val bookListLiveData = MutableLiveData<List<DataBook>>()
    private val bookLiveData = MutableLiveData<DataBook>()
    private val bookRentStatusData = MutableLiveData<String>()
    private val bookReturnStatusData = MutableLiveData<String>()

    private val errorGetBooksData = MutableLiveData<Throwable>()
    private val errorGetBookData = MutableLiveData<Throwable>()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val loading = MutableLiveData<Boolean>()

    fun startGettingBookList() {
        compositeDisposable.clear()
        compositeDisposable.add(
            bookRepository
                .getAllBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    loading.value = true
                }
                .doOnTerminate {
                    loading.value = false
                }
                .subscribe({ domainBooks ->
                    this.bookListLiveData.value =
                        domainBooks.map { BookConverter.domainBookToDataBook(it) }
                }, { error ->
                    this.errorGetBooksData.value = error
                    error.printStackTrace()
                })
        )
    }

    fun startGettingBook(bookId: String) {
        compositeDisposable.clear()
        compositeDisposable.add(
            bookRepository
                .getBook(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    loading.value = true
                }
                .subscribe({ domainBook ->
                    this.bookLiveData.value = BookConverter.domainBookToDataBook(domainBook)
                    loading.value = false
                }, { error ->
                    errorGetBookData.value = error
                    error.printStackTrace()
                })
        )
    }

    fun rentBook() {
        compositeDisposable.clear()
        bookLiveData.value?.let { book ->
            val returnDate = Date()
            if (!book.vacant) {
                returnDate.time =
                    book.returnDate?.time!!.plus(1209600000)  // Продление аденды. Время точно будет указано при занятой книге
            } else {
                returnDate.time = returnDate.time.plus(1209600000) // текущая дата + 2 недели
            }

            compositeDisposable.add(
                bookRepository.rentBook(book.bookId, returnDate)
                    .subscribeOn(Schedulers.io())
                    .map {
                        DeviceServiceConnector.getPrinterService()
                            .printDocument(
                                ru.evotor.devices.commons.Constants.DEFAULT_DEVICE_INDEX,
                                PrinterDocument(
                                    PrintableText("<----- Бибилиотека Эвотор ----->"),
                                    PrintableText("\n "),
                                    PrintableText("Название:"),
                                    PrintableText(book.title),
                                    PrintableText("\n "),
                                    PrintableText("Автор:"),
                                    PrintableText(book.author),
                                    PrintableText("\n "),
                                    PrintableText("Дата возврата:"),
                                    PrintableText(
                                        SimpleDateFormat(
                                            "dd.MM.yyyy",
                                            Locale.getDefault()
                                        ).format(returnDate)
                                    ),
                                    PrintableText("\n "),
                                    PrintableText("Штрихкод"),
                                    PrintableText("\n "),
                                    PrintableBarcode(
                                        book.barcode,
                                        PrintableBarcode.BarcodeType.CODE39
                                    ),
                                    PrintableText("<------------------------------>")
                                )
                            )
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        bookRentStatusData.value =
                            SimpleDateFormat("dd MMMM", Locale.getDefault()).format(returnDate)
                    }, {
                        errorGetBookData.value = it
                        it.printStackTrace()
                    })
            )
        }
    }

    fun returnBook() {
        compositeDisposable.clear()
        bookLiveData.value?.let { book ->
            compositeDisposable.add(
                bookRepository.returnBook(book.bookId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        bookReturnStatusData.value = "ok"
                    }, {
                        errorGetBookData.value = it
                        it.printStackTrace()
                    })
            )
        }
    }

    fun setRate(newRate: Int) {
        compositeDisposable.clear()
        bookLiveData.value?.let { book ->
            compositeDisposable.add(
                bookRepository.setBookRate(book.bookId, newRate)
                    .subscribeOn(Schedulers.io())
                    .subscribe({

                    }, {
                        errorGetBookData.value = it
                    })
            )
        }
    }

    fun prolongBook() {

    }


    fun searchQueryFilter(query: String?) {
        query?.let {
            val newList =
                bookListLiveData.value?.filter { book ->
                    book.author.toUpperCase(Locale.ROOT).contains(query) || book.title.toUpperCase(
                        Locale.ROOT
                    ).contains(query)
                }
            this.bookListLiveData.value = newList
        }
    }

    fun getBooksListObservable(): LiveData<List<DataBook>> {
        return this.bookListLiveData
    }

    fun getBookRentObservable(): LiveData<String> {
        return this.bookRentStatusData
    }

    fun getBookReturnObservable(): LiveData<String> {
        return this.bookReturnStatusData
    }

    fun getLoadingObservable(): LiveData<Boolean> {
        return this.loading
    }

    fun getBookObservable(): LiveData<DataBook> {
        return this.bookLiveData
    }

    fun getBookErrorObservable(): LiveData<Throwable> {
        return this.errorGetBookData
    }

    fun getBooksListErrorObservable(): LiveData<Throwable> {
        return this.errorGetBooksData
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun getBookByBarcode(barcode: String): DataBook? =
        bookListLiveData.value?.find { dataBook -> dataBook.barcode == barcode }
}
