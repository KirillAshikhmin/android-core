package ru.kirillashikhmin.screens.calendar

import com.digexco.arch.bl.viewModels.internal.BaseViewModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    override val router: Router
) : BaseViewModel() {

}
