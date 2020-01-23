package ru.apliter.domain.api


interface RetrofitClient<T> {

    fun getRetrofitClient(): T

}