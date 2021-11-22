package com.kappstudio.joboardgame.newgame

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
import com.kappstudio.joboardgame.newparty.PartyInvalidInput
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

    fun addMockData() {
      name.value = "三國殺"
       minPlayerQty.value  = "2"
      maxPlayerQty.value  = "10"
      time.value  = "45"
       desc.value  = "三國殺是一個以三國時代為背景，集角色扮演、戰鬥、偽裝等要素于一體的多人卡片遊戲。玩家可以通過在遊戲裡扮演不同身份的角色，隱藏自己，尋找同伴，並運用各是不同的技能擊敗敵對勢力，得到最後的勝利，創造一個屬於自己的三國傳奇。三國殺是主要流行於中國大陸、港澳和台灣的桌上紙牌遊戲/網上遊戲。該紙牌遊戲以殺人紙牌（Bang!）為原型，以華夏的三國時代及演義文學為背景，擁有不同類型的身份牌、武將牌、體力牌、遊戲牌（包括基本牌、錦囊牌、裝備牌）等。遊戲玩家數量為2-10人。三國殺遊戲由游卡桌遊（Yoka Games）開發，於2008年9月18日在北京、上海和廈門三個城市同步上市。三國殺遊戲的核心為三國殺標準版[1]，可以配合各種擴展包以增加遊戲性。"
     }

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
                is Result.Success -> {
                    _imageUrl.value = result.data!!
                    Timber.d("image: ${imageUrl.value}")
                }

        }
    } }
}
