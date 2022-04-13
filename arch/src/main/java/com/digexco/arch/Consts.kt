package com.digexco.arch

object Consts {
    const val EXTRA_STATE_DELAY_MILLS = 500L
}

object ArchConsts {
    const val navigation_result_key = "navigation_result_key"
    const val navigation_name = "navigation_name"
    const val navigation_properties = "navigation_properties"
}

enum class DialogTypes {
    alert,
    text,
    sheet,
    toast,
    date,
    loading
}
