@file:Suppress("unused")
@file:OptIn(ExperimentalContracts::class)

package com.digexco.arch.ui.dialogs

import kotlinx.datetime.LocalDateTime
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


sealed class DialogResult {
    object Positive : DialogResult()
    object Negative : DialogResult()
    object Neutral : DialogResult()
    object Dismiss : DialogResult()
    class Text(val text: String?) : DialogResult()
    class Item(val index: Int, val item: String?) : DialogResult()
    class Date(val dateTime: LocalDateTime) : DialogResult()
}


val DialogResult.isPositive get() = this is DialogResult.Positive
val DialogResult.isNegative get() = this is DialogResult.Negative
val DialogResult.isNeutral get() = this is DialogResult.Neutral
val DialogResult.isDismiss get() = this is DialogResult.Dismiss
val DialogResult.isText get() = this.checkIsText()
val DialogResult.isItem get() = this.checkIsItem()
val DialogResult.isDate get() = this.checkIsDate()

fun DialogResult.asText() = this as DialogResult.Text
fun DialogResult.asItem() = this as DialogResult.Item
fun DialogResult.asDate() = this as DialogResult.Date



@ExperimentalContracts
fun DialogResult.checkIsText(): Boolean {
    contract {
        returns(true) implies (this@checkIsText is DialogResult.Text)
    }
    return this is DialogResult.Text
}

@ExperimentalContracts
fun DialogResult.checkIsItem(): Boolean {
    contract {
        returns(true) implies (this@checkIsItem is DialogResult.Item)
    }
    return this is DialogResult.Item
}


@ExperimentalContracts
fun DialogResult.checkIsDate(): Boolean {
    contract {
        returns(true) implies (this@checkIsDate is DialogResult.Date)
    }
    return this is DialogResult.Date
}

