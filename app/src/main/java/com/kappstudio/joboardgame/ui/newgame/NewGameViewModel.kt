package com.kappstudio.joboardgame.ui.newgame

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.repository.GameRepository
import com.kappstudio.joboardgame.data.repository.StorageRepository
import com.kappstudio.joboardgame.util.LoadApiStatus
import kotlinx.coroutines.launch
import com.kappstudio.joboardgame.util.ToastUtil
import com.kappstudio.joboardgame.util.checkValid
import com.kappstudio.joboardgame.data.Result

class NewGameViewModel(
    gameName: String,
    private val storageRepository: StorageRepository,
    private val gameRepository: GameRepository,
) : ViewModel() {

    //Image
    var imageUri = MutableLiveData<Uri>()

    // EditText
    var name = MutableLiveData(gameName)
    var minPlayerQty = MutableLiveData("")
    var maxPlayerQty = MutableLiveData("")
    var time = MutableLiveData("")
    var desc = MutableLiveData("")

    // Handle the error for edittext
    private val _invalidPublish = MutableLiveData<GameInvalidInput?>()
    val invalidPublish: LiveData<GameInvalidInput?> = _invalidPublish

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus> = _status

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
        viewModelScope.launch {
            if (gameRepository.isGameExist(name.value!!.replace("\\s".toRegex(), ""))) {
                ToastUtil.show("${name.value} ${appInstance.getString(R.string.already_have)}")
                return@launch
            }

            _invalidPublish.value = when {
                imageUri.value == null ->
                    GameInvalidInput.IMAGE_EMPTY

                !name.value.checkValid() ->
                    GameInvalidInput.NAME_EMPTY

                types.size == 0 -> GameInvalidInput.TYPE_EMPTY

                !minPlayerQty.value.checkValid()
                        || (minPlayerQty.value?.toInt() ?: 0) < 1 ->
                    GameInvalidInput.MIN_PLAYER_QTY_EMPTY

                !maxPlayerQty.value.checkValid()
                        || (maxPlayerQty.value?.toInt() ?: 0) < 1 ->
                    GameInvalidInput.MAX_PLAYER_QTY_EMPTY

                !time.value.checkValid()
                        || (time.value?.toInt() ?: 0) < 1 ->
                    GameInvalidInput.TIME_EMPTY

                !desc.value.checkValid() ->
                    GameInvalidInput.DESC_EMPTY

                else -> {
                    createGame()
                    null
                }
            }
        }
    }

    private fun createGame() {
        viewModelScope.launch {
            storageRepository.uploadPhoto(imageUri.value!!).collect { photoRes ->
                _status.value = when (photoRes) {
                    is Result.Success -> {
                        val gameTaskRes = gameRepository.addGame(
                            Game(
                                name = name.value!!,
                                image = photoRes.data,
                                type = types,
                                time = time.value?.toInt()!!,
                                tools = tools,
                                minPlayerQty = minPlayerQty.value?.toInt()!!,
                                maxPlayerQty = maxPlayerQty.value?.toInt()!!,
                                desc = desc.value!!,
                            )
                        )

                        if (gameTaskRes) {
                            ToastUtil.showByRes(R.string.create_game_ok)
                            LoadApiStatus.DONE
                        } else {
                            LoadApiStatus.ERROR
                        }
                    }

                    is Result.Fail -> {
                        ToastUtil.showByRes(photoRes.stringRes)
                        LoadApiStatus.ERROR
                    }

                    else -> LoadApiStatus.LOADING
                }
            }
        }
    }
}