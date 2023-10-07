package com.kappstudio.joboardgame.ui.newparty

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Location
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.repository.GameRepository
import com.kappstudio.joboardgame.data.repository.PartyRepository
import com.kappstudio.joboardgame.data.repository.StorageRepository
import com.kappstudio.joboardgame.util.LoadApiStatus
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.util.ConnectivityUtil
import com.kappstudio.joboardgame.util.checkValid
import kotlinx.coroutines.launch
import com.kappstudio.joboardgame.util.ToastUtil

private const val DEFAULT_COVER =
    "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/cover1.png?alt=media&token=f3144faf-1e81-4d84-b25e-46e32b64b8f1"

class NewPartyViewModel(
    private val partyRepository: PartyRepository,
    private val storageRepository: StorageRepository,
    gameRepository: GameRepository,
) : ViewModel(), NavToGameDetailInterface {

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

    val allGames: LiveData<List<Game>> = gameRepository.getGamesStream().asLiveData()

    private val _gameNameList = MutableLiveData<List<String>>(emptyList())
    val gameNameList: LiveData<List<String>> = _gameNameList

    private var _partyGames = MutableLiveData<List<Game>>(mutableListOf())
    val partyGames: LiveData<List<Game>> = _partyGames

    //Cover
    val imageUri = MutableLiveData<Uri?>()
    private val coverUri = MutableLiveData<String>(DEFAULT_COVER)

    private val _status = MutableLiveData<LoadApiStatus?>()
    val status: LiveData<LoadApiStatus?> = _status

    fun addGame() {
        if (gameName.value.checkValid()) {

            if (gameNameList.value?.contains(gameName.value?.trim()) == true) {
                ToastUtil.show(gameName.value + appInstance.getString(R.string.already_in_list))
            } else {
                _gameNameList.value = _gameNameList.value!! + gameName.value!!
                gameName.value = ""
            }

        } else {
            ToastUtil.show(appInstance.getString(R.string.enter_game_name))
        }
    }

    fun removeGame(gameName: String) {
        _gameNameList.value = _gameNameList.value?.minus(gameName) as MutableList<String>?
    }

    fun setupGames() {
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

    fun addGameFromFavorite(selectedGames: MutableList<Game>) {
        val list = selectedGames.map { it.name }
        _gameNameList.value = (_gameNameList.value!! + list).distinct()
    }

    fun prepareCreate() {
        val invalidInput = when {
            !title.value.checkValid() ->
                PartyInvalidInput.TITLE_EMPTY

            partyTime.value == 0L ->
                PartyInvalidInput.TIME_EMPTY

            !location.value.checkValid() ->
                PartyInvalidInput.LOCATION_EMPTY

            !requirePlayerQty.value.checkValid() ->
                PartyInvalidInput.QTY_EMPTY

            !note.value.checkValid() ->
                PartyInvalidInput.DESC_EMPTY

            gameNameList.value == null || gameNameList.value!!.isEmpty() ->
                PartyInvalidInput.GAMES_EMPTY

            else -> {
                createParty()
                null
            }
        }

        invalidInput?.let { ToastUtil.showByRes(it.stringRes) }
    }

    private fun createParty() {
        if (ConnectivityUtil.isNotConnected()) {
            ToastUtil.showByRes(R.string.check_internet)
            return
        }

        viewModelScope.launch {
            if (imageUri.value != null) {
                uploadCover()
            }

            val newPartyTaskRes = partyRepository.createParty(
                Party(
                    title = title.value!!,
                    cover = coverUri.value!!,
                    partyTime = partyTime.value!!,
                    location = Location(
                        location.value ?: "",
                        lat.value ?: 0.0,
                        lng.value ?: 0.0
                    ),
                    note = note.value ?: "",
                    requirePlayerQty = requirePlayerQty.value?.toIntOrNull() ?: 1,
                    gameNameList = gameNameList.value!!,
                )
            )

            _status.value = if (newPartyTaskRes) {
                ToastUtil.showByRes(R.string.creat_ok)
                LoadApiStatus.DONE
            } else {
                LoadApiStatus.ERROR
            }
        }
    }

    private suspend fun uploadCover() {
        storageRepository.uploadPhoto(imageUri.value!!).collect { photoRes ->
            _status.value = when (photoRes) {
                is Result.Success -> {
                    coverUri.value = photoRes.data ?: DEFAULT_COVER
                    status.value
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