package com.digexco.dal

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


sealed class RequestResult<out T> {

    open val status: RequestStatus = RequestStatus.Unknown

    class EmptyResult : Success<Nothing?>() {
        override val value: Nothing? = null
    }

    sealed class Success<T> : RequestResult<T>() {

        abstract val value: T

        override fun toString() = "Success ($value)"

        class Value<T>(override val value: T) : Success<T>() {
            override val status: RequestStatus
                get() = RequestStatus.Ok
        }
        class ValueWithStatus<T>(override val value: T, override val status: RequestStatus) : Success<T>()

        object Empty : Success<Nothing>() {
            class Status(override val status: RequestStatus) : RequestResult<Nothing>()
            override val value: Nothing get() = error("No value")
            override fun toString() = "Success"
        }
    }

    sealed class Cached<T>(override val value: T, open val cacheTime: Long) : Success<T>() {

        override fun toString() = "Cached ($value)"

        class Value<T>(override val value: T, override val cacheTime : Long) : Cached<T>(value, cacheTime) {
            override val status: RequestStatus
                get() = RequestStatus.Ok
        }
    }

    sealed class Failure<E>(open val error: E? = null, open val message: String? = null) : RequestResult<Nothing>() {

        override fun toString() = "Failure ($message $error $throwable)"
        open val throwable: Throwable? = null

        class Error(override val status: RequestStatus, override val message: String? = null, override val throwable: Throwable? = null) : Failure<Nothing>(message = message)
        class ErrorWithData<E>(override val error : E, override val status: RequestStatus, override val message: String? = null, override val throwable: Throwable? = null) : Failure<E>(error, message)
    }
}


enum class RequestStatus(val code: Int) {
    Unknown(0),
    Ok(200),
    NotModified(304),
    BadRequest(400),
    Unauthorized(401),
    Forbidden(403),
    NotFound(404),
    NotAcceptable(406),
    RequestEntityTooLarge(413),
    Unprocessable(422),
    InternalServerError(500),
    NotImplemented(501),
    ServiceUnavailable(503),
    Canceled(1001),
    InvalidRequest(1002),
    SerializationError(1003),
    NoInternet(1004);

    companion object {
        private val map = values().associateBy(RequestStatus::code)

        fun fromCode(code: Int): RequestStatus {
            return map.getValue(code)
        }
    }
}


@OptIn(ExperimentalContracts::class)
fun <T> RequestResult<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is RequestResult.Success<T>)
    }
    return this is RequestResult.Success<T>
}

@OptIn(ExperimentalContracts::class)
fun <T> RequestResult<T>.isCached(): Boolean {
    contract {
        returns(true) implies (this@isCached is RequestResult.Cached<T>)
    }
    return this is RequestResult.Cached<T>
}

@OptIn(ExperimentalContracts::class)
fun <T> RequestResult<T>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is RequestResult.Failure<*>)
    }
    return this is RequestResult.Failure<*>
}


fun <T> RequestResult<T>.asFailure(): RequestResult.Failure<*> {
    return this as RequestResult.Failure<*>
}

fun <T> RequestResult<T>.asSuccess(): RequestResult.Success<T> {
    return this as RequestResult.Success<T>
}

fun <T> RequestResult<T>.asCached(): RequestResult.Cached<T> {
    return this as RequestResult.Cached<T>
}

