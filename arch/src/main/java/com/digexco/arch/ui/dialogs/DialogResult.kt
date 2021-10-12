@file:Suppress("unused")

package com.digexco.arch.ui.dialogs


sealed class DialogResult {
    object Positive : DialogResult()
    object Negative : DialogResult()
    object Neutral : DialogResult()
    object Dismiss : DialogResult()
    class Text(val text: String?) : DialogResult()
    class Item(val item: String?, val index: Int) : DialogResult()
}


val DialogResult.isPositive get() = this is DialogResult.Positive
val DialogResult.isNegative get() = this is DialogResult.Negative
val DialogResult.isNeutral get() = this is DialogResult.Neutral
val DialogResult.isDismiss get() = this is DialogResult.Dismiss
val DialogResult.isText get() = this is DialogResult.Text
val DialogResult.isItem get() = this is DialogResult.Item

fun DialogResult.asText() = this as DialogResult.Text
fun DialogResult.asItem() = this as DialogResult.Item

