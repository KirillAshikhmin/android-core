package com.digexco.arch.helpers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class ArchFragmentFactory(private val classLoader: ClassLoader, private val appPackageName: String) : FragmentFactory() {

    fun instantiate(className: String) = instantiate(classLoader, className)

    private val _fullClassNames = mutableMapOf<String, String>()

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        var fullClassName = _fullClassNames[className]
        if (fullClassName.isNullOrEmpty()) {
            val name = getFragmentClassNameByName(className)
            if (name.isNullOrEmpty()) return super.instantiate(classLoader, className)
            fullClassName = name
            _fullClassNames[className] = fullClassName
        }
        return super.instantiate(classLoader, fullClassName)
    }


    @Suppress("UNCHECKED_CAST")
    fun getFragmentClassNameByName(className: String): String? {
        try {
            var clsName = className
            val fullName = if (clsName.contains(".")) clsName else {
                if (!clsName.endsWith("Fragment"))
                    clsName = className + "Fragment"
                val pkgFull = appPackageName

                "$pkgFull.screens.${className.lowercase()}.$clsName"
            }
            val clz: Class<*> = Class.forName(fullName)
            return clz.name
        } catch (e: Exception) {
            println(e.toString())
        }
        return null
    }

}
