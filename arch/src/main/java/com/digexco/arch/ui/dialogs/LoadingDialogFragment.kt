package com.digexco.arch.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.digexco.arch.R
import com.digexco.arch.databinding.DialogLoadingBinding

class LoadingDialogFragment : DialogFragment(R.layout.dialog_loading) {

    val binding: DialogLoadingBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val text = arguments?.getString(TEXT_ARG)
        if (text.isNullOrEmpty()) binding.message.visibility = View.GONE
        else binding.message.text = text
    }

    companion object {
        const val TAG = "LoadingDialog"
        private const val TEXT_ARG = "text"
        fun show(fragmentManager: FragmentManager, text: String? = null): LoadingDialogFragment {
            val args = Bundle()
            args.putString(TEXT_ARG, text)
            return LoadingDialogFragment().apply {
                isCancelable = false
                arguments = args
                show(fragmentManager, TAG)
            }
        }
    }
}

