package ru.kirillashikhmin.screens.empty

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.digexco.arch.ui.fragments.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.kirillashikhmin.screens.empty.databinding.FragmentEmptyBinding

@AndroidEntryPoint
class EmptyFragment : BaseFragment(R.layout.fragment_empty) {
    override val binding by viewBinding<FragmentEmptyBinding>()
    override val vm by viewModels<EmptyViewModel>()
}
