package com.kappstudio.joboardgame.ui.newparty

import android.net.Uri
import androidx.lifecycle.*
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.allGames
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.*
import com.kappstudio.joboardgame.data.remote.FirebaseService
import com.kappstudio.joboardgame.util.LoadApiStatus
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.util.checkValid
import kotlinx.coroutines.launch
import com.kappstudio.joboardgame.util.ToastUtil

private const val DEFAULT_COVER =
    "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/cover1.png?alt=media&token=f3144faf-1e81-4d84-b25e-46e32b64b8f1"

class NewPartyViewModel : ViewModel(), NavToGameDetailInterface {
    
    var partyTime = MutableLiveData<Long>(0)

    // EditText
    var title = MutableLiveData("")
    var location = MutableLiveData("")
    var requirePlayerQty = MutableLiveData("")
    var note = MutableLiveData("")
    var gameName = MutableLiveData("")
    //LngLat
    var lat = MutableLiveData(0.0)
    var lng = MutableLiveData(0.0)

    private val _gameNameList = MutableLiveData<MutableList<String>>(mutableListOf())
    val gameNameList: LiveData<MutableList<String>>
        get() = _gameNameList

    private var _partyGames = MutableLiveData<List<Game>>(mutableListOf())
    val partyGames: LiveData<List<Game>>
        get() = _partyGames

    //Cover
    var photoUri = MutableLiveData<Uri>()

    private val _coverUrl = MutableLiveData<String>()
    private val coverUrl: LiveData<String>
        get() = _coverUrl

    // Handle the error for edittext
    private val _invalidPublish = MutableLiveData<PartyInvalidInput?>()
    val invalidPublish: LiveData<PartyInvalidInput?>
        get() = _invalidPublish

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status
    
    fun addGame() {
        if (gameName.value.checkValid() ) {

            if (gameNameList.value?.contains(gameName.value ?: "") == true) {
                ToastUtil.show(gameName.value + appInstance.getString(R.string.already_in_list))
            } else {
                _gameNameList.value =
                    _gameNameList.value?.plus(gameName.value) as MutableList<String>?
                gameName.value = ""
            }

        } else {
            ToastUtil.show(appInstance.getString(R.string.enter_game_name))
        }
    }
    
    private fun createParty() {
        viewModelScope.launch {
            uploadCover()

            val res = FirebaseService.createParty(
                Party(
                    title = title.value ?: "",
                    cover = coverUrl.value ?: DEFAULT_COVER,
                    partyTime = partyTime.value ?: 0,
                    location = Location(
                        location.value ?: "",
                        lat.value ?: 0.0,
                        lng.value ?: 0.0
                    ),
                    note = note.value ?: "",
                    requirePlayerQty = requirePlayerQty.value?.toIntOrNull() ?: 1,
                    gameNameList = gameNameList.value ?: mutableListOf(),
                )
            )

            if (res) {
                ToastUtil.show(appInstance.getString(R.string.creat_ok))
                _status.value = LoadApiStatus.DONE
            }
        }
    }

    fun removeGame(game: Game) {
        _gameNameList.value = _gameNameList.value?.minus(game.name) as MutableList<String>?
    }

    private suspend fun uploadCover() {
        photoUri.value?.let {
            _status.value = LoadApiStatus.LOADING

            when (val result = FirebaseService.uploadPhoto(it)) {
                is Result.Success -> {
                    _coverUrl.value = result.data!!
                }

                else -> {}
            }
        }
    }

    fun prepareCreate() {
        _invalidPublish.value = when {
            title.value.checkValid() ->
                PartyInvalidInput.TITLE_EMPTY
            partyTime.value == 0L ->
                PartyInvalidInput.TIME_EMPTY
            location.value.checkValid() ->
                PartyInvalidInput.LOCATION_EMPTY
            requirePlayerQty.value.checkValid() ->
                PartyInvalidInput.QTY_EMPTY
            note.value.checkValid() ->
                PartyInvalidInput.DESC_EMPTY
            gameNameList.value == null || gameNameList.value!!.size == 0 ->
                PartyInvalidInput.GAMES_EMPTY
            else -> {
                createParty()
                null
            }
        }
    }

    fun addGameFromFavorite(selectedGames: MutableList<Game>) {
        val list = mutableListOf<String>()
        selectedGames.forEach {
            list.add(it.name)
        }
        _gameNameList.value =
            (_gameNameList.value?.plus(list))?.distinctBy { it } as MutableList<String>
    }

    fun refreshGame() {
        _gameNameList.value = _gameNameList.value
    }

    fun setGame() {
        val list = mutableListOf<Game>()
        gameNameList.value?.forEach { name ->
            val query = allGames.value?.filter { game ->
                game.name == name
            }

            if (!query.isNullOrEmpty()) {
                list.add(query.first())
            } else {
                list.add(
                    Game(
                        id = "notFound",
                        name = name
                    )
                )
            }
        }

        _partyGames.value = list
    }
}