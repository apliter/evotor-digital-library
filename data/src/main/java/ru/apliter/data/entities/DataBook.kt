package ru.apliter.data.entities

import java.math.BigDecimal
import java.util.*

data class DataBook(
    val bookId: String,
    val title: String,
    val author: String,
    val barcode: String,
    val rate : Int,
    val preview: String,
    val counter: BigDecimal?,
    val vacant: Boolean,
    val returnDate: Date?,
    val uri: String?
)