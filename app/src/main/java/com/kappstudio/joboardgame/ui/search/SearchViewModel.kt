package com.kappstudio.joboardgame.ui.search

import androidx.lifecycle.*
import com.kappstudio.joboardgame.allGames
import com.kappstudio.joboardgame.allParties
import com.kappstudio.joboardgame.allUsers
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.ui.partydetail.NavToPartyDetailInterface
import com.kappstudio.joboardgame.ui.user.NavToUserInterface
import java.text.SimpleDateFormat
import java.util.*

class SearchViewModel( ) : ViewModel(), NavToGameDetailInterface, NavToUserInterface,
    NavToPartyDetailInterface {

    var isInit = true

    private var _hosts = MutableLiveData<List<User>>()
    override val hosts: LiveData<List<User>>
        get() = _hosts

    fun onInit() {
        isInit = false
    }

    // EditText
    var search = MutableLiveData("")

    private var parties = allParties
    private var games = allGames
    private var users = allUsers

    private var _newParties = MutableLiveData<List<Party>>()
    val newParties: LiveData<List<Party>>
        get() = _newParties

    private var _newGames = MutableLiveData<List<Game>>()
    val newGames: LiveData<List<Game>>
        get() = _newGames

    private var _newUsers = MutableLiveData<List<User>>()
    val newUsers: LiveData<List<User>>
        get() = _newUsers



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
        parties?.value?.forEach { party ->
            val title = party.title.lowercase(Locale.ROOT)

            var host = allUsers.value?.first { it.id == party.hostId }?.name?.lowercase(Locale.ROOT)


            val location = party.location.address.lowercase(Locale.ROOT)
            val time = SimpleDateFormat("yyyy年MM月dd日 hh:mm").format(party.partyTime)
            var game = ""
            party.gameNameList.forEach {
                game += it
            }

            if (host != null) {
                if (title.contains(query)
                    || host.contains(query)
                    || location.contains(query)
                    || time.contains(query)
                    || game.contains(query)
                ) {
                    filteredList.add(party)
                }
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

            if (name.contains(query)) {
                filteredList.add(game)
            }
        }
        _newGames.value = filteredList
    }
}