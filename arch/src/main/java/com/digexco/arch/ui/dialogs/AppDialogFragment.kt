package com.digexco.arch.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.digexco.arch.R
import com.digexco.arch.ui.fragments.BaseFragment
import java.util.*

open class AppDialogFragment : DialogFragment() {


    private var copyable: Boolean = false
    lateinit var uuid: UUID
    var dialogConductor: DialogConductor? = null

    override fun onAttach(context: Context) {
        dialogConductor = (parentFragment as? BaseFragment?)?.vm?.dialog
        super.onAttach(context)
    }

    override fun onDetach() {
        dialogConductor = null
        EditText(context).addTextChangedListener {

        }

        super.onDetach()
    }

    override fun onDismiss(dialog: DialogInterface) {
        dialogConductor?.onDialogAction(uuid, DialogResult.Dismiss)
        super.onDismiss(dialog)
    }

    private val dismissListener = DialogInterface.OnDismissListener {
        dialogConductor?.onDialogAction(uuid, DialogResult.Dismiss)
    }

    private val cancelListener = DialogInterface.OnCancelListener {
        dialogConductor?.onDialogAction(uuid, DialogResult.Dismiss)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arg = arguments ?: savedInstanceState
        ?: throw NullPointerException("You're should pass arguments")
        val uuidString = arg.getString(DialogResolver.ID)
        uuid = UUID.fromString(uuidString)
    }

    open fun invokePositiveAction() {
        dialogConductor?.onDialogAction(uuid, DialogResult.Positive)
    }

    override fun onResume() {
        super.onResume()
        if (copyable) {
            try {
                val tv = dialog?.window?.decorView?.findViewById<TextView>(android.R.id.message)
                tv?.setTextIsSelectable(true)
            } catch (e: Exception) {
                // Oups!
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arg = arguments ?: throw NullPointerException("You're should pass arguments")

        val builder = AlertDialog.Builder(requireContext())

        builder
            .setOnCancelListener(cancelListener)
            .setOnDismissListener(dismissListener)

        isCancelable = arg.getBoolean(DialogResolver.CANCELABLE)

        setDialogValue(arg, DialogResolver.TITLE, builder::setTitle)
        setDialogValue(arg, DialogResolver.DESCRIPTION, builder::setMessage)
        setDialogValue(arg, DialogResolver.POSITIVE_ANSWER, builder::setPositiveButton) {
            DialogInterface.OnClickListener { _, _ ->
                invokePositiveAction()
            }
        }
        setDialogValue(arg, DialogResolver.NEGATIVE_ANSWER, builder::setNegativeButton) {
            DialogInterface.OnClickListener { _, _ ->
                dialogConductor?.onDialogAction(uuid, DialogResult.Negative)
            }
        }
        setDialogValue(arg, DialogResolver.NEUTRAL_ANSWER, builder::setNeutralButton) {
            DialogInterface.OnClickListener { _, _ ->
                dialogConductor?.onDialogAction(uuid, DialogResult.Neutral)
            }
        }

        val items = arg.getStringArray(DialogResolver.ARRAY)
        if (items != null)
            builder.setItems(items) { _, which ->
                val item = items[which]
                dialogConductor?.onDialogAction(uuid, DialogResult.Item(which, item))
            }
        prepareDialog(builder)

        copyable = arg.getBoolean(DialogResolver.COPYABLE)

        return builder.create()
    }

    open fun prepareDialog(dialog: AlertDialog.Builder) {
    }

    private fun setDialogValue(arg: Bundle, key: String, function: (String) -> Unit) {
        val value = arg.getString(key) ?: return
        if (value.isNotEmpty()) function(value)
    }

    private fun setDialogValue(
        arg: Bundle,
        key: String,
        function: (String, DialogInterface.OnClickListener) -> Unit,
        listener: () -> DialogInterface.OnClickListener
    ) {
        val value = arg.getString(key) ?: return
        if (value.isNotEmpty()) function(value, listener())
    }

    companion object {
        const val TAG = "AppDialog"
        fun show(fragmentManager: FragmentManager, data: Bundle): AppDialogFragment {
            return AppDialogFragment().apply {
                arguments = data
                show(fragmentManager, TAG)
            }
        }
    }
}
