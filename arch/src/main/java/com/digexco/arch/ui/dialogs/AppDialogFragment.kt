package com.digexco.arch.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.digexco.arch.R
import com.digexco.arch.ui.fragments.BaseFragment
import java.util.*

open class AppDialogFragment : DialogFragment() {

    lateinit var uuid: UUID
    var dialogConductor: DialogConductor? = null

    override fun onAttach(context: Context) {
        dialogConductor = (parentFragment as? BaseFragment?)?.vm?.dialog
        super.onAttach(context)
    }

    override fun onDetach() {
        dialogConductor = null
        super.onDetach()
    }

    private val dismissListener = DialogInterface.OnDismissListener {
        dialogConductor?.onDialogAction(uuid, DialogResult.Dismiss)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arg = arguments ?: savedInstanceState ?: throw NullPointerException("You're should pass arguments")
        val uuidString = arg.getString(DialogResolver.ID)
        uuid = UUID.fromString(uuidString)
    }

    open fun invokePositiveAction() {
        dialogConductor?.onDialogAction(uuid, DialogResult.Positive)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arg = arguments ?: throw NullPointerException("You're should pass arguments")

        val dialog = AlertDialog.Builder(requireContext())

        dialog
            .setCancelable(arg.getBoolean(DialogResolver.CANCELABLE))
            .setOnDismissListener(dismissListener)

        setDialogValue(arg, DialogResolver.TITLE, dialog::setTitle)
        setDialogValue(arg, DialogResolver.DESCRIPTION, dialog::setMessage)
        setDialogValue(arg, DialogResolver.POSITIVE_ANSWER, dialog::setPositiveButton) {
            DialogInterface.OnClickListener { _, _ ->
                invokePositiveAction()
            }
        }
        setDialogValue(arg, DialogResolver.NEGATIVE_ANSWER, dialog::setNegativeButton) {
            DialogInterface.OnClickListener { _, _ ->
                dialogConductor?.onDialogAction(uuid, DialogResult.Negative)
            }
        }
        setDialogValue(arg, DialogResolver.NEUTRAL_ANSWER, dialog::setNeutralButton) {
            DialogInterface.OnClickListener { _, _ ->
                dialogConductor?.onDialogAction(uuid, DialogResult.Neutral)
            }
        }

        val items = arg.getStringArray(DialogResolver.ARRAY)
        if (items != null)
            dialog.setItems(items) { _, which ->
                val item = items[which]
                dialogConductor?.onDialogAction(uuid, DialogResult.Item(item, which))
            }
        prepareDialog(dialog)

        return dialog.create()
    }

    open fun prepareDialog(dialog: AlertDialog.Builder) {
    }

    fun setDialogValue(arg: Bundle, key: String, function: (String) -> Unit) {
        val value = arg.getString(key) ?: return
        if (value.isNotEmpty()) function(value)
    }

    fun setDialogValue(
        arg: Bundle,
        key: String,
        function: (String, DialogInterface.OnClickListener) -> Unit,
        listener: () -> DialogInterface.OnClickListener
    ) {
        val value = arg.getString(key) ?: return
        if (value.isNotEmpty()) function(value, listener())
    }

    companion object {
        private const val TAG = "AppDialog"
        fun show(fragmentManager: FragmentManager, data: Bundle): AppDialogFragment {
            return AppDialogFragment().apply {
                arguments = data
                setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
                show(fragmentManager, TAG)
            }
        }
    }
}
