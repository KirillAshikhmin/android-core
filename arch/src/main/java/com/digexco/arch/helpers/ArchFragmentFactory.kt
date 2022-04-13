package com.digexco.arch.helpers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class ArchFragmentFactory(
    private val classLoader: ClassLoader,
    private val appPackageName: String
) : FragmentFactory() {

    private val _fullClassNames = mutableMapOf<String, String>()

    fun instantiate(className: String) = instantiate(classLoader, className)

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        var fullClassName = _fullClassNames[className]
        if (fullClassName.isNullOrEmpty()) {
            val name =
                getFragmentClassNameByNameInModule(className)
                    ?: getFragmentClassNameByName(className)
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

                val pkg = pkgFull.substring(0, pkgFull.lastIndexOf('.'))
                "$pkg.ui.fragments.$clsName"
            }
            val clz: Class<*> = Class.forName(fullName)
            return clz.name
        } catch (e: Exception) {
            println(e.toString())
        }
        return null
    }

    @Suppress("UNCHECKED_CAST")
    fun getFragmentClassNameByNameInModule(className: String): String? {
        try {
            var clsName = className
            val fullName = when {
                clsName.contains("#") -> {
                    val parts = clsName.split("#")
                    val namespace = parts[0]
                    var key = parts[1]
                    if (!key.endsWith("Fragment")) key += "Fragment"
                    "$appPackageName.features.$namespace.$key"
                }
                clsName.contains(".") ->
                    clsName
                else -> {
                    if (!clsName.endsWith("Fragment"))
                        clsName = className + "Fragment"
                    "$appPackageName.features.${className.lowercase()}.$clsName"
                }
            }
            val clz: Class<*> = Class.forName(fullName)
            return clz.name
        } catch (e: Exception) {
            println(e.toString())
        }
        return null
    }

}
