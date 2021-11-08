package com.kappstudio.joboardgame.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlbumViewModel(photos: List<String>) : ViewModel() {


    private var _photos = MutableLiveData(photos)
    val photos: LiveData<List<String>>
        get() = _photos

    private var _position = MutableLiveData<Int?>()
    val position: LiveData<Int?>
    get() = _position

    // nav
    private val _navToPhoto = MutableLiveData<List<String>?>()
    val navToPhoto: LiveData<List<String>?>
        get() = _navToPhoto

    fun navToPhoto(position: Int) {
        _position.value = position
        _navToPhoto.value=photos.value
    }
    fun onNavToPhoto( ) {
        _position.value = null

        _navToPhoto.value =null

    }
}