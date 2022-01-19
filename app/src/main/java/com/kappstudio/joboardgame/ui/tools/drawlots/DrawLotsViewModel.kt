package com.kappstudio.joboardgame.ui.tools.drawlots

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.gujin.toast.ToastUtil

class DrawLotsViewModel : ViewModel() {
    private val _items = MutableLiveData<List<String>>(mutableListOf())
    val items: LiveData<List<String>>
        get() = _items

    private val _navToDraw = MutableLiveData<String?>()
    val navToDraw: LiveData<String?>
        get() = _navToDraw
    fun setItems(items: List<String>){
        _items.value=items
    }
    fun addItem(item: String) {
        if (items.value?.contains(item) == true) {
            ToastUtil.show("${item}已經在抽籤項目了!")
        } else {
            _items.value = items.value?.plus(item)
        }
    }

    fun removeItem(item: String) {
        _items.value = items.value?.minus(item)
    }

    fun draw() {
        _navToDraw.value = items.value?.randomOrNull()
    }

    fun onNavToDraw() {
        _navToDraw.value = null
    }
}