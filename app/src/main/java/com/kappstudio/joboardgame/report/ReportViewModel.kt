package com.kappstudio.joboardgame.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.data.Report
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import kotlinx.coroutines.launch

class ReportViewModel(
    theUser: User,
) : ViewModel() {
    // EditText
    var thing = MutableLiveData("")

    private val _user = MutableLiveData(theUser)
    val user: LiveData<User>
        get() = _user

    private val _sendOk = MutableLiveData<Boolean>()
    val sendOk: LiveData<Boolean>
        get() = _sendOk

    fun report() {
        viewModelScope.launch {
            val report = Report(
                thing = thing.value ?: "",
                violationId = user.value?.id ?: ""
            )
            val result = FirebaseService.sendReport(report)
            if (result) {
                _sendOk.value = true
            }
        }
    }

}