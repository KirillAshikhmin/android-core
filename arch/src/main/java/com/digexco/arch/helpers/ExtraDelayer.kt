package com.digexco.arch.helpers

class ExtraDelayer {

    private var startAt: Long = 0

    init {
        startAt = System.currentTimeMillis()
    }

    suspend fun delay(mills : Long) {
        val time = System.currentTimeMillis() - startAt
        if (time<mills) kotlinx.coroutines.delay(mills - time)
    }
}
