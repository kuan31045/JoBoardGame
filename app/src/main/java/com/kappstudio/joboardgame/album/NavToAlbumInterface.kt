package com.kappstudio.joboardgame.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface NavToAlbumInterface {
    companion object {
        private val _navToAlbum = MutableLiveData<List<String>?>()
    }

    val navToAlbum: LiveData<List<String>?>
        get() = _navToAlbum

    fun navToAlbum(photo: List<String>) {
        _navToAlbum.value = photo
    }

    fun onNavToAlbum() {
        _navToAlbum.value = null
    }
}