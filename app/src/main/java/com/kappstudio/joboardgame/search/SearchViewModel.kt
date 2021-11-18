package com.kappstudio.joboardgame.search

import androidx.lifecycle.*
import com.kappstudio.joboardgame.allGames
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.partydetail.NavToPartyDetailInterface
import com.kappstudio.joboardgame.user.NavToUserInterface
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SearchViewModel : ViewModel(), NavToGameDetailInterface, NavToUserInterface,
    NavToPartyDetailInterface {

    var isInit = true


    fun onInit() {
        isInit = false
    }

    // EditText
    var search = MutableLiveData("")

    private var parties = MutableLiveData<List<Party>>()
    private var games = allGames
    private var users = MutableLiveData<List<User>>()

    private var _newParties = MutableLiveData<List<Party>>()
    val newParties: LiveData<List<Party>>
        get() = _newParties

    private var _newGames = MutableLiveData<List<Game>>()
    val newGames: LiveData<List<Game>>
        get() = _newGames

    private var _newUsers = MutableLiveData<List<User>>()
    val newUsers: LiveData<List<User>>
        get() = _newUsers

    init {
        viewModelScope.launch {
            parties = FirebaseService.getLiveParties()
             users = FirebaseService.getLiveUsers()
        }
    }

    fun search() {
        val query = search.value?.replace("\\s".toRegex(), "")?.lowercase(Locale.ROOT) ?: ""
        if (query != "") {
            searchParties(query)
            searchGames(query)
            searchUsers(query)
        }
    }

    private fun searchParties(query: String) {
        val filteredList = mutableListOf<Party>()
        parties.value?.forEach { party ->
            val title = party.title.lowercase(Locale.ROOT)
            val host = party.host.name.lowercase(Locale.ROOT)
            val location = party.location.address.lowercase(Locale.ROOT)
            val time = SimpleDateFormat("yyyy年MM月dd日 hh:mm").format(party.partyTime)
            var game = ""
            party.gameNameList.forEach {
                game += it
            }

            if (title.contains(query)
                || host.contains(query)
                || location.contains(query)
                || time.contains(query)
                || game.contains(query)
            ) {
                filteredList.add(party)
            }
        }
        _newParties.value = filteredList
    }

    private fun searchUsers(query: String) {
        val filteredList = mutableListOf<User>()
        users.value?.forEach { user ->
            val name = user.name.lowercase(Locale.ROOT)
            if (name.contains(query)) {
                filteredList.add(user)
            }
        }
        _newUsers.value = filteredList
    }

    private fun searchGames(query: String) {
        val filteredList = mutableListOf<Game>()
        games.value?.forEach { game ->
            val name = game.name.lowercase(Locale.ROOT)
            var type = ""
            game.type.forEach {
                type+=it
            }

            if (name.contains(query) || type.contains(query)) {
                filteredList.add(game)
            }
        }
        _newGames.value = filteredList
    }
}