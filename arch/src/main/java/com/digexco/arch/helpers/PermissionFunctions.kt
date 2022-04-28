package com.digexco.arch.helpers

import android.app.Activity
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment

fun Fragment.isGranted(permission: String) = run {
    context?.let {
        (PermissionChecker.checkSelfPermission(it, permission) == PermissionChecker.PERMISSION_GRANTED)
    } ?: false
}

fun Activity.isGranted(permission: String) = run {
    PermissionChecker.checkSelfPermission(this, permission) == PermissionChecker.PERMISSION_GRANTED
}
