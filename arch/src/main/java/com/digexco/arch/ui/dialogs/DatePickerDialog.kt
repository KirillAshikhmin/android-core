package com.digexco.arch.ui.dialogs

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.digexco.arch.ui.fragments.BaseFragment
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.*

object DatePickerDialog {

    private const val DATE_PICKER_TAG = "DatePicker"

    fun show(fragmentManager: FragmentManager, bundle: Bundle) {

        val uuidString = bundle.getString(DialogResolver.ID)
        val title = bundle.getString(DialogResolver.TITLE)
        val uuid = UUID.fromString(uuidString)
        val builder: MaterialDatePicker.Builder<Long> = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText(title)
        val picker = builder.build()
        picker.show(fragmentManager, DATE_PICKER_TAG)

        picker.addOnCancelListener {
            val dialogConductor = (picker.parentFragment as? BaseFragment?)?.vm?.dialog
            dialogConductor?.onDialogAction(uuid, DialogResult.Dismiss)
        }
        picker.addOnNegativeButtonClickListener {
            val dialogConductor = (picker.parentFragment as? BaseFragment?)?.vm?.dialog
            dialogConductor?.onDialogAction(uuid, DialogResult.Negative)
        }
        picker.addOnPositiveButtonClickListener {
            val instant = Instant.fromEpochMilliseconds(it)
            val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            val dialogConductor = (picker.parentFragment as? BaseFragment?)?.vm?.dialog
            dialogConductor?.onDialogAction(uuid, DialogResult.Date(dateTime))
        }
    }
}
