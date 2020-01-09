package ru.apliter.domain.entities

import java.math.BigDecimal
import java.util.*

data class DomainBook(
    val bookId: String,
    val title: String,
    val author: String,
    val barcode: String,
    val rate : Int,
    val preview: String,
    val counter: BigDecimal?,
    val isVacant: Boolean,
    val returnDate: Date?,
    val uri: String?
)
