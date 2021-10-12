package com.digexco.arch.ui.dialogs

import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager

class TextDialogFragment : AppDialogFragment() {
    private var text: String? = null

    override fun invokePositiveAction() {
        dialogConductor?.onDialogAction(uuid, DialogResult.Text(text))
    }

    override fun prepareDialog(dialog: AlertDialog.Builder) {
        val arg = arguments ?: return
        val hint = arg.getString(DialogResolver.HINT)
        val input = EditText(context)
        input.hint = hint
        input.addTextChangedListener { text = it.toString() }


        val typedValue = TypedValue()
        requireActivity().theme.resolveAttribute(
            android.R.attr.dialogPreferredPadding,
            typedValue,
            true
        )
        val resId = typedValue.resourceId
        var margin = if (resId <= 0) 0 else requireActivity().resources.getDimensionPixelSize(resId)

        val d = requireActivity().resources.displayMetrics.density
        val padding = (-4 * d).toInt()
        margin += padding;

        val lp = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val layout = FrameLayout(requireContext())
        layout.setPadding(margin, 0, margin, 0)
        layout.addView(input, lp);

        dialog.setView(layout)
    }

    companion object {
        const val TAG = "TextDialog"
        fun show(fragmentManager: FragmentManager, data: Bundle): TextDialogFragment {
            return TextDialogFragment().apply {
                arguments = data
                show(fragmentManager, TAG)
            }
        }
    }
}
