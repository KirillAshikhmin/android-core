package com.digexco.arch.helpers

import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

fun wrapToBundle(key: String, value: String): Bundle {
    val bundle = Bundle()
    bundle.putString(key, value)
    return bundle
}

fun wrapToBundle(key: String, value: Array<out String>): Bundle {
    val bundle = Bundle()
    bundle.putStringArray(key, value)
    return bundle
}

fun wrapToBundle(key: String, value: Int): Bundle {
    val bundle = Bundle()
    bundle.putInt(key, value)
    return bundle
}

fun wrapToBundle(key: String, parcelableArrayList: ArrayList<Parcelable>): Bundle {
    val bundle = Bundle()
    bundle.putParcelableArrayList(key, parcelableArrayList)
    return bundle
}


fun Bundle.addToBundle(key: String, value: String): Bundle {
    this.putString(key, value)
    return this
}

fun Bundle.addToBundle(key: String, value: Bundle?): Bundle {
    if (value != null) this.putBundle(key, value)
    return this
}

fun Bundle.addToBundle(key: String, value: Boolean): Bundle {
    this.putBoolean(key, value)
    return this
}


fun Bundle.toMap(map: Map<String, Any> = mapOf()): Map<String, Any> {
    val keys = keySet()
    val mutableMap: MutableMap<String, Any> = mutableMapOf()
    mutableMap.putAll(map)
    val iterator = keys?.iterator()
    if (iterator != null) {
        while (iterator.hasNext()) {
            val k = iterator.next()
            val v = this[k]
            if (v != null) {
                mutableMap[k] = v
            }
        }
    }
    return mutableMap.toMap()
}


fun <V> Map<String, V>.toBundle(bundle: Bundle = Bundle()): Bundle = bundle.apply {
    forEach {
        val k = it.key
        val v = it.value
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            when (v) {
                is Size -> putSize(k, v) //api 21
                is SizeF -> putSizeF(k, v) //api 21
            }
        }
        when (v) {
            is IBinder -> putBinder(k, v)
            is Bundle -> putBundle(k, v)
            is Boolean -> putBoolean(k, v)
            is Byte -> putByte(k, v)
            is ByteArray -> putByteArray(k, v)
            is Char -> putChar(k, v)
            is CharArray -> putCharArray(k, v)
            is CharSequence -> putCharSequence(k, v)
            is Float -> putFloat(k, v)
            is FloatArray -> putFloatArray(k, v)
            is Parcelable -> putParcelable(k, v)
            is java.io.Serializable -> putSerializable(k, v)
            is Short -> putShort(k, v)
            is ShortArray -> putShortArray(k, v)
            else -> throw IllegalArgumentException("$v is of a type that is not currently supported")
        }
    }
}


@Suppress("UNCHECKED_CAST")
fun <T> Map<String, *>.tryGetValue(key: String): T? {
    if (this.containsKey(key)) return this[key] as T?
    return null
}

@Suppress("UNCHECKED_CAST")
fun <T, U> Map<U, *>.tryGetValue(key: U): T? {
    if (this.containsKey(key)) return this[key] as T?
    return null
}

val <T> LiveData<T>.valueNN
    get() = this.value!!


fun MutableLiveData<Boolean>.inverse() {
    val current = this.valueNN
    this.value = !current
}



fun CharSequence?.isNotNullOrEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}

fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block(this.value, liveData.value)
    }
    return result
}

fun <T, R> LiveData<T>.castTo(
    block: (T?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value)
    }
    return result
}
