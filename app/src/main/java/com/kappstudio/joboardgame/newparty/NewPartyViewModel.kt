package com.kappstudio.joboardgame.newparty

import android.net.Uri
import androidx.lifecycle.*
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.*

import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.source.remote.LoadApiStatus
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

private const val defaultCover =
    "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/cover1.png?alt=media&token=f3144faf-1e81-4d84-b25e-46e32b64b8f1"

class NewPartyViewModel : ViewModel(), NavToGameDetailInterface {


    var time = MutableLiveData<Long>(0)

    // EditText
    var title = MutableLiveData("")
    var location = MutableLiveData("")
    var requirePlayerQty = MutableLiveData("")
    var note = MutableLiveData("")
    var gameName = MutableLiveData("")

    //LngLat
    var lat = MutableLiveData(0.0)
    var lng = MutableLiveData(0.0)

    val _games = MutableLiveData<MutableList<Game>>(mutableListOf())
    val games: LiveData<MutableList<Game>>
        get() = _games

    //Cover
    var photoUri = MutableLiveData<Uri>()

    private val _coverUrl = MutableLiveData<String>()
    private val coverUrl: LiveData<String>
        get() = _coverUrl

    // Handle the error for edittext
    private val _invalidPublish = MutableLiveData<InvalidInput?>()
    val invalidPublish: LiveData<InvalidInput?>
        get() = _invalidPublish

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    fun addGame() {
        if (gameName.value?.replace("\\s".toRegex(), "") != "") {
            viewModelScope.launch {
                val game = FirebaseService.getGameByName(gameName.value ?: "")
                gameName.value = ""
                if (_games.value?.contains(game) == true) {
                    ToastUtil.show(game.name + appInstance.getString(R.string.already_in_list))
                } else {
                    _games.value = _games.value?.plus(game) as MutableList<Game>?
                }
            }
        } else {
            ToastUtil.show("請輸入遊戲名")
        }

    }


    fun createParty() {

        viewModelScope.launch {
            uploadCover()
            val gameMapList = mutableListOf<HashMap<String, String>>()
            games.value?.forEach {
                gameMapList.add(toGameMap(it))
            }
            val res = FirebaseService.createParty(
                NewParty(
                    title = title.value ?: "",
                    cover = coverUrl.value ?: defaultCover,
                    partyTime = time.value ?: 0,
                    location = Location(
                        location.value ?: "",
                        lat.value ?: 0.0,
                        lng.value ?: 0.0
                    ),
                    note = note.value ?: "",
                    requirePlayerQty = requirePlayerQty.value?.toIntOrNull() ?: 1,
                    gameList = gameMapList,
                )
            )

            if (res) {
                ToastUtil.show(appInstance.getString(R.string.creat_ok))
                _status.value = LoadApiStatus.DONE
            }

        }


    }

    fun removeGame(game: Game) {
        _games.value = _games.value?.minus(game) as MutableList<Game>
    }

    private suspend fun uploadCover() {
        photoUri.value?.let {


            _status.value = LoadApiStatus.LOADING

            when (val result = FirebaseService.uploadPhoto(it)) {
                is Result.Success -> {
                    _coverUrl.value = result.data!!
                    Timber.d("Photo: ${coverUrl.value}")
                }
            }

        }
    }

    fun prepareCreate() {
        _invalidPublish.value =    when {
            title.value?.replace("\\s".toRegex(), "").isNullOrEmpty() ->
                 InvalidInput.TITLE_EMPTY
            time.value == 0L ->
               InvalidInput.TIME_EMPTY
            location.value?.replace("\\s".toRegex(), "").isNullOrEmpty() ->
                InvalidInput.LOCATION_EMPTY
           requirePlayerQty.value?.replace("\\s".toRegex(), "").isNullOrEmpty() ->
               InvalidInput.QTY_EMPTY
            note.value?.replace("\\s".toRegex(), "").isNullOrEmpty() ->
                InvalidInput.DESC_EMPTY
            games.value==null || games.value!!.size==0 ->
                InvalidInput.GAMES_EMPTY
            else -> {
                createParty()
                null

            }
        }
    }



}