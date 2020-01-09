package ru.apliter.domain.api


interface RetrofitClientApi<T> {

    fun getRetrofitClient(): T

}