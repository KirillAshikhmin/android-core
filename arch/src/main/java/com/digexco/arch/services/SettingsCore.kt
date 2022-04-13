package com.digexco.arch.services

import android.content.Context
import androidx.core.content.edit
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.Preferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


@Suppress("UNCHECKED_CAST")
abstract class SettingsCore {
    lateinit var preference: Preferences
    fun init(context: Context) {
        preference = BinaryPreferencesBuilder(context).build()
    }


    abstract fun cleanAll()

    open fun cleanUser() {}

    fun <T> set(value: T, propertyName: String = "") {
        if (propertyName.isBlank()) return
        addOrUpdateValue(propertyName, value)
    }

    inline fun <reified T : Any?> get(defaultValue: T?, propertyName: String): T? {
        return if (propertyName.isEmpty()) defaultValue else getValueOrDefault(
            propertyName,
            defaultValue
        )
    }

    inline fun <reified T : Any?> getValueOrDefault(propertyName: String, defaultValue: T?): T? {
        with(preference) {
            return when (T::class) {
                Boolean::class -> getBoolean(propertyName, (defaultValue as? Boolean) ?: false) as? T
                Int::class -> getInt(propertyName, (defaultValue as? Int) ?: 0) as? T
                Float::class -> getFloat(propertyName, (defaultValue as? Float) ?: .0f) as? T
                Double::class -> getDouble(propertyName, (defaultValue as? Double) ?: .0) as? T
                Long::class -> getLong(propertyName, (defaultValue as? Long) ?: 0L) as T?
                String::class -> getString(propertyName, defaultValue as? String) as? T
                List::class -> (getStringSet(propertyName, defaultValue as? Set<String>)?.toList()) as? T
                else -> defaultValue
            }
        }
    }

    private fun <T> addOrUpdateValue(propertyName: String, value: T) {
        try {
            preference.edit(true) {
                when (value) {
                    is Boolean -> putBoolean(propertyName, value)
                    is Int -> putInt(propertyName, value)
                    is Float -> putFloat(propertyName, value)
                    is String -> putString(propertyName, value)
                    is Long -> putLong(propertyName, value)
                    is List<*> -> putStringSet(propertyName, value.map { it.toString() }.toSet())
                    null -> putString(propertyName, null)
                    else -> println("ErrorType")
                }
            }
        } catch (e: Exception) {
            println(e.toString())
        }
    }


}

inline fun <reified T : Any> SettingsCore.preferenceObject(defaultValue: T) =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            get(defaultValue, property.name) ?: defaultValue

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
            set(value, property.name)
    }

inline fun <reified T : Any?> SettingsCore.preferenceObjectNullable(defaultValue: T? = null) =
    object : ReadWriteProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? =
            get(defaultValue, property.name)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) =
            set(value, property.name)
    }
