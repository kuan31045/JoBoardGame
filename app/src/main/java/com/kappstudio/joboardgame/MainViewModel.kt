package com.kappstudio.joboardgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class MainViewModel : ViewModel() {

    private val _page = MutableLiveData<PageType>()
    val page: LiveData<PageType>
        get() = _page

    private val _title = MutableLiveData("")
    val title: LiveData<String>
        get() = _title

    private val _isImmersion = MutableLiveData(false)
    val isImmersion: LiveData<Boolean>
        get() = _isImmersion

    fun setBarStatus(status: PageType) {
        Timber.d("Status: $status")
        _page.value = status
        _title.value = status.title
        _isImmersion.value = status == PageType.GAME_DETAIL
    }
}