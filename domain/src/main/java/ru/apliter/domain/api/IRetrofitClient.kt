package ru.apliter.domain.api


interface IRetrofitClient<T> {

    fun getRetrofitClient(): T

}