package com.kappstudio.joboardgame.ui.newgame

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.allGames
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.*
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.source.remote.LoadApiStatus
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil
import timber.log.Timber

class NewGameViewModel : ViewModel() {

    //Image
    var imageUri = MutableLiveData<Uri>()

    // EditText
    var name = MutableLiveData("")
    var minPlayerQty = MutableLiveData("")
    var maxPlayerQty = MutableLiveData("")
    var time = MutableLiveData("")
    var desc = MutableLiveData("")

    private val _imageUrl = MutableLiveData<String>()
    private val imageUrl: LiveData<String>
        get() = _imageUrl

    // Handle the error for edittext
    private val _invalidPublish = MutableLiveData<GameInvalidInput?>()
    val invalidPublish: LiveData<GameInvalidInput?>
        get() = _invalidPublish

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private var types = mutableListOf<String>()
    private var tools = mutableListOf<String>()

    fun addType(type: String) {
        types.add(type)
    }

    fun removeType(type: String) {
        types.remove(type)
    }

    fun addTool(tool: String) {
        tools.add(tool)
    }

    fun removeTool(tool: String) {
        tools.remove(tool)
    }

    fun prepareCreate() {
        if (allGames.value?.filter { it.name == name.value?.replace("\\s".toRegex(), "") ?: "" }?.size ?: 0 == 0) {


            Timber.d("$types + $tools")
            _invalidPublish.value = when {
                imageUri.value == null ->
                    GameInvalidInput.IMAGE_EMPTY

                name.value?.replace("\\s".toRegex(), "").isNullOrEmpty() ->
                    GameInvalidInput.NAME_EMPTY

                types.size == 0 -> GameInvalidInput.TYPE_EMPTY

                minPlayerQty.value?.replace("\\s".toRegex(), "").isNullOrEmpty()
                        || (minPlayerQty.value?.toInt() ?: 0) < 1 ->
                    GameInvalidInput.MIN_PLAYER_QTY_EMPTY

                maxPlayerQty.value?.replace("\\s".toRegex(), "").isNullOrEmpty()
                        || (maxPlayerQty.value?.toInt() ?: 0) < 1 ->
                    GameInvalidInput.MAX_PLAYER_QTY_EMPTY

                time.value?.replace("\\s".toRegex(), "").isNullOrEmpty()
                        || (time.value?.toInt() ?: 0) < 1 ->
                    GameInvalidInput.TIME_EMPTY

                desc.value?.replace("\\s".toRegex(), "").isNullOrEmpty() ->
                    GameInvalidInput.DESC_EMPTY

                else -> {
                    createGame()

                    null

                }
            }
        }else{
            ToastUtil.show("資料庫內已經有${name.value}了!")
        }
    }


    private fun createGame() {
        viewModelScope.launch {
            uploadImage()
            val res = FirebaseService.createGame(
                Game(
                    name = name.value ?: "",
                    image = imageUrl.value ?: "",
                    type = types,
                    time = time.value?.toInt() ?: 0,
                    tools = tools,
                    minPlayerQty = minPlayerQty.value?.toInt() ?: 0,
                    maxPlayerQty = maxPlayerQty.value?.toInt() ?: 0,
                    desc = desc.value ?: "",
                )
            )

            if (res) {
                ToastUtil.show(appInstance.getString(R.string.add_ok))
                _status.value = LoadApiStatus.DONE
            }
        }
    }

    private suspend fun uploadImage() {
        imageUri.value?.let {it->

            _status.value = LoadApiStatus.LOADING
            when (val result = FirebaseService.uploadPhoto(it)) {
                is com.kappstudio.joboardgame.data.Resource.Success -> {
                    _imageUrl.value = result.data!!
                    Timber.d("image: ${imageUrl.value}")
                }

        }
    } }
}
