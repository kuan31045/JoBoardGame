package com.kappstudio.jotabletopgame.newparty

import androidx.lifecycle.*
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.appInstance
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.data.NewParty
import com.kappstudio.jotabletopgame.data.source.remote.FirebaseService
import com.kappstudio.jotabletopgame.data.toGameMap
import com.kappstudio.jotabletopgame.gamedetail.NavToGameDetailInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil
import timber.log.Timber

class NewPartyViewModel : ViewModel(), NavToGameDetailInterface {
    var time = MutableLiveData<Long>(0)

    // EditText
    var title = MutableLiveData("")
    var location = MutableLiveData("")
    var requirePlayerQty = MutableLiveData("")
    var note = MutableLiveData("")
    var gameName = MutableLiveData("")

    val _games = MutableLiveData<MutableList<Game>>(mutableListOf())
    val games: LiveData<MutableList<Game>>
        get() = _games

    // Spinner
    val countryPosition = MutableLiveData<Int>()
    val country: LiveData<String> = Transformations.map(countryPosition) {
        appInstance.resources.getStringArray(R.array.country_list)[countryPosition.value ?: 0]
    }
    // nav
    private val _navToGameDetail = MutableLiveData<String?>()
    val navToGameDetail: LiveData<String?>
        get() = _navToGameDetail

    fun addGame() {
        if (gameName.value?.replace("\\s".toRegex(), "") != "") {
            viewModelScope.launch {
                val game = FirebaseService.getGameByName(gameName.value ?: "")
                gameName.value = ""
                if (_games.value?.contains(game) == true) {
                    ToastUtil.show(game.name + appInstance.getString(R.string.already_in_list))
                }else{
                    _games.value = _games.value?.plus(game) as MutableList<Game>?
                }
            }
        } else {
            ToastUtil.show("請輸入遊戲名")
        }

    }


    fun createParty() {

        viewModelScope.launch {

            val gameMapList = mutableListOf<HashMap<String, String>>()
            games.value?.forEach {
                gameMapList.add(toGameMap(it))
            }
            val res = FirebaseService.createParty(
                NewParty(
                    title = title.value ?: "",
                    partyTime = time.value ?: 0,
                    location = location.value ?: "",
                    note = note.value ?: "",
                    requirePlayerQty = requirePlayerQty.value?.toIntOrNull() ?: 1,
                    gameList = gameMapList,
                )

            )

            if (res) {
                ToastUtil.show(appInstance.getString(R.string.creat_ok))
            }

        }


    }

    fun removeGame(game: Game) {
        _games.value= _games.value?.minus(game) as MutableList<Game>
    }

    override fun navToGameDetail(gameId: String) {
        if (gameId != "notFound") {
            _navToGameDetail.value = gameId
        } else {
            ToastUtil.show("資料庫內找不到這款遊戲，麻煩您自行去Google")
        }
    }


    override fun onNavToGameDetail() {
        _navToGameDetail.value = null
    }

}