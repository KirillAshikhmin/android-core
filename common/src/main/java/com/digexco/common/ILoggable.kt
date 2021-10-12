package com.digexco.common

interface ILoggable {
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun w(tag: String, message: String)
    fun e(tag: String, throwable: Throwable)
    fun e(tag: String, message: String)
    fun e(tag: String, throwable: Throwable, message: String)
}
